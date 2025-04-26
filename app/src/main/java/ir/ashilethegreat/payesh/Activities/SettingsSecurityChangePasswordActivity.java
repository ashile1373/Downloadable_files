package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class SettingsSecurityChangePasswordActivity extends AppCompatActivity {

    ImageView back;
    Button changePassword, backToLogin;
    TextInputLayout oldPasswordLayout, newPasswordLayout, renewPasswordLayout;
    EditText oldPassword, newPassword, renewPassword;
    CardView offlineLayout;
    NestedScrollView nestedScrollView;
    DBHandler dbHandler;
    CheckBox numberAndSignChecker, caseChecker, counterChecker;
    ProgressBar changePasswordProgressBar;
    RequestQueue requestQueue;
    UserModal userModal;
    final int DEFAULT_TIMEOUT = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_security_change_password);

        back = findViewById(R.id.back);
        changePassword = findViewById(R.id.changePassword);
        changePasswordProgressBar = findViewById(R.id.changePasswordProgressBar);
        oldPasswordLayout = findViewById(R.id.oldPasswordLayout);
        newPasswordLayout = findViewById(R.id.newPasswordLayout);
        renewPasswordLayout = findViewById(R.id.renewPasswordLayout);
        oldPassword = findViewById(R.id.oldPassword);
        newPassword = findViewById(R.id.newPassword);
        renewPassword = findViewById(R.id.renewPassword);
        numberAndSignChecker = findViewById(R.id.numberAndSignChecker);
        caseChecker = findViewById(R.id.caseChecker);
        counterChecker = findViewById(R.id.counterChecker);
        offlineLayout = findViewById(R.id.offlineLayout);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        backToLogin = findViewById(R.id.backToLogin);

        dbHandler = new DBHandler(this);
        requestQueue = Volley.newRequestQueue(this);
        userModal = dbHandler.getUser();

        if (userModal.getLoginMode() == 1) {
            nestedScrollView.setVisibility(View.VISIBLE);
            offlineLayout.setVisibility(View.INVISIBLE);
        } else {
            nestedScrollView.setVisibility(View.INVISIBLE);
            offlineLayout.setVisibility(View.VISIBLE);
        }

        oldPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newPasswordLayout.setError(null);
                renewPasswordLayout.setError(null);

                if (!newPassword.getText().toString().isEmpty()) {
                    caseChecker.setChecked(newPassword.getText().toString().matches(".*[a-z]+.*") && newPassword.getText().toString().matches(".*[A-Z]+.*"));
                    numberAndSignChecker.setChecked(newPassword.getText().toString().matches(".*[0-9]+.*") && newPassword.getText().toString().matches(".*[!#$%]+.*"));
                    counterChecker.setChecked(newPassword.getText().toString().length() >= 8);
                } else {
                    counterChecker.setChecked(false);
                    numberAndSignChecker.setChecked(false);
                    caseChecker.setChecked(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        renewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                renewPasswordLayout.setError(null);
                newPasswordLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        changePassword.setOnClickListener(v -> {
            if (userModal.getLoginMode() == 1) {
                if (checkErrors())
                    alertToastMaker("خطاهای فرم را برطرف کنید.");
                else {
                    changePasswordProgressBar.setVisibility(View.VISIBLE);
                    changePassword.setVisibility(View.INVISIBLE);
                    editPassword(newPassword.getText().toString().trim(), newPassword.getText().toString().trim(), changePassword, changePasswordProgressBar);
                }
            } else {
                errorToastMaker("برای تغییر رمز عبور باید به صورت آنلاین وارد حساب کاربری خود شوید.");
            }
        });

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsSecurityChangePasswordActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsSecurityChangePasswordActivity.this, SettingsSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retValue = false;
        if (!oldPassword.getText().toString().trim().equals(dbHandler.getUser().getPassword())) {
            oldPasswordLayout.setError("رمز عبور پیشین نادرست است!");
            retValue = true;
        }
        if (!newPassword.getText().toString().equals(renewPassword.getText().toString())) {
            newPasswordLayout.setError("");
            renewPasswordLayout.setError("رمز عبورهای جدید همخوانی ندارند!");
            retValue = true;
        }
        if (newPassword.getText().toString().length() < 8 || renewPassword.getText().toString().length() < 8) {
            newPasswordLayout.setError("");
            renewPasswordLayout.setError("رمز عبور جدید نمی تواند از 8 کاراکتر کمتر باشد!");
            retValue = true;
        }
        if (newPassword.getText().toString().equals(renewPassword.getText().toString())) {
            if (!Pattern.matches("^.*(?=.{3,})(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[\\d\\\\x])(?=.*[!$#%]).*$", newPassword.getText().toString())) {
                renewPasswordLayout.setError("قالب رمز عبور صحیح نیست!");
                retValue = true;
            }
        }
        if (!caseChecker.isChecked() || !counterChecker.isChecked() || !numberAndSignChecker.isChecked()) {
            renewPasswordLayout.setError("قالب رمز عبور صحیح نیست!");
            retValue = true;
        }
        return retValue;
    }

    private void editPassword(String newPassword, String renewPassword, View changePassword, View changePasswordProgressBar) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("password", newPassword);
            jsonBody.put("password_confirmation", renewPassword);

        } catch (JSONException ignored) {
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLEditProfile), jsonBody,
                response -> {

                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response.getString("status").equals("1")) {
                            successfulToastMaker("رمز عبور با موفقیت تغییر یافت!");
                            alertToastMaker("لطفاً دوباره وارد حساب کاربری خود شوید.");
                            Intent intent = new Intent(SettingsSecurityChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        } else {
                            changePasswordProgressBar.setVisibility(View.INVISIBLE);
                            changePassword.setVisibility(View.VISIBLE);
                            errorToastMaker(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس به روز رسانی اطلاعات کاربری");
                        changePasswordProgressBar.setVisibility(View.INVISIBLE);
                        changePassword.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    errorToastMaker("متأسفانه خطایی رخ داد! ارتباط اینترنتی خود را بررسی کنید.");
                    changePasswordProgressBar.setVisibility(View.INVISIBLE);
                    changePassword.setVisibility(View.VISIBLE);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                UserModal user = dbHandler.getUser();
                headers.put("Authorization", "Bearer " + user.getToken());
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void refreshAccessToken() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", dbHandler.getUser().getUsername());
            jsonBody.put("password", dbHandler.getUser().getPassword());

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
                recreate();
            } catch (JSONException e) {
                // this will never happen but if so, show error to user.
            }
        }, error -> {
            // show error to user. refresh failed.
            Log.e("test", new String(error.networkResponse.data));

        });
        requestQueue.add(refreshTokenRequest);
    }

    public void alertToastMaker(String s) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(s);
        Toast toast = new Toast(SettingsSecurityChangePasswordActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void successfulToastMaker(String s) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_successful, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(s);
        Toast toast = new Toast(SettingsSecurityChangePasswordActivity.this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}