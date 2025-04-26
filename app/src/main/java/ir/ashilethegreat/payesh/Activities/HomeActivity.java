package ir.ashilethegreat.payesh.Activities;


import android.app.Activity;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.AppNewFeaturesRecyclerViewAdapter;
import ir.ashilethegreat.payesh.BuildConfig;
import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;


public class HomeActivity extends AppCompatActivity {

    CardView planInputRequestsLayout, planPendingToSupperVisionLayout, planReadyToSendRequestsLayout, planSentLayout;
    CardView planReadyToSendCounterLayout, planPendingToSupperVisionCounterLayout;
    TextView goOnline, update, planReadyToSendCounter, planPendingToSupperVisionCounter, username, phone;
    RelativeLayout offlineLayout, updateLayout;
    DrawerLayout drawerLayout;
    FrameLayout navigationView;
    androidx.appcompat.widget.Toolbar toolbar;
    ImageView close, profilePicture;
    RelativeLayout pendingToSupperVisionManagementLayout, transferPlansManagementLayout, planInquiryLayout, settingsLayout, signOutLayout;

    int supperVisionCounter = 0, readyCounter = 0;
    DBHandler dbHandler;
    String userID;
    AppInfoModal appInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        close = findViewById(R.id.close);
        username = findViewById(R.id.username);
        phone = findViewById(R.id.phone);
        offlineLayout = findViewById(R.id.offlineLayout);
        goOnline = findViewById(R.id.goOnline);
        planReadyToSendCounter = findViewById(R.id.planReadyToSendCounter);
        planPendingToSupperVisionCounter = findViewById(R.id.planPendingToSupperVisionCounter);
        planInputRequestsLayout = findViewById(R.id.planInputRequestsLayout);
        planPendingToSupperVisionLayout = findViewById(R.id.planPendingToSupperVisionLayout);
        planReadyToSendRequestsLayout = findViewById(R.id.planReadyToSendRequestsLayout);
        planSentLayout = findViewById(R.id.planSentLayout);
        planReadyToSendCounterLayout = findViewById(R.id.planReadyToSendCounterLayout);
        planPendingToSupperVisionCounterLayout = findViewById(R.id.planPendingToSupperVisionCounterLayout);
        profilePicture = findViewById(R.id.profilePicture);
        pendingToSupperVisionManagementLayout = findViewById(R.id.pendingToSupperVisionManagementLayout);
        transferPlansManagementLayout = findViewById(R.id.transferPlansManagementLayout);
        planInquiryLayout = findViewById(R.id.planInquiryLayout);
        settingsLayout = findViewById(R.id.settingsLayout);
        signOutLayout = findViewById(R.id.signOutLayout);
        updateLayout = findViewById(R.id.updateLayout);
        update = findViewById(R.id.update);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        navigationView.bringToFront();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        close.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));

        dbHandler = new DBHandler(HomeActivity.this);
        UserModal userModal = dbHandler.getUser();
        userID = userModal.getId();
        username.setText(String.format("%s %s", userModal.getFirstName(), userModal.getLastName()));
        phone.setText(userModal.getPhone());
        if (userModal.getPic() != null) {
            byte[] decodedString = Base64.decode(userModal.getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePicture.setImageBitmap(decodedByte);
        }

        appInfo = dbHandler.getAppInfo();

        ArrayList<String> appFeatures = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(appInfo.getAppNewFeatures());
        } catch (JSONException ignored) {

        }
        for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
            try {
                appFeatures.add(jsonArray.get(i).toString());
            } catch (JSONException ignored) {
            }
        }

        if (appInfo.getAppFirstRun().equals("1")) {
            Dialog dialog = new Dialog(this);
            dbHandler.updateAppFirstRun();

            dialog.setContentView(R.layout.custom_app_new_update_features);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            Button confirm = dialog.findViewById(R.id.confirm);
            RecyclerView appNewFeaturesRecyclerView = dialog.findViewById(R.id.appNewFeaturesRecyclerView);

            AppNewFeaturesRecyclerViewAdapter adapter = new AppNewFeaturesRecyclerViewAdapter(appFeatures);

            appNewFeaturesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            appNewFeaturesRecyclerView.setAdapter(adapter);

            confirm.setOnClickListener(v -> dialog.dismiss());

            dialog.show();
        }

        planInputRequestsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InputRequestsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        planPendingToSupperVisionLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PendingToSuperVisionActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        planReadyToSendRequestsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReadyToSendRequestsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        planSentLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SentActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        goOnline.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        update.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(appInfo.getAppUpdateURL()));
            startActivity(i);
        });

        profilePicture.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileDetailsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        pendingToSupperVisionManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ReloadManagementActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        transferPlansManagementLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TransferPlansManagementActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        planInquiryLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, InquiryActivity.class);
            intent.putExtra("natCode", "");
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        settingsLayout.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
//            dbHandler.changeBackToReadyToSend();
        });

        signOutLayout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_home_confirmation_sign_out_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);

            confirm.setOnClickListener(v1 -> {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                dialog.dismiss();
            });

            cancel.setOnClickListener(v12 -> dialog.dismiss());

            dialog.show();
        });

        close.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));

        dbHandler.close();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else
                    finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        UserModal userModal = dbHandler.getUser();
//        username.setText(String.format(userModal.getFirstName() + " " + userModal.getLastName() + " "));
        supperVisionCounter = dbHandler.readPlans(userID, 0).size();
        readyCounter = dbHandler.readPlans(userID, 1).size();

        if (userModal.getLoginMode() == 1) {
            offlineLayout.setVisibility(View.GONE);
            if (Integer.parseInt(appInfo.getAppLastVersion()) > BuildConfig.VERSION_CODE) {
                updateLayout.setVisibility(View.VISIBLE);
            }
        } else {
            offlineLayout.setVisibility(View.VISIBLE);
        }

        if (supperVisionCounter == 0)
            planPendingToSupperVisionCounterLayout.setVisibility(View.GONE);
        else if (supperVisionCounter > 0 && supperVisionCounter <= 99) {
            planPendingToSupperVisionCounterLayout.setVisibility(View.VISIBLE);
            planPendingToSupperVisionCounter.setText(String.valueOf(supperVisionCounter));
        } else if (supperVisionCounter > 99) {
            planPendingToSupperVisionCounterLayout.setVisibility(View.VISIBLE);
            planPendingToSupperVisionCounter.setText("99+");
        }

        if (readyCounter == 0)
            planReadyToSendCounterLayout.setVisibility(View.GONE);
        else if (readyCounter > 0 && readyCounter <= 99) {
            planReadyToSendCounterLayout.setVisibility(View.VISIBLE);
            planReadyToSendCounter.setText(String.valueOf(readyCounter));
        } else if (readyCounter > 99) {
            planReadyToSendCounterLayout.setVisibility(View.VISIBLE);
            planReadyToSendCounter.setText("99+");
        }
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (drawerLayout.isDrawerOpen(GravityCompat.START))
//            drawerLayout.closeDrawer(GravityCompat.START);
//        else
//            finish();
//    }

    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(HomeActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}