package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;

public class SettingsSecurityEnterPatternActivity extends AppCompatActivity {

    ImageView back;
    PatternLockView patternLockView;
    String patternString1, patternString2 = "";
    Button confirm, reconfirm, redo;
    boolean whichStep = true;
    TextView infoText;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_security_enter_pattern);

        back = findViewById(R.id.back);
        patternLockView = findViewById(R.id.pattern);
        confirm = findViewById(R.id.confirm);
        reconfirm = findViewById(R.id.reconfirm);
        redo = findViewById(R.id.redo);
        infoText = findViewById(R.id.infoText);

        dbHandler = new DBHandler(this);
        AppInfoModal appInfo = dbHandler.getAppInfo();

        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                if (whichStep) {
                    patternString1 = PatternLockUtils.patternToString(patternLockView, pattern);
                    Log.i("test", patternString1);

                    if (patternString1.length() <= 3) {
                        alertToastMaker("الگو باید حداقل از 4 نقطه تشکیل شده باشد!");
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> patternLockView.clearPattern(), 2000);

                    } else {
                        redo.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        whichStep = false;
                        patternLockView.setInputEnabled(false);
                    }
                } else {
                    patternString2 = PatternLockUtils.patternToString(patternLockView, pattern);
                    infoText.setText("الگو را دوباره وارد کنید:");

                    if (patternString2.length() <= 3) {
                        alertToastMaker("الگو باید حداقل از 4 نقطه تشکیل شده باشد!");
                        patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(() -> {
                            patternLockView.clearPattern();
                        }, 2000);

                    } else {
                        if (patternString2.equals(patternString1)) {
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.AUTO_DRAW);
                            infoText.setText("الگوی وارد شده شما :");
                            Log.i("test", patternString2);
                            patternLockView.setCorrectStateColor(getResources().getColor(R.color.green, null));
                            reconfirm.setVisibility(View.VISIBLE);
                            confirm.setVisibility(View.INVISIBLE);
                            patternLockView.setInputEnabled(false);

                        } else {
                            alertToastMaker("الگوها با یکدیگر همخوانی ندارند!\nدوباره تلاش کنید.");
                            patternLockView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                            final Handler handler = new Handler(Looper.getMainLooper());
                            handler.postDelayed(() -> {
                                patternLockView.clearPattern();
                            }, 2000);
                        }

                    }

                }
            }

            @Override
            public void onCleared() {
                patternString1 = "";
                whichStep = true;
                infoText.setText("الگوی خود را وارد کنید:");
                patternLockView.setCorrectStateColor(getResources().getColor(R.color.blue_500, null));
                reconfirm.setVisibility(View.GONE);
                patternLockView.setInputEnabled(true);

            }
        });

        reconfirm.setOnClickListener(v -> {
            appInfo.setAppPatternEnabled("1");
            appInfo.setAppPattern(patternString2);
            dbHandler.updateAppInfo(appInfo);
            infoToastMaker("ورود با الگو فعال شد.");
            Intent intent = new Intent(SettingsSecurityEnterPatternActivity.this, SettingsSecurityActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        confirm.setOnClickListener(v -> {
            patternLockView.clearPattern();
            confirm.setVisibility(View.INVISIBLE);
            infoText.setText("الگو را دوباره وارد کنید:");
            patternLockView.setInputEnabled(true);
        });

        redo.setOnClickListener(v -> {
            patternLockView.clearPattern();
            confirm.setVisibility(View.GONE);
            reconfirm.setVisibility(View.GONE);
            redo.setVisibility(View.GONE);
            whichStep = true;
            patternLockView.setInputEnabled(true);
            patternString1 = "";
            infoText.setText("الگوی خود را وارد کنید:");
            patternLockView.setCorrectStateColor(getResources().getColor(R.color.blue_500, null));
            reconfirm.setVisibility(View.GONE);
        });

        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SettingsSecurityEnterPatternActivity.this, SettingsSecurityActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(SettingsSecurityEnterPatternActivity.this);
        toast.setDuration(Toast.LENGTH_SHORT);
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
        Toast toast = new Toast(SettingsSecurityEnterPatternActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}