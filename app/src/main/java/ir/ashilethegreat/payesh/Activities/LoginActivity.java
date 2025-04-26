package ir.ashilethegreat.payesh.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import ir.ashilethegreat.payesh.BuildConfig;
import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.FingerPrintDetector;
import ir.ashilethegreat.payesh.R;


public class LoginActivity extends AppCompatActivity implements FingerPrintDetector.CallActivity {

    final int PERMISSION_CODE = 1000;
    final int DEFAULT_TIMEOUT = 20000;
    EditText username, password;
    Button login, update;

    ImageView fingerPrint;
    TextView loginSituation;
    private final String KEY_NAME = "AndroidKey";
    KeyGenerator keyGenerator;
    LinearLayout fingerPrintLoginLayout, patternLoginLayout, updateLayout;
    KeyStore keyStore;
    Cipher cipher;
    Dialog dialog;
    FingerprintManager fingerprintManager;
    ProgressBar loginProgressBar, dialogLoginProgressBar;
    ExtendedFloatingActionButton cancel;
    String usernameString, passwordString;
    DBHandler dbHandler;
    int lastVersion, essentialVersion;
    String updateURL, message;
    AppInfoModal appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        fingerPrintLoginLayout = findViewById(R.id.fingerPrintLoginLayout);
        patternLoginLayout = findViewById(R.id.patternLoginLayout);
        loginProgressBar = findViewById(R.id.loginProgressBar);
        updateLayout = findViewById(R.id.updateLayout);
        update = findViewById(R.id.update);

        dbHandler = new DBHandler(LoginActivity.this);
        UserModal user = dbHandler.getUser();
        appInfo = dbHandler.getAppInfo();

        if (Integer.parseInt(appInfo.getAppLastVersion()) > BuildConfig.VERSION_CODE) {
            updateLayout.setVisibility(View.VISIBLE);
        }

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        }

        if (dbHandler.isUserTableEmpty())
            fingerPrintLoginLayout.setVisibility(View.GONE);
        else username.setText(user.getUsername());

        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                !dbHandler.isUserTableEmpty()) {
            // never add fingerprintmanager.ishardwaredetected()!! cause error on some devices
            if (appInfo.getAppFingerPrintEnabled() != null) {
                if (appInfo.getAppFingerPrintEnabled().equals("1"))
                    fingerPrintLoginLayout.setVisibility(View.VISIBLE);
            }
            username.setText(user.getUsername());
        }

        if (appInfo.getAppPatternEnabled() != null) {
            if (appInfo.getAppPatternEnabled().equals("1"))
                patternLoginLayout.setVisibility(View.VISIBLE);
        }
        login.setOnClickListener(v -> {

            if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
                loginProgressBar.setVisibility(View.VISIBLE);
                login.setVisibility(View.INVISIBLE);
                fingerPrintLoginLayout.setVisibility(View.GONE);
                patternLoginLayout.setVisibility(View.GONE);

                usernameString = username.getText().toString();
                passwordString = password.getText().toString();

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", usernameString);
                    jsonBody.put("password", passwordString);
                } catch (JSONException e) {
                    errorToastMaker("خطای ساخت اطلاعات حساب کاربری");
                }

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody,
                        response -> {
                            try {
                                if (response.getString("status").equals("1")) {

                                    JSONObject lastVersionString = new JSONObject(response.getString("version_last"));
                                    JSONObject essentialVersionString = new JSONObject(response.getString("version_essential"));

                                    lastVersion = Integer.parseInt(lastVersionString.getString("version"));
                                    essentialVersion = Integer.parseInt(essentialVersionString.getString("version"));
                                    updateURL = lastVersionString.getString("url");
                                    message = lastVersionString.getString("message");

                                    appInfo.setAppLastVersion(lastVersionString.getString("version"));
                                    appInfo.setAppEssentialVersion(essentialVersionString.getString("version"));
                                    appInfo.setAppUpdateURL(updateURL);
                                    appInfo.setAppNewFeatures(message);

                                    dbHandler.updateAppInfo(appInfo);

                                    if (BuildConfig.VERSION_CODE == lastVersion || BuildConfig.VERSION_CODE >= essentialVersion) {

                                        String loginToken = response.getString("token");
                                        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET, getString(R.string.URLProfile), jsonBody, response1 -> {
                                            try {

                                                if (dbHandler.isUserTableEmpty()) {
                                                    dbHandler.addUserInfo("1", response1.getString("id"), loginToken, usernameString, passwordString,
                                                            response1.getString("name"), response1.getString("family"), response1.getString("phone"), 1, 1);
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                } else {
                                                    UserModal userModal = dbHandler.getUser();
                                                    userModal.setId(response1.getString("id"));
                                                    userModal.setToken(loginToken);
                                                    userModal.setUsername(usernameString);
                                                    userModal.setPassword(passwordString);
                                                    userModal.setFirstName(response1.getString("name"));
                                                    userModal.setLastName(response1.getString("family"));
                                                    userModal.setPhone(response1.getString("phone"));
                                                    userModal.setLoginMode(1);
                                                    userModal.setUserType(1);

                                                    dbHandler.updateUserInfo(userModal);
                                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                }
                                                overridePendingTransition(0, 0);
                                                finish();
                                            } catch (JSONException e) {
                                                loginProgressBar.setVisibility(View.INVISIBLE);
                                                login.setVisibility(View.VISIBLE);
                                                errorToastMaker("خطای سرویس ورود به حساب کاربری");
                                            }

                                        }, error -> {

                                            loginProgressBar.setVisibility(View.INVISIBLE);
                                            login.setVisibility(View.VISIBLE);
                                            if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                                                    !dbHandler.isUserTableEmpty()) {
                                                // never add fingerprintmanager.ishardwaredetected()!! cause error on some devices
                                                fingerPrintLoginLayout.setVisibility(View.VISIBLE);
                                            }
                                            alertToastMaker("اطلاعات کاربری خود را به درستی وارد کنید.");
                                        }
                                        ) {
                                            @Override
                                            public Map<String, String> getHeaders() {
                                                HashMap<String, String> headers = new HashMap<>();
//                                            String credentials = "username:password";
//                                            String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                                            headers.put("Accept", "application/json");
//                                            headers.put("Content-Type", "application/json");
                                                headers.put("Authorization", "Bearer " + loginToken);
                                                return headers;
                                            }
                                        };
                                        requestQueue.add(profileRequest);
                                        profileRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                DEFAULT_TIMEOUT,
                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    } else if (essentialVersion > BuildConfig.VERSION_CODE) {
                                        loginProgressBar.setVisibility(View.INVISIBLE);
                                        login.setVisibility(View.VISIBLE);

                                        createUpdateEssentialDialog();
                                    }
                                } else /*if (response.getString("status").equals("0"))*/ {
                                    loginProgressBar.setVisibility(View.INVISIBLE);
                                    login.setVisibility(View.VISIBLE);
                                    alertToastMaker("نام کاربری یا رمز عبور نادرست است!");
                                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED &&
                                            !dbHandler.isUserTableEmpty()) {
                                        // never add fingerprintmanager.ishardwaredetected()!! cause error on some devices
                                        fingerPrintLoginLayout.setVisibility(View.VISIBLE);
                                    }
                                }

                            } catch (JSONException e) {
                                loginProgressBar.setVisibility(View.INVISIBLE);
                                login.setVisibility(View.VISIBLE);
                                alertToastMaker("خطای سرویس ورود به حساب کاربری");
                            }
                        },
                        error -> {
                            if (!dbHandler.isUserTableEmpty())
                                if (user.getUsername().equals(usernameString) && user.getPassword().equals(passwordString)) {
                                    Intent intent = new Intent(LoginActivity.this, LoginOfflineActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                    finish();
                                }
                            error.printStackTrace();

                            loginProgressBar.setVisibility(View.INVISIBLE);
                            login.setVisibility(View.VISIBLE);
                            alertToastMaker("ارتباط اینترنتی خود را بررسی کنید.");
                        }) {
//                    @Override
//                    public String getBodyContentType() {
//                        return "application/json; charset=utf-8";
//                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {
                        String json;
                        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                            try {
                                json = new String(volleyError.networkResponse.data,
                                        HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                            } catch (UnsupportedEncodingException e) {
                                return new VolleyError(e.getMessage());
                            }
                            return new VolleyError(json);
                        }
                        return volleyError;
                    }
                };
                //
                //
                requestQueue.add(jsonRequest);
                jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                        DEFAULT_TIMEOUT,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            } else alertToastMaker("نام کاربری و رمز عبور را وارد کنید!");
        });

        fingerPrintLoginLayout.setOnClickListener(v -> {
            if (fingerprintManager.hasEnrolledFingerprints()) {
                dialog = new Dialog(LoginActivity.this);

                dialog.setContentView(R.layout.custom_login_fingerprint_dialog);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);

                fingerPrint = dialog.findViewById(R.id.fingerPrint);
                loginSituation = dialog.findViewById(R.id.loginSituation);
                dialogLoginProgressBar = dialog.findViewById(R.id.loginProgressBar);
                cancel = dialog.findViewById(R.id.cancel);

                dialog.show();

                cancel.setOnClickListener(v1 -> dialog.dismiss());

                try {
                    generateKey();
                } catch (KeyStoreException e) {
                    errorToastMaker("خطای ساخت کلید ورود با اثر انگشت");
                }

                if (initCipher()) {
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);
                    FingerPrintDetector helper = new FingerPrintDetector(this, loginSituation, fingerPrint, dialogLoginProgressBar, cancel, this);
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            } else errorToastMaker("اثر انگشتی یافت نشد!");
        });

        patternLoginLayout.setOnClickListener(v -> {

            dialog = new Dialog(LoginActivity.this);

            dialog.setContentView(R.layout.custom_login_pattern_dialog);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ProgressBar patternProgressBar = dialog.findViewById(R.id.patternProgressBar);
            Button cancel = dialog.findViewById(R.id.cancel);
            PatternLockView patternLock = dialog.findViewById(R.id.patternLock);

            patternLock.addPatternLockListener(new PatternLockViewListener() {
                @Override
                public void onStarted() {

                }

                @Override
                public void onProgress(List<PatternLockView.Dot> progressPattern) {

                }

                @Override
                public void onComplete(List<PatternLockView.Dot> pattern) {
                    String patternString1 = PatternLockUtils.patternToString(patternLock, pattern);
                    if (patternString1.equals(appInfo.getAppPattern())) {
                        patternProgressBar.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                        patternLock.setCorrectStateColor(getResources().getColor(R.color.green, null));
                        patternLock.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                        new Handler().postDelayed(() -> setResult(), 1000);
                    } else {
                        shortAlertToastMaker("الگوی ورودی نادرست است!\nدوباره تلاش کنید.");
                        patternLock.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(patternLock::clearPattern, 2000);
                    }
                }

                @Override
                public void onCleared() {

                }
            });

            dialog.show();

            cancel.setOnClickListener(v1 -> dialog.dismiss());

        });

        update.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(appInfo.getAppUpdateURL()));
            startActivity(i);
        });
//        update.setOnClickListener(v -> {
//            Intent intent = new Intent(LoginActivity.this, DownloadCenterActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            finish();
//        });
    }

    private void createUpdateEssentialDialog() {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_app_info_checker_update_essential);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        Button confirm = dialog.findViewById(R.id.confirm);
        Button cancel = dialog.findViewById(R.id.cancel);

        confirm.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(appInfo.getAppUpdateURL()));
            startActivity(i);
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }

    private void generateKey() throws KeyStoreException {
        try {

            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            keyGenerator.generateKey();

        } catch (KeyStoreException
                 | NoSuchAlgorithmException
                 | NoSuchProviderException
                 | InvalidAlgorithmParameterException
                 | CertificateException
                 | IOException exc) {
            exc.printStackTrace();
        }
    }

    private boolean initCipher() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);

        } catch (NoSuchAlgorithmException |
                 NoSuchPaddingException e) {
            errorToastMaker("خطای تبدیل کلید اثر انگشت");
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;


        } catch (KeyPermanentlyInvalidatedException e) {
            return false;

        } catch (KeyStoreException | CertificateException
                 | UnrecoverableKeyException | IOException
                 | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();

            errorToastMaker("اثر انگشتی در تلفن همراه ثبت نشده است.");
            return false;
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.USE_FINGERPRINT}, PERMISSION_CODE);
    }


    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(LoginActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(LoginActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(LoginActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void shortAlertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(LoginActivity.this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    @Override
    public void setResult() {
        setResult(RESULT_OK);

        UserModal user = dbHandler.getUser();

        username.setText(user.getUsername());
        password.setText(user.getPassword());
        login.performClick();
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}