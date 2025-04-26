package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.DataViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class DataViewActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewpager;
    DataViewAdapter adapter;
    AutoCompleteTextView planFinalSuperVision;
    TextInputLayout planFinalSuperVisionLayout;
    Button confirm;
    DBHandler dbHandler;
    ArrayAdapter<String> adapterOfOptions;
    String planId;
    PlanModal planModal;
    LinearLayout back;
    static public CardView bottomBar, bottomBar2;
    static public boolean flag1 = false, flag2 = false, flag3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_view);

        back = findViewById(R.id.back);
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);
        planFinalSuperVision = findViewById(R.id.planFinalSuperVision);
        planFinalSuperVisionLayout = findViewById(R.id.planFinalSuperVisionLayout);
        confirm = findViewById(R.id.confirm);
        bottomBar = findViewById(R.id.bottomBar);
        bottomBar2 = findViewById(R.id.bottomBar2);

        dbHandler = new DBHandler(this);
        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        planModal = dbHandler.planSelectRow(planId);
        flag1 = false;
        flag2 = false;
        flag3 = false;

        if (planModal.getPlanFinalSuperVisionSituation() != 0)
            planFinalSuperVision.setText(dbHandler.readSuperVisionTypeNameFromID(planModal.getPlanFinalSuperVisionSituation()));

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readSuperVisionTypesNames());
        planFinalSuperVision.setAdapter(adapterOfOptions);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new DataViewAdapter(fragmentManager, getLifecycle());
        viewpager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("مشخصات مجری"));
        tabLayout.addTab(tabLayout.newTab().setText("جزئیات طرح"));
        tabLayout.addTab(tabLayout.newTab().setText("زیست بوم"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        planFinalSuperVision.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planFinalSuperVisionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm.setOnClickListener(v -> {
            if (planFinalSuperVision.getText().toString().isEmpty()) {
                planFinalSuperVisionLayout.setError("یک مورد را انتخاب کنید!");
            } else {
                switch (dbHandler.readSuperVisionTypeID(planFinalSuperVision.getText().toString())) {
                    case ("1") -> {
                        //PLAN CONFIRM
                        if (planModal.getPlanFinalSuperVisionSituation() == 0) {
                            dbHandler.setPlanFinalSupervisionSituation(1, planId, planModal.getUserID());
                            dbHandler.setCorrectionPlanValues(planId, planModal.getUserID());

                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_correction_needed_dialog_layout);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);

                            Button confirm = dialog.findViewById(R.id.confirm);
                            Button cancel = dialog.findViewById(R.id.cancel);

                            confirm.setOnClickListener(v1 -> {
                                Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                                intent.putExtra("planId", planId);
                                intent.putExtra("isCorrectionNeeded", 0);
                                intent.putExtra("goTo", 1);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();

                                dialog.dismiss();
                            });

                            cancel.setOnClickListener(v1 -> {
                                Intent intent = new Intent(this, ConfirmPlanLicenseActivity.class);
                                intent.putExtra("planId", planId);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();

                                dialog.dismiss();
                            });
                            dialog.show();
                        } else
                            createWarningDialog(1);
                    }
                    case ("2") -> {
                        //PLAN VIOLATION
                        if (planModal.getPlanFinalSuperVisionSituation() == 0) {
                            dbHandler.setPlanFinalSupervisionSituation(2, planId, planModal.getUserID());
                            dbHandler.setCorrectionPlanValues(planId, planModal.getUserID());

                            Dialog dialog = new Dialog(this);
                            dialog.setContentView(R.layout.custom_correction_needed_dialog_layout);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setCancelable(false);

                            Button confirm = dialog.findViewById(R.id.confirm);
                            Button cancel = dialog.findViewById(R.id.cancel);

                            confirm.setOnClickListener(v1 -> {
                                Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                                intent.putExtra("planId", planId);
                                intent.putExtra("isCorrectionNeeded", 0);
                                intent.putExtra("goTo", 2);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();

                                dialog.dismiss();
                            });

                            cancel.setOnClickListener(v1 -> {
                                Intent intent = new Intent(this, ViolationPlanLocationActivity.class);
                                intent.putExtra("planId", planId);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();

                                dialog.dismiss();
                            });
                            dialog.show();

                        } else
                            createWarningDialog(2);
                    }
                    case ("3") -> {
                        //PLAN REJECTED
                        if (planModal.getPlanFinalSuperVisionSituation() == 0) {
                            dbHandler.setPlanFinalSupervisionSituation(3, planId, planModal.getUserID());
                            Intent intent = new Intent(this, RejectedPlanActivity.class);
                            intent.putExtra("planId", planId);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        } else
                            createWarningDialog(3);
                    }
                    case ("4") -> {
                        //PLAN CORRECTION
                        if (planModal.getPlanFinalSuperVisionSituation() == 0) {
                            dbHandler.setPlanFinalSupervisionSituation(4, planId, planModal.getUserID());
                            ArrayList<FacilityModal> facilityModalArrayList = dbHandler.readFacilities(planId);
                            ArrayList<LicenseModal> licenseModalArrayList = dbHandler.readLicenses(planId);
                            for (int i = 0; i < facilityModalArrayList.size(); i++) {
                                dbHandler.insertCorrectionEcosystemFacility(facilityModalArrayList.get(i));
                            }
                            for (int i = 0; i < licenseModalArrayList.size(); i++) {
                                dbHandler.insertCorrectionEcosystemLicense(licenseModalArrayList.get(i));
                            }
                            Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                            intent.putExtra("planId", planId);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        } else
                            createWarningDialog(4);
                    }
//                    case ("5") -> {
//                        if (planModal.getPlanFinalSuperVisionSituation() == 0){
//                            dbHandler.setPlanFinalSupervisionSituation(5, planId);
//                            onBackPressed();
//                        }
//                        else
//                            createWarningDialog(5);
//
//                    }
                }
            }
        });

        back.setOnClickListener(v -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DataViewActivity.this, PendingToSuperVisionActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void createWarningDialog(int situation) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_change_super_vision_confirmation_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        Button confirm = dialog.findViewById(R.id.confirm);
        Button cancel = dialog.findViewById(R.id.cancel);
        dialog.show();

        confirm.setOnClickListener(v -> {
            dialog.dismiss();
            dbHandler.deleteFinalSuperVisionPlan(planId, planModal.getUserID());
            dbHandler.setPlanFinalSupervisionSituation(situation, planId, planModal.getUserID());

            switch (situation) {
                case (1) -> {
                    dbHandler.setCorrectionPlanValues(planId, planModal.getUserID());

                    Dialog dialog1 = new Dialog(this);
                    dialog1.setContentView(R.layout.custom_correction_needed_dialog_layout);
                    Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(false);

                    Button confirm1 = dialog1.findViewById(R.id.confirm);
                    Button cancel1 = dialog1.findViewById(R.id.cancel);

                    confirm1.setOnClickListener(v1 -> {
                        Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                        intent.putExtra("planId", planId);
                        intent.putExtra("isCorrectionNeeded", 0);
                        intent.putExtra("goTo", 1);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                        dialog1.dismiss();
                    });

                    cancel1.setOnClickListener(v1 -> {
                        Intent intent = new Intent(this, ConfirmPlanLicenseActivity.class);
                        intent.putExtra("planId", planId);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                        dialog1.dismiss();
                    });
                    dialog1.show();
                }
                case (2) -> {
                    dbHandler.setCorrectionPlanValues(planId, planModal.getUserID());

                    Dialog dialog1 = new Dialog(this);
                    dialog1.setContentView(R.layout.custom_correction_needed_dialog_layout);
                    Objects.requireNonNull(dialog1.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog1.setCanceledOnTouchOutside(false);
                    dialog1.setCancelable(false);

                    Button confirm1 = dialog1.findViewById(R.id.confirm);
                    Button cancel1 = dialog1.findViewById(R.id.cancel);

                    confirm1.setOnClickListener(v1 -> {
                        Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                        intent.putExtra("planId", planId);
                        intent.putExtra("isCorrectionNeeded", 0);
                        intent.putExtra("goTo", 2);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                        dialog1.dismiss();
                    });

                    cancel1.setOnClickListener(v1 -> {
                        Intent intent = new Intent(this, ViolationPlanLocationActivity.class);
                        intent.putExtra("planId", planId);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();

                        dialog1.dismiss();
                    });
                    dialog1.show();
                }

                case (3) -> {
                    Intent intent = new Intent(this, RejectedPlanActivity.class);
                    intent.putExtra("planId", planId);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                case (4) -> {
                    ArrayList<FacilityModal> facilityModalArrayList = dbHandler.readFacilities(planId);
                    ArrayList<LicenseModal> licenseModalArrayList = dbHandler.readLicenses(planId);
                    for (int i = 0; i < facilityModalArrayList.size(); i++) {
                        dbHandler.insertCorrectionEcosystemFacility(facilityModalArrayList.get(i));
                    }
                    for (int i = 0; i < licenseModalArrayList.size(); i++) {
                        dbHandler.insertCorrectionEcosystemLicense(licenseModalArrayList.get(i));
                    }
                    Intent intent = new Intent(this, CorrectionPlanDetailsActivity.class);
                    intent.putExtra("planId", planId);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();

                }
//                    case ("5") -> {
//                        if (planModal.getPlanFinalSuperVisionSituation() == 0){
//                            dbHandler.setPlanFinalSupervisionSituation(5, planId);
//                            onBackPressed();
//                        }
//                        else
//                            createWarningDialog(5);
//
//                    }
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());
    }
}