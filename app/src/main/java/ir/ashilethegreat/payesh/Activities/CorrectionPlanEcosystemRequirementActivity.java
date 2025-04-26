package ir.ashilethegreat.payesh.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.EcosystemCounselingsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemCultivationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemEducationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemFinanceRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemIdentificationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemLicenseRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemMarketingRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemNotificationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemProvidingInfrastructuresRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemProvidingTechnologiesRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class CorrectionPlanEcosystemRequirementActivity extends AppCompatActivity {

    LinearLayout back;
    TextView cancelRequest;
    SwitchCompat license, facility, counseling, marketing, education, providingTechnology, notification, providingInfrastructure,
            identification, cultivation;
    RecyclerView licensesRecyclerView, facilitiesRecyclerView, counselingsRecyclerView, marketingRecyclerView, educationsRecyclerView, providingTechnologiesRecyclerView,
            notificationsRecyclerView, providingInfrastructureRecyclerView, identificationsRecyclerView, cultivationsRecyclerView;
    Button nextStep;
    ExtendedFloatingActionButton addNewLicenseEchoSystem, addNewFacilitiesEchoSystem, addNewCounselingEchoSystem, addNewMarketingEchoSystem,
            addNewEducationEchoSystem, addNewProvidingTechnologyEchoSystem, addNewNotificationEchoSystem, addNewProvidingInfrastructureEchoSystem,
            addNewIdentificationEchoSystem, addNewCultivationEchoSystem;

    DBHandler dbHandler;
    String planId;
    int whichStep = 2;
    PlanModal selectedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction_plan_ecosystem_requirements);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_yellow));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        license = findViewById(R.id.license);
        facility = findViewById(R.id.facility);
        counseling = findViewById(R.id.counseling);
        marketing = findViewById(R.id.marketing);
        education = findViewById(R.id.educationOne);
        providingTechnology = findViewById(R.id.providingTechnology);
        notification = findViewById(R.id.notification);
        providingInfrastructure = findViewById(R.id.providingInfrastructure);
        identification = findViewById(R.id.identification);
        cultivation = findViewById(R.id.cultivation);
        addNewLicenseEchoSystem = findViewById(R.id.addNewLicenseEchoSystem);
        addNewFacilitiesEchoSystem = findViewById(R.id.addNewFacilitiesEchoSystem);
        addNewCounselingEchoSystem = findViewById(R.id.addNewCounselingEchoSystem);
        addNewMarketingEchoSystem = findViewById(R.id.addNewMarketingEchoSystem);
        addNewEducationEchoSystem = findViewById(R.id.addNewEducationEchoSystem);
        addNewProvidingTechnologyEchoSystem = findViewById(R.id.addNewProvidingTechnologyEchoSystem);
        addNewNotificationEchoSystem = findViewById(R.id.addNewNotificationEchoSystem);
        addNewProvidingInfrastructureEchoSystem = findViewById(R.id.addNewProvidingInfrastructureEchoSystem);
        addNewIdentificationEchoSystem = findViewById(R.id.addNewIdentificationEchoSystem);
        addNewCultivationEchoSystem = findViewById(R.id.addNewCultivationEchoSystem);
        cultivation = findViewById(R.id.cultivation);
        licensesRecyclerView = findViewById(R.id.licenseRecyclerView);
        facilitiesRecyclerView = findViewById(R.id.facilitiesRecyclerView);
        counselingsRecyclerView = findViewById(R.id.counselingsRecyclerView);
        marketingRecyclerView = findViewById(R.id.marketingRecyclerView);
        educationsRecyclerView = findViewById(R.id.educationsRecyclerView);
        providingTechnologiesRecyclerView = findViewById(R.id.providingTechnologiesRecyclerView);
        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        providingInfrastructureRecyclerView = findViewById(R.id.providingInfrastructureRecyclerView);
        identificationsRecyclerView = findViewById(R.id.identificationsRecyclerView);
        cultivationsRecyclerView = findViewById(R.id.cultivationsRecyclerView);
        nextStep = findViewById(R.id.nextStep);

        dbHandler = new DBHandler(CorrectionPlanEcosystemRequirementActivity.this);


        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        selectedPlan = dbHandler.planSelectRow(planId);
        PlanModal correctionPlanModal = dbHandler.correctionPlanSelectRow(planId, selectedPlan.getUserID());

        if (selectedPlan.getPlanEchoSystemLicence().equals("1")) {
            license.setChecked(true);
            licensesRecyclerView.setVisibility(View.VISIBLE);
//            addNewLicenseEchoSystem.setVisibility(View.VISIBLE);
        } else {
            license.setChecked(false);
            licensesRecyclerView.setVisibility(View.GONE);
            addNewLicenseEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemFinance().equals("1")) {
            facility.setChecked(true);
            facilitiesRecyclerView.setVisibility(View.VISIBLE);
//            addNewFacilitiesEchoSystem.setVisibility(View.VISIBLE);
        } else {
            facility.setChecked(false);
            facilitiesRecyclerView.setVisibility(View.GONE);
            addNewFacilitiesEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemCounseling().equals("1")) {
            counseling.setChecked(true);
            counselingsRecyclerView.setVisibility(View.VISIBLE);
//            addNewCounselingEchoSystem.setVisibility(View.VISIBLE);
        } else {
            counseling.setChecked(false);
            counselingsRecyclerView.setVisibility(View.GONE);
            addNewCounselingEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemMarketing().equals("1")) {
            marketing.setChecked(true);
            marketingRecyclerView.setVisibility(View.VISIBLE);
//            addNewMarketingEchoSystem.setVisibility(View.VISIBLE);
        } else {
            marketing.setChecked(false);
            marketingRecyclerView.setVisibility(View.GONE);
            addNewMarketingEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemEducation().equals("1")) {
            education.setChecked(true);
            educationsRecyclerView.setVisibility(View.VISIBLE);
//            addNewEducationEchoSystem.setVisibility(View.VISIBLE);
        } else {
            education.setChecked(false);
            educationsRecyclerView.setVisibility(View.GONE);
            addNewEducationEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemProvidingTechnology().equals("1")) {
            providingTechnology.setChecked(true);
            providingTechnologiesRecyclerView.setVisibility(View.VISIBLE);
//            addNewProvidingTechnologyEchoSystem.setVisibility(View.VISIBLE);
        } else {
            providingTechnology.setChecked(false);
            providingTechnologiesRecyclerView.setVisibility(View.GONE);
            addNewProvidingTechnologyEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemNotices().equals("1")) {
            notification.setChecked(true);
            notificationsRecyclerView.setVisibility(View.VISIBLE);
//            addNewNotificationEchoSystem.setVisibility(View.VISIBLE);
        } else {
            notification.setChecked(false);
            notificationsRecyclerView.setVisibility(View.GONE);
            addNewNotificationEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemProvidingInfrastructure().equals("1")) {
            providingInfrastructure.setChecked(true);
            providingInfrastructureRecyclerView.setVisibility(View.VISIBLE);
//            addNewProvidingInfrastructureEchoSystem.setVisibility(View.VISIBLE);
        } else {
            providingInfrastructure.setChecked(false);
            providingInfrastructureRecyclerView.setVisibility(View.GONE);
            addNewProvidingInfrastructureEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemIdentification().equals("1")) {
            identification.setChecked(true);
            identificationsRecyclerView.setVisibility(View.VISIBLE);
//            addNewIdentificationEchoSystem.setVisibility(View.VISIBLE);
        } else {
            identification.setChecked(false);
            identificationsRecyclerView.setVisibility(View.GONE);
            addNewIdentificationEchoSystem.setVisibility(View.GONE);
        }

        if (selectedPlan.getPlanEchoSystemCultivation().equals("1")) {
            cultivation.setChecked(true);
            cultivationsRecyclerView.setVisibility(View.VISIBLE);
//            addNewCultivationEchoSystem.setVisibility(View.VISIBLE);
        } else {
            cultivation.setChecked(false);
            cultivationsRecyclerView.setVisibility(View.GONE);
            addNewCultivationEchoSystem.setVisibility(View.GONE);
        }

        EcosystemLicenseRecyclerViewAdapter adapter1 = new EcosystemLicenseRecyclerViewAdapter(dbHandler.readLicenses(planId), dbHandler.readCorrectionLicenses(planId),false,this);
        licensesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        licensesRecyclerView.setAdapter(adapter1);

        EcosystemFinanceRecyclerViewAdapter adapter2 = new EcosystemFinanceRecyclerViewAdapter(dbHandler.readFacilities(planId),dbHandler.readCorrectionFacilities(planId), false,this);
        facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        facilitiesRecyclerView.setAdapter(adapter2);

        EcosystemCounselingsRecyclerViewAdapter adapter3 = new EcosystemCounselingsRecyclerViewAdapter(dbHandler.readCounselings(planId), this);
        counselingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        counselingsRecyclerView.setAdapter(adapter3);

        EcosystemCultivationsRecyclerViewAdapter adapter4 = new EcosystemCultivationsRecyclerViewAdapter(dbHandler.readCultivations(planId), this);
        cultivationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cultivationsRecyclerView.setAdapter(adapter4);

        EcosystemEducationsRecyclerViewAdapter adapter5 = new EcosystemEducationsRecyclerViewAdapter(dbHandler.readEducations(planId), this);
        educationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        educationsRecyclerView.setAdapter(adapter5);

        EcosystemIdentificationsRecyclerViewAdapter adapter6 = new EcosystemIdentificationsRecyclerViewAdapter(dbHandler.readIdentifications(planId), this);
        identificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        identificationsRecyclerView.setAdapter(adapter6);

        EcosystemMarketingRecyclerViewAdapter adapter7 = new EcosystemMarketingRecyclerViewAdapter(dbHandler.readMarketings(planId), this);
        marketingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        marketingRecyclerView.setAdapter(adapter7);

        EcosystemNotificationsRecyclerViewAdapter adapter8 = new EcosystemNotificationsRecyclerViewAdapter(dbHandler.readNotifications(planId), this);
        notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationsRecyclerView.setAdapter(adapter8);

        EcosystemProvidingInfrastructuresRecyclerViewAdapter adapter9 = new EcosystemProvidingInfrastructuresRecyclerViewAdapter(dbHandler.readProvidingInfrastructures(planId), this);
        providingInfrastructureRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        providingInfrastructureRecyclerView.setAdapter(adapter9);

        EcosystemProvidingTechnologiesRecyclerViewAdapter adapter10 = new EcosystemProvidingTechnologiesRecyclerViewAdapter(dbHandler.readProvidingTechnologies(planId), this);
        providingTechnologiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        providingTechnologiesRecyclerView.setAdapter(adapter10);

        cultivation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                cultivationsRecyclerView.setVisibility(View.VISIBLE);
            //                addNewCultivationEchoSystem.setVisibility(View.VISIBLE);
            //                cultivationsRecyclerView.setVisibility(View.GONE);
            //                addNewCultivationEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        counseling.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                counselingsRecyclerView.setVisibility(View.VISIBLE);
            //                addNewCounselingEchoSystem.setVisibility(View.VISIBLE);
            //                counselingsRecyclerView.setVisibility(View.GONE);
            //                addNewCounselingEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        education.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                educationsRecyclerView.setVisibility(View.VISIBLE);
            //                addNewEducationEchoSystem.setVisibility(View.VISIBLE);
            //                educationsRecyclerView.setVisibility(View.GONE);
            //                addNewEducationEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        facility.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                facilitiesRecyclerView.setVisibility(View.VISIBLE);
            //                addNewFacilitiesEchoSystem.setVisibility(View.VISIBLE);
            //                facilitiesRecyclerView.setVisibility(View.GONE);
            //                addNewFacilitiesEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        identification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                identificationsRecyclerView.setVisibility(View.VISIBLE);
            //                addNewIdentificationEchoSystem.setVisibility(View.VISIBLE);
            //                identificationsRecyclerView.setVisibility(View.GONE);
            //                addNewIdentificationEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        license.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                licensesRecyclerView.setVisibility(View.VISIBLE);
            //                addNewLicenseEchoSystem.setVisibility(View.VISIBLE);
            //                licensesRecyclerView.setVisibility(View.GONE);
            //                addNewLicenseEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        marketing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                marketingRecyclerView.setVisibility(View.VISIBLE);
            //                addNewMarketingEchoSystem.setVisibility(View.VISIBLE);
            //                marketingRecyclerView.setVisibility(View.GONE);
            //                addNewMarketingEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        notification.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                notificationsRecyclerView.setVisibility(View.VISIBLE);
            //                addNewNotificationEchoSystem.setVisibility(View.VISIBLE);
            //                notificationsRecyclerView.setVisibility(View.GONE);
            //                addNewNotificationEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        providingInfrastructure.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                providingInfrastructureRecyclerView.setVisibility(View.VISIBLE);
            //                addNewProvidingInfrastructureEchoSystem.setVisibility(View.VISIBLE);
            //                providingInfrastructureRecyclerView.setVisibility(View.GONE);
            //                addNewProvidingInfrastructureEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });
        providingTechnology.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //                providingTechnologiesRecyclerView.setVisibility(View.VISIBLE);
            //                addNewProvidingTechnologyEchoSystem.setVisibility(View.VISIBLE);
            //                providingTechnologiesRecyclerView.setVisibility(View.GONE);
            //                addNewProvidingTechnologyEchoSystem.setVisibility(View.GONE);
            buttonView.setChecked(isChecked);
        });

        back.setOnClickListener(v -> onBackPressed());

        cancelRequest.setOnClickListener(v ->

        {
            Intent intent = new Intent(CorrectionPlanEcosystemRequirementActivity.this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        });

        nextStep.setOnClickListener(v ->

        {

            selectedPlan.setWhichStep(whichStep);

            dbHandler.updatePlanInfo(selectedPlan);

            Intent intent = new Intent(CorrectionPlanEcosystemRequirementActivity.this, CorrectionPlanLicenseActivity.class);
            intent.putExtra("planId", planId);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(CorrectionPlanEcosystemRequirementActivity.this, CorrectionPlanDetailsActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(CorrectionPlanEcosystemRequirementActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }


}