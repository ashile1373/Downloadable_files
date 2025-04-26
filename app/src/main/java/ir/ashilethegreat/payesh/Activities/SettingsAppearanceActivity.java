package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;

public class SettingsAppearanceActivity extends AppCompatActivity {

    ImageView back;
    Button confirm;
    MaterialCardView darkLogo, lightLogo;
    DBHandler dbHandler;
    AppInfoModal appInfoModal;
    AutoCompleteTextView theme;
    ArrayAdapter<String> adapterOfOptions;
    String[] options = {"زمینه خودکار", "زمینه روشن", "زمینه تیره"};
    RelativeLayout lightView, darkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_appearance);

        back = findViewById(R.id.back);
        darkLogo = findViewById(R.id.darkLogo);
        lightLogo = findViewById(R.id.lightLogo);
        theme = findViewById(R.id.theme);
        lightView = findViewById(R.id.lightView);
        darkView = findViewById(R.id.darkView);
        confirm = findViewById(R.id.confirm);

        dbHandler = new DBHandler(this);
        appInfoModal = dbHandler.getAppInfo();

        if (appInfoModal.getAppLogo().equals("0")) {
            lightLogo.setStrokeColor(getColor(R.color.blue_200));
            darkLogo.setStrokeColor(getColor(R.color.gray));
            darkLogo.setEnabled(true);
            lightLogo.setEnabled(false);
        } else {
            darkLogo.setStrokeColor(getColor(R.color.blue_200));
            lightLogo.setStrokeColor(getColor(R.color.gray));
            darkLogo.setEnabled(false);
            lightLogo.setEnabled(true);
        }
        switch (appInfoModal.getAppTheme()) {
            case "0" -> {
                theme.setText("زمینه خودکار");
                lightView.setVisibility(View.GONE);
                darkView.setVisibility(View.GONE);
            }
            case "1" -> {
                theme.setText("زمینه روشن");
                lightView.setVisibility(View.VISIBLE);
                darkView.setVisibility(View.GONE);
            }
            case "2" -> {
                theme.setText("زمینه تیره");
                lightView.setVisibility(View.GONE);
                darkView.setVisibility(View.VISIBLE);
            }
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, options);
        theme.setAdapter(adapterOfOptions);

        theme.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (theme.getText().toString()) {
                    case "زمینه خودکار" -> {
                        lightView.setVisibility(View.GONE);
                        darkView.setVisibility(View.GONE);
                    }
                    case "زمینه روشن" -> {
                        lightView.setVisibility(View.VISIBLE);
                        darkView.setVisibility(View.GONE);
                    }
                    case "زمینه تیره" -> {
                        lightView.setVisibility(View.GONE);
                        darkView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        lightLogo.setOnClickListener(v -> {
            createCloseAppDialog(0);
        });

        darkLogo.setOnClickListener(v -> {
            createCloseAppDialog(1);
        });

        confirm.setOnClickListener(v -> {
            switch (theme.getText().toString()) {
                case "زمینه خودکار" -> {
                    appInfoModal.setAppTheme("0");
                }
                case "زمینه روشن" -> {
                    appInfoModal.setAppTheme("1");
                }
                case "زمینه تیره" -> {
                    appInfoModal.setAppTheme("2");
                }
            }
            dbHandler.updateAppInfo(appInfoModal);

            infoToastMaker("تنظیمات اعمال شد!");
            Intent intent = new Intent(SettingsAppearanceActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        back.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SettingsAppearanceActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void createCloseAppDialog(int situation) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_settings_close_app_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        Button confirm = dialog.findViewById(R.id.confirm);
        Button cancel = dialog.findViewById(R.id.cancel);

        confirm.setOnClickListener(v -> {
            if (situation == 1) {
                darkLogo.setEnabled(false);
                lightLogo.setEnabled(true);
                appInfoModal.setAppLogo("1");
                dbHandler.updateAppInfo(appInfoModal);
                infoToastMaker("نمای تیره فعال شد.");

                // disable old icon
                PackageManager manager = getPackageManager();
                manager.setComponentEnabledSetting(
                        new ComponentName(SettingsAppearanceActivity.this, SplashActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);

                // enable new icon
                manager.setComponentEnabledSetting(
                        new ComponentName(SettingsAppearanceActivity.this, DarkSplashActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);
                dialog.dismiss();
                finish();
            } else if (situation == 0) {
                darkLogo.setEnabled(true);
                lightLogo.setEnabled(false);
                appInfoModal.setAppLogo("0");
                dbHandler.updateAppInfo(appInfoModal);
                infoToastMaker("نمای روشن فعال شد.");

                PackageManager manager = getPackageManager();
                manager.setComponentEnabledSetting(
                        new ComponentName(SettingsAppearanceActivity.this, SplashActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                        PackageManager.DONT_KILL_APP);

                manager.setComponentEnabledSetting(
                        new ComponentName(SettingsAppearanceActivity.this, DarkSplashActivity.class),
                        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                        PackageManager.DONT_KILL_APP);
                dialog.dismiss();
                finish();
            }
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(SettingsAppearanceActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}