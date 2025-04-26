package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.AppNewFeaturesRecyclerViewAdapter;
import ir.ashilethegreat.payesh.BuildConfig;
import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class SettingsActivity extends AppCompatActivity {

    ImageView back , profilePicture;
    ExtendedFloatingActionButton profileDetails;
    TextView profileName,appVersion;
    DBHandler dbHandler;
    UserModal userModal;
    CardView settingsSecurityLayout, settingsAppearanceLayout, settingsFeedbackLayout,
            settingsVersionLayout,settingsBackupLayout;
    String lastVersion, essentialVersion, updateURL, message;
    private final int DEFAULT_TIMEOUT = 20000;
    AppInfoModal appInfoModal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.back);
        profileName = findViewById(R.id.profileName);
        profilePicture = findViewById(R.id.profilePic);
        profileDetails = findViewById(R.id.btnProfileDetails);
        settingsSecurityLayout = findViewById(R.id.settingsSecurityLayout);
        settingsAppearanceLayout = findViewById(R.id.settingsAppearanceLayout);
        settingsFeedbackLayout = findViewById(R.id.settingsFeedbackLayout);
        settingsBackupLayout = findViewById(R.id.settingsBackupLayout);
        settingsVersionLayout = findViewById(R.id.settingsVersionLayout);
        appVersion = findViewById(R.id.appVersion);

        dbHandler = new DBHandler(this);
        userModal = dbHandler.getUser();
        appInfoModal = dbHandler.getAppInfo();

        appVersion.setText(BuildConfig.VERSION_NAME);

        if (appInfoModal.getAppTheme() != null){
            switch (appInfoModal.getAppTheme()) {
                case "0" -> //automatic theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                case "1" -> //theme light
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                case "2" -> //theme dark
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }

        if (userModal.getPic() != null) {
            byte[] decodedString = Base64.decode(userModal.getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(decodedByte);
        }
        profileName.setText(String.format("%s %s", userModal.getFirstName(), userModal.getLastName()));

        profileDetails.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, ProfileDetailsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        settingsSecurityLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, SettingsSecurityActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        settingsAppearanceLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, SettingsAppearanceActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        settingsFeedbackLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(SettingsActivity.this, SettingsSecurityActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            finish();
            infoToastMaker("این امکان به زودی در دسترس قرار خواهد گرفت.");
        });

        settingsBackupLayout.setOnClickListener(v -> {
//            Intent intent = new Intent(SettingsActivity.this, SettingsSecurityActivity.class);
//            startActivity(intent);
//            overridePendingTransition(0, 0);
//            finish();
            infoToastMaker("این امکان به زودی در دسترس قرار خواهد گرفت.");
        });

        settingsVersionLayout.setOnClickListener(v -> {
            createUpdateDialog();
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SettingsActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void createUpdateDialog() {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_settings_update_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        ExtendedFloatingActionButton versionOkConfirm = dialog.findViewById(R.id.versionOkConfirm);
        ExtendedFloatingActionButton versionNewConfirm = dialog.findViewById(R.id.versionNewConfirm);
        ExtendedFloatingActionButton versionNewCancel = dialog.findViewById(R.id.versionNewCancel);
        LinearLayout versionNewLayout = dialog.findViewById(R.id.versionNewLayout);
        LinearLayout versionOkLayout = dialog.findViewById(R.id.versionOkLayout);
        RecyclerView versionNewFeaturesRecyclerView = dialog.findViewById(R.id.versionNewFeaturesRecyclerView);
        ProgressBar progressBar = dialog.findViewById(R.id.progressBar);

        if (!userModal.getUsername().isEmpty() && !userModal.getPassword().isEmpty()) {

            String usernameString = userModal.getUsername();
            String passwordString = userModal.getPassword();

            RequestQueue requestQueue = Volley.newRequestQueue(this);

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("username", usernameString);
                jsonBody.put("password", passwordString);
            } catch (JSONException ignored) {
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody,
                    response -> {
                        try {
                            if (response.getString("status").equals("1")) {
                                JSONObject lastVersionString = new JSONObject(response.getString("version_last"));
                                JSONObject essentialVersionString = new JSONObject(response.getString("version_essential"));

                                lastVersion = lastVersionString.getString("version");
                                essentialVersion = essentialVersionString.getString("version");
                                updateURL = lastVersionString.getString("url");
                                message = lastVersionString.getString("message");

                                appInfoModal.setAppLastVersion(lastVersion);
                                appInfoModal.setAppUpdateURL(updateURL);
                                appInfoModal.setAppNewFeatures(message.replaceAll("/", ""));

                                dbHandler.updateAppInfo(appInfoModal);

                                progressBar.setVisibility(View.INVISIBLE);

                                if (Integer.parseInt(lastVersion) == BuildConfig.VERSION_CODE) {
                                    versionOkLayout.setVisibility(View.VISIBLE);
                                } else {

                                    ArrayList<String> appFeatures = new ArrayList<>();
                                    JSONArray jsonArray = null;
                                    try {
                                        jsonArray = new JSONArray(appInfoModal.getAppNewFeatures());
                                    } catch (JSONException ignored) {

                                    }
                                    for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                                        try {
                                            appFeatures.add(jsonArray.get(i).toString());
                                        } catch (JSONException ignored) {
                                        }
                                    }

                                    AppNewFeaturesRecyclerViewAdapter adapter = new AppNewFeaturesRecyclerViewAdapter(appFeatures);
                                    versionNewFeaturesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                                    versionNewFeaturesRecyclerView.setAdapter(adapter);
                                    versionNewLayout.setVisibility(View.VISIBLE);

                                }
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.INVISIBLE);
                            alertToastMaker("خطای سرویس ورود به حساب کاربری");
                            dialog.dismiss();
                        }
                    },
                    error -> {
                        progressBar.setVisibility(View.INVISIBLE);
                        alertToastMaker("ارتباط اینترنتی خود را بررسی کنید.");
                        dialog.dismiss();
                    }) {
            };

            requestQueue.add(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        versionNewConfirm.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(appInfoModal.getAppUpdateURL()));
            startActivity(i);
        });

        versionOkConfirm.setOnClickListener(v -> dialog.dismiss());
        versionNewCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(SettingsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(SettingsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}