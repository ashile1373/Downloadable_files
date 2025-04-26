package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.switchmaterial.SwitchMaterial;

import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;

public class SettingsSecurityActivity extends AppCompatActivity {

    ImageView back;
    DBHandler dbHandler;
    SwitchMaterial fingerPrintSwitch, patternSwitch;
    CardView fingerPrintLayout,patternLayout, passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_security);

        back = findViewById(R.id.back);
        passwordLayout = findViewById(R.id.passwordLayout);
        fingerPrintSwitch = findViewById(R.id.fingerPrintSwitch);
        patternSwitch = findViewById(R.id.patternSwitch);
        fingerPrintLayout = findViewById(R.id.fingerPrintLayout);
        patternLayout = findViewById(R.id.patternLayout);

        dbHandler = new DBHandler(this);
        AppInfoModal appInfoModal = dbHandler.getAppInfo();

        fingerPrintSwitch.setChecked(appInfoModal.getAppFingerPrintEnabled().equals("1"));
        patternSwitch.setChecked(appInfoModal.getAppPatternEnabled().equals("1"));

        passwordLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SettingsSecurityActivity.this, SettingsSecurityChangePasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            infoToastMaker("این امکان به زودی در دسترس قرار خواهد گرفت.");
        });

        fingerPrintLayout.setOnClickListener(v -> fingerPrintSwitch.setChecked(!fingerPrintSwitch.isChecked()));

        patternLayout.setOnClickListener(v -> patternSwitch.setChecked(!patternSwitch.isChecked()));

        fingerPrintSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                buttonView.setChecked(false);
                appInfoModal.setAppFingerPrintEnabled("0");
                dbHandler.updateAppInfo(appInfoModal);
                infoToastMaker("ورود با حسگر اثر انگشت غیر فعال شد.");
            } else {
                buttonView.setChecked(true);
                appInfoModal.setAppFingerPrintEnabled("1");
                dbHandler.updateAppInfo(appInfoModal);
                infoToastMaker("ورود با حسگر اثر انگشت فعال شد.");
            }
        });

        patternSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                buttonView.setChecked(false);
                appInfoModal.setAppPatternEnabled("0");
                appInfoModal.setAppPattern("");
                dbHandler.updateAppInfo(appInfoModal);
                infoToastMaker("ورود با الگو غیر فعال شد.");
            } else {
                Intent intent = new Intent(SettingsSecurityActivity.this, SettingsSecurityEnterPatternActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        back.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(SettingsSecurityActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(SettingsSecurityActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}