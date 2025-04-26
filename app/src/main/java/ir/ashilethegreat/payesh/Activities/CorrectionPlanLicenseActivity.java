package ir.ashilethegreat.payesh.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.carto.styles.LineStyle;
import com.carto.styles.LineStyleBuilder;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.neshan.common.model.LatLng;
import org.neshan.mapsdk.MapView;
import org.neshan.mapsdk.model.Circle;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.ConfirmPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class CorrectionPlanLicenseActivity extends AppCompatActivity implements LocationListener {

    // used to track request permissions
    private final int REQUEST_CODE = 123;
    private final int REQUEST_CHECK_SETTINGS = 111;
    private final int REQUEST_PLAN_CAMERA_PERMISSION = 110;
    private final int REQUEST_LICENSE_CAMERA_PERMISSION = 109;
    private final int REQUEST_OWNERSHIP_CAMERA_PERMISSION = 108;
    // location updates interval - 1 sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    // fastest updates interval - 1 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;

    // User's current location
    private Location userLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationRequest locationRequest;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationCallback locationCallback;
    private String lastUpdateTime;
    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    LocationManager locationManager;
    Criteria criteria;
    Circle circle;
    boolean mapLoadingFlag = true;
    public String bestProvider, planLatitudeString = "", planLongitudeString = "";
    MapView map;
    RelativeLayout mapDragCanceler, mapLoading;
    LottieAnimationView planImageAnimationView, planLicenseAnimationView, planOwnershipAnimationView;
    FloatingActionButton myLocation;
    ExtendedFloatingActionButton editMyLocation;
    ImageView planImage, planLicenseImage, planOwnershipImage, planImageClose, planImageEditing, planLicenseImageClose,
            planOwnershipImageClose, mapMarker, planLicenseImageEditing, planOwnershipImageEditing;
    String planPhotoToBase64, licensePhotoToBase64, ownershipPhotoToBase64 = "";

    public String planRegionLocationString, planBorderLocationString, provinceString, townshipString, sectionString, cityString, regionString,
            mainStreetString, subStreetString, alleyString, codeString, postalCodeString = "", moreAddressString;
    int /*flag*/ addressUpdated = 0;
    boolean addressEditing = false;
    ExtendedFloatingActionButton enterAddress;
    CardView addressCardView;
    LottieAnimationView noAddressAnimationView;
    TextView thirdPartAddress, forthPartAddress, fifthPartAddress, firstPartAddress, secondPartAddress, noAddressFound;
    NestedScrollView nestedScrollView;

    ArrayAdapter<String> adapterOfOptions;
    LinearLayout back;
    TextView cancelRequest, licenseText;
    Button nextStep;
    AutoCompleteTextView planLicenceExist;
    EditText planEmployeesCount;
    TextInputLayout planLocationErrorLayout, planImageForErrorLayout, planLicenseImageForErrorLayout, planLicenceExistLayout, planEmployeesCountLayout;
    LinearLayout licenseImageLayout, ownershipImageLayout;


    DBHandler dbHandler;
    String planId;
    int whichStep = 3;
    ConfirmPlanModal correctionSelectedPlan;
    PlanModal selectedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction_plan_license);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_yellow));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nextStep = findViewById(R.id.nextStep);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        planLocationErrorLayout = findViewById(R.id.planLocationErrorLayout);
        map = findViewById(R.id.mapview);
        mapMarker = findViewById(R.id.mapMarker);
        mapDragCanceler = findViewById(R.id.mapDragCanceler);
        mapLoading = findViewById(R.id.mapLoading);
        editMyLocation = findViewById(R.id.editMyLocation);
        myLocation = findViewById(R.id.myLocation);
        addressCardView = findViewById(R.id.addressCardView);
        noAddressAnimationView = findViewById(R.id.noAddressAnimationView);
        noAddressFound = findViewById(R.id.noAddressFoundTextView);
        firstPartAddress = findViewById(R.id.regionLocation);
        secondPartAddress = findViewById(R.id.borderLocation);
        thirdPartAddress = findViewById(R.id.city);
        forthPartAddress = findViewById(R.id.street);
        fifthPartAddress = findViewById(R.id.postalCode);
        enterAddress = findViewById(R.id.enterAddress);
        planLicenceExist = findViewById(R.id.licenceExist);
        planLicenceExistLayout = findViewById(R.id.licenceExistLayout);
        planImage = findViewById(R.id.planImageFirst);
        planImageAnimationView = findViewById(R.id.planImageAnimationView);
        planOwnershipAnimationView = findViewById(R.id.planOwnershipAnimationView);
        planLicenseAnimationView = findViewById(R.id.planLicenseAnimationView);
        planLicenseImage = findViewById(R.id.planLicenseImage);
        planOwnershipImage = findViewById(R.id.planOwnershipImage);
        planImageClose = findViewById(R.id.planImageClose);
        planImageEditing = findViewById(R.id.planImageEditing);
        planLicenseImageEditing = findViewById(R.id.planLicenseImageEditing);
        planOwnershipImageEditing = findViewById(R.id.planOwnershipImageEditing);
        planLicenseImageClose = findViewById(R.id.planLicenseImageClose);
        planOwnershipImageClose = findViewById(R.id.planOwnershipImageClose);
        planImageForErrorLayout = findViewById(R.id.planImageForErrorLayout);
        planLicenseImageForErrorLayout = findViewById(R.id.planLicenseImageForErrorLayout);
        planEmployeesCount = findViewById(R.id.planEmployeesCount);
        planEmployeesCountLayout = findViewById(R.id.planEmployeesCountLayout);
        licenseImageLayout = findViewById(R.id.licenseImageLayout);
        ownershipImageLayout = findViewById(R.id.ownershipImageLayout);
        licenseText = findViewById(R.id.licenseText);

        dbHandler = new DBHandler(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        selectedPlan = dbHandler.planSelectRow(planId);
        correctionSelectedPlan = dbHandler.confirmPlanSelectRow(planId, selectedPlan.getUserID());

        if (!correctionSelectedPlan.getConfirmPlanLicenceExist().equals(""))
            planLicenceExist.setText(dbHandler.readPlanLicensesExistNameFromID(correctionSelectedPlan.getConfirmPlanLicenceExist()));
        planEmployeesCount.setText(correctionSelectedPlan.getConfirmPlanEmployeeCount());
        map.getSettings().setNeshanLogoMargins(-100, -100);
        if (!correctionSelectedPlan.getConfirmPlanLatitude().equals("") && !correctionSelectedPlan.getConfirmPlanLongitude().equals("")) {
            LatLng latLng = new LatLng(Double.parseDouble(correctionSelectedPlan.getConfirmPlanLatitude()),
                    Double.parseDouble(correctionSelectedPlan.getConfirmPlanLongitude()));
//            myLocation.hide();
            map.moveCamera(latLng, 0.25f);
            map.setZoom(16f, 0.25f);
            mapDragCanceler.setVisibility(View.VISIBLE);

            planLongitudeString = correctionSelectedPlan.getConfirmPlanLongitude();
            planLatitudeString = correctionSelectedPlan.getConfirmPlanLatitude();
            myLocation.setVisibility(View.INVISIBLE);
            editMyLocation.setVisibility(View.VISIBLE);
        }

        if (dbHandler.readPlanLicensesExistID(planLicenceExist.getText().toString()).equals("1")) {
            licenseText.setVisibility(View.VISIBLE);
            planLicenseImageForErrorLayout.setVisibility(View.VISIBLE);
            licenseImageLayout.setVisibility(View.VISIBLE);

            if (!correctionSelectedPlan.getConfirmPlanLicenceImage().equals("")) {
                licensePhotoToBase64 = correctionSelectedPlan.getConfirmPlanLicenceImage();
                byte[] decodedString = Base64.decode(correctionSelectedPlan.getConfirmPlanLicenceImage(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                planLicenseImage.setVisibility(View.VISIBLE);
                planLicenseAnimationView.setVisibility(View.INVISIBLE);
                planLicenseImageClose.setVisibility(View.INVISIBLE);
                planLicenseImageEditing.setVisibility(View.VISIBLE);
                planImageForErrorLayout.setError(null);
                Glide.with(this)
                        .load(decodedByte)
                        .placeholder(R.drawable.circular_pgbar)
                        .into(planLicenseImage);
            }
        }

        if (!correctionSelectedPlan.getConfirmPlanImage().equals("")) {
            planPhotoToBase64 = correctionSelectedPlan.getConfirmPlanImage();
            byte[] decodedString = Base64.decode(planPhotoToBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            planImage.setVisibility(View.VISIBLE);
            planImageAnimationView.setVisibility(View.INVISIBLE);
//            secondImageCardView.setVisibility(View.VISIBLE);
//            planAnimationViewSecond.setVisibility(View.VISIBLE);
            planImageClose.setVisibility(View.INVISIBLE);
            planImageEditing.setVisibility(View.VISIBLE);
            planImageForErrorLayout.setError(null);
            Glide.with(this)
                    .load(decodedByte)
                    .placeholder(R.drawable.circular_pgbar)
                    .into(planImage);
        }

        if (!correctionSelectedPlan.getConfirmPlanOwnershipImage().equals("")) {
            ownershipPhotoToBase64 = correctionSelectedPlan.getConfirmPlanOwnershipImage();
            byte[] decodedString = Base64.decode(ownershipPhotoToBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            planOwnershipImage.setVisibility(View.VISIBLE);
            planOwnershipAnimationView.setVisibility(View.INVISIBLE);
//            secondImageCardView.setVisibility(View.VISIBLE);
//            planAnimationViewSecond.setVisibility(View.VISIBLE);
            planOwnershipImageClose.setVisibility(View.INVISIBLE);
            planOwnershipImageEditing.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(decodedByte)
                    .placeholder(R.drawable.circular_pgbar)
                    .into(planOwnershipImage);
        }
        if (!correctionSelectedPlan.getConfirmPlanRegionLocation().isEmpty())
            planRegionLocationString = correctionSelectedPlan.getConfirmPlanRegionLocation();
        if (!correctionSelectedPlan.getConfirmPlanBorderLocation().isEmpty())
            planBorderLocationString = correctionSelectedPlan.getConfirmPlanBorderLocation();
        provinceString = selectedPlan.getProvince();
        townshipString = selectedPlan.getTownship();
        if (!correctionSelectedPlan.getConfirmSection().isEmpty())
            sectionString = correctionSelectedPlan.getConfirmSection();
        else {
            if (!selectedPlan.getSection().isEmpty())
                sectionString = selectedPlan.getSection();
        }
        if (!correctionSelectedPlan.getConfirmCity().isEmpty())
            cityString = correctionSelectedPlan.getConfirmCity();
        else {
            if (!selectedPlan.getCity().isEmpty())
                cityString = selectedPlan.getCity();
        }
        if (!correctionSelectedPlan.getConfirmRegion().isEmpty())
            regionString = correctionSelectedPlan.getConfirmRegion();
        else {
            if (!selectedPlan.getRegion().isEmpty())
                regionString = selectedPlan.getRegion();
        }
        if (!correctionSelectedPlan.getConfirmMainStreet().isEmpty())
            mainStreetString = correctionSelectedPlan.getConfirmMainStreet();
        else {
            if (!selectedPlan.getMainStreet().isEmpty())
                mainStreetString = selectedPlan.getMainStreet();
        }
        if (!correctionSelectedPlan.getConfirmSubStreet().isEmpty())
            subStreetString = correctionSelectedPlan.getConfirmSubStreet();
        else {
            if (!selectedPlan.getSubStreet().isEmpty())
                subStreetString = selectedPlan.getSubStreet();
        }
        if (!correctionSelectedPlan.getConfirmAlley().isEmpty())
            alleyString = correctionSelectedPlan.getConfirmAlley();
        else {
            if (!selectedPlan.getAlley().isEmpty())
                alleyString = selectedPlan.getAlley();
        }
        if (!correctionSelectedPlan.getConfirmCode().isEmpty())
            codeString = correctionSelectedPlan.getConfirmCode();
        else {
            if (!selectedPlan.getCode().isEmpty())
                codeString = selectedPlan.getCode();
        }
        if (!correctionSelectedPlan.getConfirmPostalCode().isEmpty())
            postalCodeString = correctionSelectedPlan.getConfirmPostalCode();
        else {
            if (!selectedPlan.getPostalCode().isEmpty())
                postalCodeString = selectedPlan.getPostalCode();
        }
        if (!correctionSelectedPlan.getConfirmMoreAddress().isEmpty())
            moreAddressString = correctionSelectedPlan.getConfirmMoreAddress();
        else {
            if (!selectedPlan.getMoreAddress().isEmpty())
                moreAddressString = selectedPlan.getMoreAddress();
        }
        addressUpdated = selectedPlan.getAddressUpdated();

        if (selectedPlan.getAddressUpdated() == 1) {
            addressCardView.setVisibility(View.VISIBLE);
            noAddressFound.setVisibility(View.GONE);
            noAddressAnimationView.setVisibility(View.GONE);
            enterAddress.setText("ویرایش نشانی");

            String firstPartAddressString = dbHandler.readPlanRegionLocationNameFromID(planRegionLocationString);
            String secondPartAddressString = dbHandler.readPlanBorderLocationNameFromID(planBorderLocationString);
            String thirdPartAddressString = dbHandler.readProvinceNameFromID(provinceString) + "،" + dbHandler.readCityNameFromID(townshipString) + "،" +
                    dbHandler.readSectionNameFromID(sectionString) + "،" + cityString + "،" + regionString;
            String forthPartAddressString = mainStreetString + "،" + subStreetString + "،" + alleyString + "، پلاک " +
                    codeString;
            String fifthParthAddressString = postalCodeString;

            if (postalCodeString.equals(""))
                fifthParthAddressString = "تعریف نشده";

            firstPartAddress.setText(firstPartAddressString);
            secondPartAddress.setText(secondPartAddressString);
            thirdPartAddress.setText(thirdPartAddressString);
            forthPartAddress.setText(forthPartAddressString);
            fifthPartAddress.setText(fifthParthAddressString);
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanLicensesExist());
        planLicenceExist.setAdapter(adapterOfOptions);

        planEmployeesCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planEmployeesCountLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        planLicenceExist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planLicenceExistLayout.setError(null);
                if (!planLicenceExist.getText().toString().isEmpty()) {
                    if (dbHandler.readPlanLicensesExistID(planLicenceExist.getText().toString()).equals("1")) {
                        licenseText.setVisibility(View.VISIBLE);
                        planLicenseImageForErrorLayout.setVisibility(View.VISIBLE);
                        licenseImageLayout.setVisibility(View.VISIBLE);
                        planLicenseAnimationView.setVisibility(View.VISIBLE);
                        planLicenseImageClose.setVisibility(View.INVISIBLE);

                    } else {
                        licenseText.setVisibility(View.GONE);
                        planLicenseImageForErrorLayout.setVisibility(View.GONE);
                        licenseImageLayout.setVisibility(View.GONE);
                        planLicenseAnimationView.setVisibility(View.GONE);
                        licensePhotoToBase64 = "";
                        if (planLicenseAnimationView.getVisibility() != View.VISIBLE) {
                            if (!correctionSelectedPlan.getConfirmPlanLicenceImage().isEmpty() || planLicenseImageClose.getVisibility() == View.VISIBLE)
                                planLicenseImageEditing.performClick();
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        myLocation.setOnClickListener(v -> focusOnUserLocation());

        mapDragCanceler.setOnClickListener(v -> {
        });

        mapLoading.setOnClickListener(v -> {
        });


        editMyLocation.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_plan_details_confirmation_location_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);
            ExtendedFloatingActionButton cancel = dialog.findViewById(R.id.cancel);

            cancel.setOnClickListener(v14 -> dialog.dismiss());

            confirm.setOnClickListener(v15 -> {
                editMyLocation.setVisibility(View.GONE);
                myLocation.setVisibility(View.VISIBLE);
//                locationUpdated = 0;
//                dbHandler.updateLocationUpdated(planId, locationUpdated);
                planLongitudeString = "";
                planLatitudeString = "";
                dialog.dismiss();
                mapDragCanceler.setVisibility(View.GONE);
            });

            dialog.show();
        });

        enterAddress.setOnClickListener(v -> createAddressDialog());

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        });

        planImageAnimationView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2 &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Dialog dialog = new Dialog(CorrectionPlanLicenseActivity.this);

                dialog.setContentView(R.layout.custom_camera_permission_dialog_layout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);

                confirm.setOnClickListener(v15 -> {
                    dialog.dismiss();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PLAN_CAMERA_PERMISSION);
                });
                dialog.show();

                return;
            }
            ImagePicker.Companion.with(this)
                    .cameraOnly()
                    .maxResultSize(750, 1000)
                    .compress(1024)
                    .crop()
                    .start(REQUEST_PLAN_CAMERA_PERMISSION);
        });

        planImageClose.setOnClickListener(v -> {
            Glide.with(getApplicationContext()).clear(planImage);
            planImageAnimationView.setVisibility(View.VISIBLE);
            planImage.setVisibility(View.INVISIBLE);
            planImageClose.setVisibility(View.INVISIBLE);
        });

        planImageEditing.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_plan_details_confirmation_picture_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);
            ExtendedFloatingActionButton cancel = dialog.findViewById(R.id.cancel);

            confirm.setOnClickListener(v1 -> {
                Glide.with(getApplicationContext()).clear(planImage);
                correctionSelectedPlan.setConfirmPlanImage("");
                dbHandler.updateConfirmPlanFinalSupervisionSituation(correctionSelectedPlan);
                planImageAnimationView.setVisibility(View.VISIBLE);
                planImage.setVisibility(View.INVISIBLE);
                planImageEditing.setVisibility(View.INVISIBLE);
                dialog.dismiss();
            });

            cancel.setOnClickListener(v1 -> dialog.dismiss());

            dialog.show();
        });

        planLicenseAnimationView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2 &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Dialog dialog = new Dialog(CorrectionPlanLicenseActivity.this);

                dialog.setContentView(R.layout.custom_camera_permission_dialog_layout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);

                confirm.setOnClickListener(v15 -> {
                    dialog.dismiss();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_LICENSE_CAMERA_PERMISSION);
                });
                dialog.show();
                return;
            }
            ImagePicker.Companion.with(this)
                    .cameraOnly()
                    .maxResultSize(750, 1000)
                    .compress(1024)
                    .crop()
                    .start(REQUEST_LICENSE_CAMERA_PERMISSION);
        });

        planLicenseImageClose.setOnClickListener(v -> {
            Glide.with(getApplicationContext()).clear(planLicenseImage);
            planLicenseAnimationView.setVisibility(View.VISIBLE);
            planLicenseImage.setVisibility(View.INVISIBLE);
            planLicenseImageClose.setVisibility(View.INVISIBLE);
        });

        planLicenseImageEditing.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_plan_details_confirmation_picture_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);
            ExtendedFloatingActionButton cancel = dialog.findViewById(R.id.cancel);

            confirm.setOnClickListener(v1 -> {
                Glide.with(getApplicationContext()).clear(planLicenseImage);
                correctionSelectedPlan.setConfirmPlanLicenceImage("");
                dbHandler.updateConfirmPlanFinalSupervisionSituation(correctionSelectedPlan);
                planLicenseAnimationView.setVisibility(View.VISIBLE);
                planLicenseImageClose.setVisibility(View.INVISIBLE);
                planLicenseImage.setVisibility(View.INVISIBLE);
                planLicenseImageEditing.setVisibility(View.INVISIBLE);
                licensePhotoToBase64 = "";
                dialog.dismiss();
            });

            cancel.setOnClickListener(v1 -> {
                planLicenceExist.setText(dbHandler.readPlanLicensesExistNameFromID("1"));
                adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanLicensesExist());
                planLicenceExist.setAdapter(adapterOfOptions);
                dialog.dismiss();
            });

            dialog.show();
        });

        planOwnershipAnimationView.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2 &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Dialog dialog = new Dialog(CorrectionPlanLicenseActivity.this);

                dialog.setContentView(R.layout.custom_camera_permission_dialog_layout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);

                confirm.setOnClickListener(v15 -> {
                    dialog.dismiss();
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_OWNERSHIP_CAMERA_PERMISSION);
                });
                dialog.show();

                return;
            }
            ImagePicker.Companion.with(this)
                    .cameraOnly()
                    .maxResultSize(750, 1000)
                    .compress(1024)
                    .crop()
                    .start(REQUEST_OWNERSHIP_CAMERA_PERMISSION);
        });

        planOwnershipImageClose.setOnClickListener(v -> {
            Glide.with(getApplicationContext()).clear(planOwnershipImage);
            planOwnershipAnimationView.setVisibility(View.VISIBLE);
            planOwnershipImage.setVisibility(View.INVISIBLE);
            planOwnershipImageClose.setVisibility(View.INVISIBLE);
        });

        planOwnershipImageEditing.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_plan_details_confirmation_picture_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);

            ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);
            ExtendedFloatingActionButton cancel = dialog.findViewById(R.id.cancel);

            confirm.setOnClickListener(v1 -> {
                Glide.with(getApplicationContext()).clear(planOwnershipImage);
                correctionSelectedPlan.setConfirmPlanOwnershipImage("");
                dbHandler.updateConfirmPlanFinalSupervisionSituation(correctionSelectedPlan);
                planOwnershipAnimationView.setVisibility(View.VISIBLE);
                planOwnershipImageClose.setVisibility(View.INVISIBLE);
                planOwnershipImage.setVisibility(View.INVISIBLE);
                planOwnershipImageEditing.setVisibility(View.INVISIBLE);
                ownershipPhotoToBase64 = "";
                dialog.dismiss();
            });

            cancel.setOnClickListener(v1 -> {
                planLicenceExist.setText(dbHandler.readPlanLicensesExistNameFromID("1"));
                adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanLicensesExist());
                planLicenceExist.setAdapter(adapterOfOptions);
                dialog.dismiss();
            });

            dialog.show();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");
            else {
                correctionSelectedPlan.setConfirmPlanLicenceExist(dbHandler.readPlanLicensesExistID(planLicenceExist.getText().toString()));
                correctionSelectedPlan.setConfirmPlanEmployeeCount(planEmployeesCount.getText().toString());
                correctionSelectedPlan.setConfirmPlanLatitude(String.valueOf(map.getCameraTargetPosition().getLatitude()));
                correctionSelectedPlan.setConfirmPlanLongitude(String.valueOf(map.getCameraTargetPosition().getLongitude()));
                correctionSelectedPlan.setConfirmPlanRegionLocation(planRegionLocationString);
                correctionSelectedPlan.setConfirmPlanBorderLocation(planBorderLocationString);
                correctionSelectedPlan.setConfirmSection(sectionString);
                correctionSelectedPlan.setConfirmCity(cityString);
                correctionSelectedPlan.setConfirmRegion(regionString);
                correctionSelectedPlan.setConfirmMainStreet(mainStreetString);
                correctionSelectedPlan.setConfirmSubStreet(subStreetString);
                correctionSelectedPlan.setConfirmAlley(alleyString);
                correctionSelectedPlan.setConfirmCode(codeString);
                correctionSelectedPlan.setConfirmPostalCode(postalCodeString);
                correctionSelectedPlan.setConfirmMoreAddress(moreAddressString);
                correctionSelectedPlan.setConfirmPlanImage(planPhotoToBase64);

                if (licenseImageLayout.getVisibility() != View.VISIBLE)
                    correctionSelectedPlan.setConfirmPlanLicenceImage("");
                else correctionSelectedPlan.setConfirmPlanLicenceImage(licensePhotoToBase64);

                if (ownershipImageLayout.getVisibility() != View.VISIBLE)
                    correctionSelectedPlan.setConfirmPlanOwnershipImage("");
                else correctionSelectedPlan.setConfirmPlanOwnershipImage(ownershipPhotoToBase64);

                selectedPlan.setAddressUpdated(addressUpdated);

                Bundle extras = getIntent().getExtras();
                String planId = extras.getString("planId");

                dbHandler.updateConfirmPlanFinalSupervisionSituation(correctionSelectedPlan);

                selectedPlan.setWhichStep(whichStep);
                selectedPlan.setStatusRegistration(isNetworkAvailable());
                dbHandler.updatePlanInfo(selectedPlan);

                Intent intent = new Intent(this, CorrectionPlanFacilityActivity.class);
                intent.putExtra("planId", planId);
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
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, CorrectionPlanEcosystemRequirementActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (planEmployeesCount.getText().toString().isEmpty()) {
            planEmployeesCountLayout.setError("تعداد شاغلین را وارد نمایید!");
            if (!planEmployeesCount.getText().toString().isEmpty()) {
                if (Double.parseDouble(planEmployeesCount.getText().toString()) <= 0) {
                    planEmployeesCountLayout.setError("تعداد شاغلین را به درستی وارد نمایید!");
                }
            }
            nestedScrollView.smoothScrollTo(0, planEmployeesCountLayout.getTop() - 10);
            retVal = true;
        }
        if (planImageAnimationView.getVisibility() == View.VISIBLE) {
            planImageForErrorLayout.setError("تصویر طرح را عکس برداری نمایید!");
            nestedScrollView.smoothScrollTo(0, planImageForErrorLayout.getTop() - 10);
            retVal = true;
        }
        if (dbHandler.readPlanLicensesExistID(planLicenceExist.getText().toString()).equals("1")) {
            if (planLicenseAnimationView.getVisibility() == View.VISIBLE) {
                planLicenseImageForErrorLayout.setError("تصویر مجوز، پروانه کسب و... را عکس برداری نمایید!");
                nestedScrollView.smoothScrollTo(0, planLicenseImageForErrorLayout.getTop() - 10);
                retVal = true;
            }
        }
        if (planLicenceExist.getText().toString().isEmpty()) {
            planLicenceExistLayout.setError("یک مورد را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, planLicenceExistLayout.getTop() - 10);
            retVal = true;
        }
        if (noAddressFound.getVisibility() == View.VISIBLE) {
            noAddressFound.setTextColor(ContextCompat.getColor(this, R.color.red));
            nestedScrollView.smoothScrollTo(0, noAddressFound.getTop() - 10);
            retVal = true;
        }
        if (!planLatitudeString.equals("") && !planLongitudeString.equals("")) {
            if (!locationDistanceCalculator(map.getCameraTargetPosition(),
                    new LatLng(Double.parseDouble(planLatitudeString), Double.parseDouble(planLongitudeString)))) {
                planLocationErrorLayout.setError("موقعیت مکانی شما نباید بیشتر از 200 متر با نشانگر روی نقشه تفاوت داشته باشد!\nموقعیت مکانی خود را با ضربه روی دکمه به روزرسانی نمایید! ");
                nestedScrollView.smoothScrollTo(0, planLocationErrorLayout.getTop() - 10);
                retVal = true;
            }
        }
        if (planLongitudeString.equals("") || planLatitudeString.equals("")) {
            planLocationErrorLayout.setError("موقعیت مکانی خود را با ضربه روی دکمه مشخص نمایید!");
            nestedScrollView.smoothScrollTo(0, planLocationErrorLayout.getTop() - 10);
            retVal = true;
        }
        return retVal;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // everything related to ui is initialized here
        initLocation();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dialog dialog = new Dialog(CorrectionPlanLicenseActivity.this);

            dialog.setContentView(R.layout.custom_location_permission_dialog_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);

            confirm.setOnClickListener(v15 -> {
                dialog.dismiss();
                startReceivingLocationUpdates();
            });
            dialog.show();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void initLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        settingsClient = LocationServices.getSettingsClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                lastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                stopLocationUpdates();
            }
        };

        mRequestingLocationUpdates = false;
        if (editMyLocation.getVisibility() != View.VISIBLE) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(locationRequest);
            locationSettingsRequest = builder.build();
        }
    }

    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    private void startLocationUpdates() {

        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
                    }


                })
                .addOnFailureListener(this, e -> {

//                    int statusCode = ((ApiException) e).getStatusCode();
//                    if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
//                        if (mRequestingLocationUpdates) {
//                            try {
//                                // Show the dialog by calling startResolutionForResult(), and check the
//                                // result in onActivityResult().
//                                ResolvableApiException rae = (ResolvableApiException) e;
//                                rae.startResolutionForResult(this, REQUEST_CODE);
//                            } catch (IntentSender.SendIntentException sie) {
//                                errorToastMaker("امکان اعمال تنظیمات وجود ندارد!");
//                            }
//                        }
//                    }

//                    onLocationChange();
                });
    }

    public void stopLocationUpdates() {
        // Removing location updates
        fusedLocationClient
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener(this, task -> {
                });
    }


    public void startReceivingLocationUpdates() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library

        Dexter.withContext(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
//                        mRequestingLocationUpdates = true;
//                        startLocationUpdates();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_PLAN_CAMERA_PERMISSION) {
                Uri uri = data.getData();
                planImage.setImageURI(uri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    planPhotoToBase64 = Base64.encodeToString(getBitmapAsByteArray(bitmap), Base64.DEFAULT);
                } catch (IOException e) {
                    errorToastMaker("خطای تصویر");
                }

                planImage.setVisibility(View.VISIBLE);
                planImageAnimationView.setVisibility(View.INVISIBLE);

                planImageClose.setVisibility(View.VISIBLE);
                planImageForErrorLayout.setError(null);
            }
            if (requestCode == REQUEST_LICENSE_CAMERA_PERMISSION) {
                Uri uri = data.getData();
                planLicenseImage.setImageURI(uri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    licensePhotoToBase64 = Base64.encodeToString(getBitmapAsByteArray(bitmap), Base64.DEFAULT);
                } catch (IOException e) {
                    errorToastMaker("خطای تصویر");
                }

                planLicenseImage.setVisibility(View.VISIBLE);
                planLicenseAnimationView.setVisibility(View.INVISIBLE);
                planLicenseImageClose.setVisibility(View.VISIBLE);
                planLicenseImageForErrorLayout.setError(null);
            }
            if (requestCode == REQUEST_OWNERSHIP_CAMERA_PERMISSION) {
                Uri uri = data.getData();
                planOwnershipImage.setImageURI(uri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    ownershipPhotoToBase64 = Base64.encodeToString(getBitmapAsByteArray(bitmap), Base64.DEFAULT);
                } catch (IOException e) {
                    errorToastMaker("خطای تصویر");
                }

                planOwnershipImage.setVisibility(View.VISIBLE);
                planOwnershipAnimationView.setVisibility(View.INVISIBLE);
                planOwnershipImageClose.setVisibility(View.VISIBLE);
            }
        }
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                errorToastMaker("بدون استفاده از موقعیت یاب مکانی (GPS)، امکان ادامه ی عملیات وجود ندارد!");
                mRequestingLocationUpdates = false;
            }
        }
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            switch (resultCode) {
                case Activity.RESULT_OK ->
                    // All required changes were successfully made
                        alertToastMaker("اکنون می توانید موقعیت مکانی خود را به روزرسانی کنید.");
                case Activity.RESULT_CANCELED ->
                    // The user was asked to change settings, but chose not to
                        errorToastMaker("بدون استفاده از موقعیت یاب مکانی (GPS)، امکان ادامه ی عملیات وجود ندارد!");
                default -> {
                }
            }
        }

    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void focusOnUserLocation() {
        mapLoadingFlag = true;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
//                    startReceivingLocationUpdates();
            createNoLocationPermissionDialog();
        else {
            /* If permission granted */
            /* start location and it's services */
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                errorToastMaker("موقعیت یاب مکانی (GPS) را روشن کنید!");
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);

                builder.setAlwaysShow(true); //this displays dialog box like Google Maps with two buttons - OK and NO,THANKS

                Task<LocationSettingsResponse> task =
                        LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

                task.addOnCompleteListener(task1 -> {
                    try {
                        LocationSettingsResponse response = task1.getResult(ApiException.class);
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                    } catch (ApiException exception) {
                        switch (exception.getStatusCode()) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                // Location settings are not satisfied. But could be fixed by showing the
                                // user a dialog.
                                try {
                                    // Cast to a resolvable exception.
                                    ResolvableApiException resolvable = (ResolvableApiException) exception;
                                    // Show the dialog by calling startResolutionForResult(),
                                    // and check the result in onActivityResult().
                                    resolvable.startResolutionForResult(
                                            this,
                                            REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException e) {
                                    // Ignore the error.
                                } catch (ClassCastException e) {
                                    // Ignore, should be an impossible error.
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                // Location settings are not satisfied. However, we have no way to fix the
                                // settings so we won't show the dialog.
                                break;
                        }
                    }
                });
            } else {
                /* If GPS is on */
                /* start updating location */
//                initLocation();
//                startReceivingLocationUpdates();
//                startLocationUpdates();

                mapLoading.setVisibility(View.VISIBLE);
                mapMarker.setVisibility(View.INVISIBLE);

                new Handler().postDelayed(() -> {
                    //run this method only when run is true
                    if (mapLoadingFlag) {
                        new Handler().postDelayed(() -> {
                            //run this method only when run is true
                            if (mapLoadingFlag) {

                                mapLoading.setVisibility(View.INVISIBLE);
                                mapMarker.setVisibility(View.VISIBLE);
                                alertToastMaker("در صورت کندی در موقعیت یابی حتماً موقعیت خود را تغییر دهید و دوباره تلاش کنید.");
                            }
                        }, 20000);
                        alertToastMaker("موقعیت یابی بیش از حد انتظار به درازا کشید!\nلطفاً موقعیت خود را تغییر دهید.");
                    }
                }, 50000);

                criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setAltitudeRequired(false);
                criteria.setBearingRequired(false);
                criteria.setCostAllowed(true);
                criteria.setPowerRequirement(Criteria.POWER_HIGH);
                bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true));

                planLocationErrorLayout.setError(null);

                /*if (userLocation != null) {
                    onLocationChanged(userLocation);
                } else*/
                locationManager.requestLocationUpdates(bestProvider, 1000, 0, this);
            }
        }

    }

    private void createNoLocationPermissionDialog() {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_plan_details_open_location_permission_settings);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);
        ExtendedFloatingActionButton cancel = dialog.findViewById(R.id.cancel);

        cancel.setOnClickListener(v14 -> {
            errorToastMaker("بدون استفاده از موقعیت یاب مکانی (GPS)، امکان ادامه ی عملیات وجود ندارد!");
            dialog.dismiss();
        });

        confirm.setOnClickListener(v15 -> {
            openSettings();
            dialog.dismiss();
        });

        dialog.show();

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        locationManager.removeUpdates(this);

        mapLoadingFlag = false;

        userLocation = location;
        planLatitudeString = String.valueOf(userLocation.getLatitude());
        planLongitudeString = String.valueOf(userLocation.getLongitude());
        infoToastMaker("موقعیت مکانی به روزرسانی شد.");

        LatLng latLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        drawCircle(userLocation.getLatitude(), userLocation.getLongitude());

//        locationUpdated = 1;
//        dbHandler.updateLocationUpdated(planId, locationUpdated);
        map.moveCamera(latLng, 0.25f);
        map.setZoom(16f, 0.25f);
        mapLoading.setVisibility(View.INVISIBLE);
        mapMarker.setVisibility(View.VISIBLE);
    }

    public void drawCircle(double latitude, double longitude) {
        if (circle != null)
            map.removeCircle(circle);
        circle = new Circle(new LatLng(latitude, longitude), 200, getLineStyle());
        map.addCircle(circle);
    }

    private LineStyle getLineStyle() {
        LineStyleBuilder lineStCr = new LineStyleBuilder();
        lineStCr.setColor(new com.carto.graphics.Color((short) 234, (short) 169, (short) 47, (short) 255));
        lineStCr.setWidth(1.5f);
        lineStCr.setStretchFactor(0f);
        return lineStCr.buildStyle();
    }

    private void createAddressDialog() {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_enter_address_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        Button confirm = dialog.findViewById(R.id.confirm);
        Button cancel = dialog.findViewById(R.id.cancel);
        TextInputLayout planRegionLocationLayout = dialog.findViewById(R.id.planRegionLocationLayout);
        TextInputLayout planBorderLocationLayout = dialog.findViewById(R.id.planBorderLocationLayout);
        TextInputLayout provinceLayout = dialog.findViewById(R.id.provinceLayout);
        TextInputLayout townshipLayout = dialog.findViewById(R.id.townshipLayout);
        TextInputLayout sectionLayout = dialog.findViewById(R.id.sectionLayout);
        TextInputLayout cityLayout = dialog.findViewById(R.id.cityLayout);
        TextInputLayout regionLayout = dialog.findViewById(R.id.regionLayout);
        TextInputLayout mainStreetLayout = dialog.findViewById(R.id.mainStreetLayout);
        TextInputLayout subStreetLayout = dialog.findViewById(R.id.subStreetLayout);
        TextInputLayout alleyLayout = dialog.findViewById(R.id.alleyLayout);
        TextInputLayout codeLayout = dialog.findViewById(R.id.codeLayout);
        TextInputLayout postalCodeLayout = dialog.findViewById(R.id.postalCodeLayout);
        TextInputLayout moreAddressLayout = dialog.findViewById(R.id.moreAddressLayout);
        AutoCompleteTextView planBorderLocation = dialog.findViewById(R.id.planBorderLocation);
        AutoCompleteTextView planRegionLocation = dialog.findViewById(R.id.planRegionLocation);
        AutoCompleteTextView province = dialog.findViewById(R.id.province);
        AutoCompleteTextView township = dialog.findViewById(R.id.township);
        AutoCompleteTextView section = dialog.findViewById(R.id.section);
        EditText city = dialog.findViewById(R.id.city);
        EditText region = dialog.findViewById(R.id.region);
        EditText mainStreet = dialog.findViewById(R.id.mainStreet);
        EditText subStreet = dialog.findViewById(R.id.subStreet);
        EditText alley = dialog.findViewById(R.id.alley);
        EditText code = dialog.findViewById(R.id.code);
        EditText postalCode = dialog.findViewById(R.id.postalCode);
        EditText moreAddress = dialog.findViewById(R.id.moreAddress);

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readProvincesNames());
        province.setAdapter(adapterOfOptions);

        planRegionLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planRegionLocationLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planBorderLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planBorderLocationLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        province.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                provinceLayout.setError(null);
//                township.setText("");
//                section.setText("");
//                adapterOfOptions = new ArrayAdapter<>(NewRequestPlanDetailsActivity.this, R.layout.spinner_layout,
//                        dbHandler.readCitiesNames(dbHandler.provinceSelectRow(province.getText().toString())));
//                township.setAdapter(adapterOfOptions);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        township.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                townshipLayout.setError(null);
//                section.setText("");
                adapterOfOptions = new ArrayAdapter<>(CorrectionPlanLicenseActivity.this, R.layout.spinner_layout,
                        dbHandler.readSectionsNames(dbHandler.citySelectRow(String.valueOf(dbHandler.provinceSelectRow(province.getText().toString()).getProvinceID()), township.getText().toString())));
                section.setAdapter(adapterOfOptions);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        section.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sectionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        region.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                regionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mainStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mainStreetLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        subStreet.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                subStreetLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        alley.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                alleyLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                codeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        postalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                postalCodeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });
        moreAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                moreAddressLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        planRegionLocation.setText(dbHandler.readPlanRegionLocationNameFromID(planRegionLocationString));
        planBorderLocation.setText(dbHandler.readPlanBorderLocationNameFromID(planBorderLocationString));
        province.setText(dbHandler.readProvinceNameFromID(selectedPlan.getProvince()));
        township.setText(dbHandler.readCityNameFromID(selectedPlan.getTownship()));
        section.setText(dbHandler.readSectionNameFromID(sectionString));

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanRegionLocationNames());
        planRegionLocation.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanBorderLocationNames());
        planBorderLocation.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(CorrectionPlanLicenseActivity.this, R.layout.spinner_layout,
                dbHandler.readSectionsNames(dbHandler.citySelectRow(String.valueOf(dbHandler.provinceSelectRow(province.getText().toString()).getProvinceID()), township.getText().toString())));
        section.setAdapter(adapterOfOptions);

        city.setText(cityString);
        region.setText(regionString);
        mainStreet.setText(mainStreetString);
        subStreet.setText(subStreetString);
        alley.setText(alleyString);
        code.setText(codeString);
        postalCode.setText(postalCodeString);
        moreAddress.setText(moreAddressString);
        addressUpdated = selectedPlan.getAddressUpdated();

        if (selectedPlan.getAddressUpdated() == 1) {

            String firstPartAddressString = dbHandler.readPlanRegionLocationNameFromID(planRegionLocationString);
            String secondPartAddressString = dbHandler.readPlanBorderLocationNameFromID(planBorderLocationString);
            String thirdPartAddressString = dbHandler.readProvinceNameFromID(provinceString) + "،" + dbHandler.readCityNameFromID(townshipString) + "،" +
                    dbHandler.readSectionNameFromID(sectionString) + "،" + cityString + "،" + regionString;
            String forthPartAddressString = mainStreetString + "،" + subStreetString + "،" + alleyString + "، پلاک " +
                    codeString;
            String fifthParthAddressString = postalCodeString;

            if (postalCodeString.equals(""))
                fifthParthAddressString = "تعریف نشده";

            firstPartAddress.setText(firstPartAddressString);
            secondPartAddress.setText(secondPartAddressString);
            thirdPartAddress.setText(thirdPartAddressString);
            forthPartAddress.setText(forthPartAddressString);
            fifthPartAddress.setText(fifthParthAddressString);
        }


        if (!selectedPlan.getProvince().isEmpty()) {
            province.setEnabled(false);
            province.setTextColor(getColor(R.color.dark_gray));
        }
        if (!selectedPlan.getTownship().isEmpty()) {
            township.setEnabled(false);
            township.setTextColor(getColor(R.color.dark_gray));
        }


        confirm.setOnClickListener(v -> {
            if (planRegionLocation.getText().toString().isEmpty())
                planRegionLocationLayout.setError("نوع منطقه را انتخاب کنید!");
            if (planBorderLocation.getText().toString().isEmpty())
                planBorderLocationLayout.setError("موقعیت مرزی را انتخاب کنید!");
            if (province.getText().toString().trim().length() < 2)
                provinceLayout.setError("استان را به درستی انتخاب کنید!");
            if (township.getText().toString().trim().length() < 2)
                townshipLayout.setError("شهرستان را  به درستی انتخاب کنید!");
            if (section.getText().toString().trim().length() < 2)
                sectionLayout.setError("بخش را به درستی وارد کنید!");
            if (city.getText().toString().trim().length() < 2)
                cityLayout.setError("شهر را به درستی وارد کنید!");
            if (region.getText().toString().trim().length() < 2)
                regionLayout.setError("دهستان/منطقه را به درستی وارد کنید!");
            if (mainStreet.getText().toString().trim().length() < 2)
                mainStreetLayout.setError("روستا/خیابان اصلی را به درستی وارد کنید!");
            if (subStreet.getText().toString().trim().length() < 2)
                subStreetLayout.setError("خیابان فرعی را به درستی وارد کنید!");
            if (alley.getText().toString().trim().length() < 2)
                alleyLayout.setError("کوچه را به درستی وارد کنید!");
            if (code.getText().toString().trim().isEmpty())
                codeLayout.setError("پلاک را به درستی وارد کنید!");
            if (!code.getText().toString().trim().isEmpty()) {
                try {
                    if (Double.parseDouble(code.getText().toString()) < 1 || Double.parseDouble(code.getText().toString()) > 100000)
                        codeLayout.setError("محدوده عددی پلاک باید بین 1 تا 100000 باشد!");
                } catch (Exception e) {
                    codeLayout.setError("ورودی پلاک به شکل درست عددی نوشته نشده است!");
                }
            }
            if (!postalCode.getText().toString().trim().isEmpty()) {
                if (postalCode.getText().toString().length() < 10)
                    postalCodeLayout.setError("کدپستی 10 رقم می باشد!");
            }
            if (moreAddress.getText().toString().trim().length() < 2)
                moreAddressLayout.setError("توضیحات آدرس را وارد کنید!");

            if (planRegionLocationLayout.getError() == null && planBorderLocationLayout.getError() == null &&
                    provinceLayout.getError() == null && townshipLayout.getError() == null &&
                    sectionLayout.getError() == null && cityLayout.getError() == null &&
                    regionLayout.getError() == null && mainStreetLayout.getError() == null &&
                    subStreetLayout.getError() == null && alleyLayout.getError() == null &&
                    codeLayout.getError() == null && postalCodeLayout.getError() == null &&
                    moreAddressLayout.getError() == null) {

                planBorderLocationString = dbHandler.readPlanBorderLocationID(planBorderLocation.getText().toString());
                planRegionLocationString = dbHandler.readPlanRegionLocationID(planRegionLocation.getText().toString());
                provinceString = String.valueOf(dbHandler.provinceSelectRow(province.getText().toString()).getProvinceID());
                townshipString = String.valueOf(dbHandler.citySelectRow(provinceString, township.getText().toString()).getCityID());
                sectionString = dbHandler.readSectionsID(townshipString, section.getText().toString());

                cityString = city.getText().toString().trim();
                regionString = region.getText().toString().trim();
                mainStreetString = mainStreet.getText().toString().trim();
                subStreetString = subStreet.getText().toString().trim();
                alleyString = alley.getText().toString().trim();
                codeString = code.getText().toString().trim();
                postalCodeString = postalCode.getText().toString().trim();
                moreAddressString = moreAddress.getText().toString().trim();
                addressUpdated = 1;
                addressEditing = false;

                addressCardView.setVisibility(View.VISIBLE);
                noAddressFound.setVisibility(View.GONE);
                noAddressAnimationView.setVisibility(View.GONE);
                enterAddress.setText("ویرایش محل طرح");

                String firstPartAddressString1 = planRegionLocation.getText().toString();
                String secondPartAddressString1 = planBorderLocation.getText().toString();
                String thirdPartAddressString1 = province.getText().toString() + "،" + township.getText().toString() + "،" + section.getText().toString()
                        + "،" + city.getText().toString() + "،" + region.getText().toString();
                String forthPartAddressString1 = mainStreet.getText().toString() + "،" + subStreet.getText().toString() + "،" + alley.getText().toString()
                        + "، پلاک " + code.getText().toString();
                String fifthPartAddressString1;
                if (!postalCodeString.equals(""))
                    fifthPartAddressString1 = postalCode.getText().toString();
                else
                    fifthPartAddressString1 = "تعریف نشده";
                firstPartAddress.setText(firstPartAddressString1);
                secondPartAddress.setText(secondPartAddressString1);
                thirdPartAddress.setText(thirdPartAddressString1);
                forthPartAddress.setText(forthPartAddressString1);
                fifthPartAddress.setText(fifthPartAddressString1);

                dialog.dismiss();

            } else {
                alertToastMaker("خطاهای فرم نشانی را برطرف کنید!");
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
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

    public void infoToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_info, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public boolean locationDistanceCalculator(LatLng cameraFocus, LatLng userActualLocation) {
        double theta = cameraFocus.getLongitude() - userActualLocation.getLongitude();
        double dist = Math.sin(deg2rad(cameraFocus.getLatitude()))
                * Math.sin(deg2rad(userActualLocation.getLatitude()))
                + Math.cos(deg2rad(cameraFocus.getLatitude()))
                * Math.cos(deg2rad(userActualLocation.getLatitude()))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        //distance in meters
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;
        return !(dist > 200);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
    }

    private int isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
        int isAvailable = 2;

        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isAvailable = 1;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isAvailable = 1;
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                isAvailable = 1;
            }
        }
        return isAvailable;
    }
}