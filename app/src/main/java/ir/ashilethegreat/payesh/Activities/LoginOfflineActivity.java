package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class LoginOfflineActivity extends AppCompatActivity {

    Button back, offlineLogin;

    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_offline);

        back = findViewById(R.id.back);
        offlineLogin = findViewById(R.id.offlineLogin);

        dbHandler = new DBHandler(LoginOfflineActivity.this);

        UserModal user = dbHandler.getUser();

        back.setOnClickListener(v -> onBackPressed());

        offlineLogin.setOnClickListener(v -> {
            user.setLoginMode(2);
            dbHandler.updateUserInfo(user);

            Intent intent = new Intent(LoginOfflineActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LoginOfflineActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}