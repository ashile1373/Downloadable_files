package ir.ashilethegreat.payesh.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import ir.ashilethegreat.payesh.BuildConfig;
import ir.ashilethegreat.payesh.DBhandlers.AppInfoModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

@SuppressLint("CustomSplashScreen")
public class DarkSplashActivity extends AppCompatActivity {
    DBHandler dbHandler;
    TextView appVersion;
    private final int DEFAULT_TIMEOUT = 20000;
    String lastVersion, essentialVersion, updateURL, message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dark_splash);

        appVersion = findViewById(R.id.appVersion);

        dbHandler = new DBHandler(this);
        AppInfoModal appInfoModal = dbHandler.getAppInfo();

        appVersion.setText(BuildConfig.VERSION_NAME);

        if (dbHandler.isProvincesTableEmpty()) {
            dbHandler.insertAppInfo(BuildConfig.VERSION_CODE);
            dbHandler.insertProvinces();
            dbHandler.insertCities();
            dbHandler.insertSections();
            dbHandler.insertEducations();
            dbHandler.insertFieldOfStudiesPartOne();
            dbHandler.insertFieldOfStudiesPartTwo();
            dbHandler.insertFieldOfStudiesPartThree();
            dbHandler.insertFieldOfStudiesPartFour();
            dbHandler.insertFieldOfStudiesPartFive();
            dbHandler.insertFieldOfStudiesPartSix();
            dbHandler.insertFieldOfStudiesPartSeven();
            dbHandler.insertFieldOfStudiesPartEight();
            dbHandler.insertFieldOfStudiesPartNine();
            dbHandler.insertSpecialization();
            dbHandler.insertHouseholds();
            dbHandler.insertPlanTitlesPartOne();
            dbHandler.insertPlanTitlesPartTwo();
            dbHandler.insertPlanTitlesPartThree();
            dbHandler.insertPlanTitlesPartFour();
            dbHandler.insertPlanTitlesPartFive();
            dbHandler.insertPlanTitlesPartSix();
            dbHandler.insertPlanTitlesPartSeven();
            dbHandler.insertPlanTitlesPartEight();
            dbHandler.insertPlanTitlesPartNine();
            dbHandler.insertPlanTitlesPartTen();
            dbHandler.insertPlanTitlesPartEleven();
            dbHandler.insertPlanTitlesPartTwelve();
            dbHandler.insertPlanTitlesPartThirteen();
            dbHandler.insertPlanTitlesPartFourteen();
            dbHandler.insertPlanTitlesPartFifteen();
            dbHandler.insertPlanTitlesPartSixteen();
            dbHandler.insertPlanTitlesPartSeventeen();
            dbHandler.insertPlanTitlesPartEighteen();
            dbHandler.insertPlanTitlesPartNineteen();
            dbHandler.insertPlanTitlesPartTwenty();
            dbHandler.insertPlanTitlesPartTwentyOne();
            dbHandler.insertPlanTitlesPartTwentyTwo();
            dbHandler.insertPlanTitlesPartTwentyThree();
            dbHandler.insertPlanTitlesPartTwentyFour();
            dbHandler.insertPlanTitlesPartTwentyFive();
            dbHandler.insertPlanTitlesPartTwentySix();
            dbHandler.insertPlanSituations();
            dbHandler.insertPlanEconomicSections();
            dbHandler.insertPlanRegionLocations();
            dbHandler.insertPlanBorderLocations();
            dbHandler.insertOrgans();
            dbHandler.insertBanks();
            dbHandler.insertDateFacilities();
            dbHandler.insertSupplyFacilities();
            dbHandler.insertLicenceTypes();
            dbHandler.updateAppNewFeatures();
            dbHandler.setAppInfoSettings();
            dbHandler.insertRejectedTypes();
            dbHandler.insertSuperVisionTypes();
            dbHandler.insertPlanLicensesExist();
            dbHandler.insertReceivedFacilitySituationTypes();
            dbHandler.insertMarketingLevels();
            dbHandler.insertPlanTypes();
            dbHandler.insertPlanClosureReasons();
            dbHandler.insertPlanBails();

            final Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.postDelayed(() -> {
                Intent i = new Intent(DarkSplashActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }, 6000);
        }
        else {
            UserModal user = dbHandler.getUser();
            if (user.getUsername() != null && user.getPassword() != null)
                if (!user.getUsername().isEmpty() && !user.getPassword().isEmpty()) {

                    String usernameString = user.getUsername();
                    String passwordString = user.getPassword();

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
                                        JSONObject essentialVersionString = new JSONObject(response.getString("version_essential"));
                                        JSONObject lastVersionString = new JSONObject(response.getString("version_last"));

                                        lastVersion = lastVersionString.getString("version");
                                        essentialVersion = essentialVersionString.getString("version");
                                        updateURL = lastVersionString.getString("url");
                                        message = lastVersionString.getString("message");

                                        appInfoModal.setAppLastVersion(lastVersion);
                                        appInfoModal.setAppEssentialVersion(essentialVersion);
                                        appInfoModal.setAppUpdateURL(updateURL);
                                        appInfoModal.setAppNewFeatures(message);

                                        dbHandler.updateAppInfo(appInfoModal);
                                    }

                                } catch (JSONException ignored) {
                                }
                            },
                            error -> {

                            }) {
                    };

                    requestQueue.add(jsonRequest);
                    jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DEFAULT_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                }

            final Handler handler2 = new Handler(Looper.getMainLooper());
            handler2.postDelayed(() -> {
                Intent i = new Intent(DarkSplashActivity.this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(0, 0);
                finish();
            }, 3000);
        }

        if (appInfoModal.getAppTheme() != null) {
            switch (appInfoModal.getAppTheme()) {
                case "0" -> //automatic theme
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                case "1" -> //theme light
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                case "2" -> //theme dark
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }

    }

}