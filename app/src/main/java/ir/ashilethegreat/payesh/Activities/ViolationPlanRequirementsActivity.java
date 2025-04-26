package ir.ashilethegreat.payesh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.ViolationPlanModal;
import ir.ashilethegreat.payesh.R;
import saman.zamani.persiandate.PersianDate;

public class ViolationPlanRequirementsActivity extends AppCompatActivity {

    CheckBox violationTypePersonalInfo, violationTypeStartDate, violationTypeNoFacilityExist,
            violationTypeNonCooperation, violationTypeNonPlanImplement, violationTypePlanTitle,
            violationTypeContradictionOfFacilities, violationTypeNonCompletionOfPlan, violationTypeIncompatibilityPlan,
            violationTypeConflictInPlanPlace, violationTypePlanClosure;

    TextInputLayout violationErrorLayout, personalInfoDescriptionLayout, startDateDescriptionLayout,
            violationTypeContradictionOfFacilitiesDescriptionLayout, nonCooperationDescriptionLayout, nonPlanImplementDescriptionLayout,
            planTitleDescriptionLayout, noFacilityExistDescriptionLayout, violationTypeConflictInPlanPlaceDescriptionLayout,
            violationTypeNonCompletionOfPlanDescriptionLayout, incompatibilityPlanDescriptionLayout, violationPlanClosureDescriptionLayout,
            planClosureReasonLayout, violationTypeNonCompletionOfPlanStartInMonthLayout, planTitleLayout, receivedFacilityLayout,
            planStartDateLayout;

    EditText nonPlanImplementDescription, nonCooperationDescription, violationTypeContradictionOfFacilitiesDescription,
            startDateDescription, personalInfoDescription, planTitleDescription, noFacilityExistDescription,
            violationTypeNonCompletionOfPlanDescription, incompatibilityPlanDescription, violationTypeConflictInPlanPlaceDescription,
            violationPlanClosureDescription, violationTypeNonCompletionOfPlanStartInMonth, receivedFacility, planStartDate;
    AutoCompleteTextView planClosureReason, planTitle;

    LinearLayout violationTypePlanClosureLayout, violationTypeNonCompletionOfPlanLayout, violationTypePlanTitleLayout,
            violationTypeContradictionOfFacilitiesLayout, violationTypeStartDateLayout;

    ExtendedFloatingActionButton btnChooseStartDate;

    LinearLayout back;
    TextView cancelRequest, receivedFacilityToText;
    Button nextStep;

    NestedScrollView nestedScrollView;
    DBHandler dbHandler;
    String planId, planDay = "", planMonth = "", planYear = "";
    PlanModal selectedPlan;
    ViolationPlanModal violationSelectedPlan;
    ArrayAdapter<String> adapterOfOptions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_violation_plan_requirements);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_red));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nextStep = findViewById(R.id.nextStep);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        btnChooseStartDate = findViewById(R.id.btnChooseStartDate);
        violationTypePersonalInfo = findViewById(R.id.violationTypePersonalInfo);
        violationTypeStartDate = findViewById(R.id.violationTypeStartDate);
        violationTypeNoFacilityExist = findViewById(R.id.violationTypeNoFacilityExist);
        violationTypeNonCooperation = findViewById(R.id.violationTypeNonCooperation);
        violationTypeNonPlanImplement = findViewById(R.id.violationTypeNonPlanImplement);
        violationTypePlanTitle = findViewById(R.id.violationTypePlanTitle);
        violationTypeContradictionOfFacilities = findViewById(R.id.violationTypeContradictionOfFacilities);
        violationTypeNonCompletionOfPlan = findViewById(R.id.violationTypeNonCompletionOfPlan);
        violationTypeIncompatibilityPlan = findViewById(R.id.violationTypeIncompatibilityPlan);
        violationTypeConflictInPlanPlace = findViewById(R.id.violationTypeConflictInPlanPlace);
        violationTypePlanClosure = findViewById(R.id.violationTypePlanClosure);

        violationErrorLayout = findViewById(R.id.violationErrorLayout);
        personalInfoDescriptionLayout = findViewById(R.id.personalInfoDescriptionLayout);
        startDateDescriptionLayout = findViewById(R.id.startDateDescriptionLayout);
        violationTypeContradictionOfFacilitiesDescriptionLayout = findViewById(R.id.violationTypeContradictionOfFacilitiesDescriptionLayout);
        nonCooperationDescriptionLayout = findViewById(R.id.nonCooperationDescriptionLayout);
        nonPlanImplementDescriptionLayout = findViewById(R.id.nonPlanImplementDescriptionLayout);
        planTitleDescriptionLayout = findViewById(R.id.planTitleDescriptionLayout);
        noFacilityExistDescriptionLayout = findViewById(R.id.noFacilityExistDescriptionLayout);
        violationTypeNonCompletionOfPlanDescriptionLayout = findViewById(R.id.violationTypeNonCompletionOfPlanDescriptionLayout);
        incompatibilityPlanDescriptionLayout = findViewById(R.id.incompatibilityPlanDescriptionLayout);
        violationTypeConflictInPlanPlaceDescriptionLayout = findViewById(R.id.violationTypeConflictInPlanPlaceDescriptionLayout);
        violationPlanClosureDescriptionLayout = findViewById(R.id.violationPlanClosureDescriptionLayout);
        planClosureReasonLayout = findViewById(R.id.planClosureReasonLayout);
        violationTypeNonCompletionOfPlanStartInMonthLayout = findViewById(R.id.violationTypeNonCompletionOfPlanStartInMonthLayout);
        planTitleLayout = findViewById(R.id.planTitleLayout);
        receivedFacilityLayout = findViewById(R.id.receivedFacilityLayout);
        planStartDateLayout = findViewById(R.id.planStartDateLayout);

        nonPlanImplementDescription = findViewById(R.id.nonPlanImplementDescription);
        nonCooperationDescription = findViewById(R.id.nonCooperationDescription);
        violationTypeContradictionOfFacilitiesDescription = findViewById(R.id.violationTypeContradictionOfFacilitiesDescription);
        startDateDescription = findViewById(R.id.startDateDescription);
        personalInfoDescription = findViewById(R.id.personalInfoDescription);
        planTitleDescription = findViewById(R.id.planTitleDescription);
        noFacilityExistDescription = findViewById(R.id.noFacilityExistDescription);
        violationTypeNonCompletionOfPlanDescription = findViewById(R.id.violationTypeNonCompletionOfPlanDescription);
        incompatibilityPlanDescription = findViewById(R.id.incompatibilityPlanDescription);
        violationTypeConflictInPlanPlaceDescription = findViewById(R.id.violationTypeConflictInPlanPlaceDescription);
        planClosureReason = findViewById(R.id.planClosureReason);
        violationPlanClosureDescription = findViewById(R.id.violationPlanClosureDescription);
        violationTypeNonCompletionOfPlanStartInMonth = findViewById(R.id.violationTypeNonCompletionOfPlanStartInMonth);
        planTitle = findViewById(R.id.planTitle);
        receivedFacility = findViewById(R.id.receivedFacility);
        receivedFacilityToText = findViewById(R.id.receivedFacilityToText);
        planStartDate = findViewById(R.id.planStartDate);

        violationTypePlanClosureLayout = findViewById(R.id.violationTypePlanClosureLayout);
        violationTypeNonCompletionOfPlanLayout = findViewById(R.id.violationTypeNonCompletionOfPlanLayout);
        violationTypePlanTitleLayout = findViewById(R.id.violationTypePlanTitleLayout);
        violationTypeContradictionOfFacilitiesLayout = findViewById(R.id.violationTypeContradictionOfFacilitiesLayout);
        violationTypeStartDateLayout = findViewById(R.id.violationTypeStartDateLayout);

        dbHandler = new DBHandler(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        selectedPlan = dbHandler.planSelectRow(planId);
        violationSelectedPlan = dbHandler.violationPlanSelectRow(planId, selectedPlan.getUserID());

        if (violationSelectedPlan.getViolationPersonalInfo() == 1) {
            violationTypePersonalInfo.setChecked(true);
            personalInfoDescriptionLayout.setVisibility(View.VISIBLE);
            personalInfoDescription.setText(violationSelectedPlan.getViolationPersonalInfoDescription());
        }

        if (violationSelectedPlan.getViolationStartDate() == 1) {
            violationTypeStartDate.setChecked(true);
            startDateDescription.setText(violationSelectedPlan.getViolationStartDateDescription());
            planStartDate.setText(violationSelectedPlan.getViolationPlanAdditionalCorrectStartDate());
            violationTypeStartDateLayout.setVisibility(View.VISIBLE);
        }

        if (violationSelectedPlan.getViolationEcosystem() == 1) {
            violationTypeNoFacilityExist.setChecked(true);
            noFacilityExistDescription.setText(violationSelectedPlan.getViolationEcosystemDescription());
            noFacilityExistDescriptionLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationNonCooperation() == 1) {
            violationTypeNonCooperation.setChecked(true);
            nonCooperationDescription.setText(violationSelectedPlan.getViolationNonCooperationDescription());
            nonCooperationDescriptionLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationNonPlanImplement() == 1) {
            violationTypeNonPlanImplement.setChecked(true);
            nonPlanImplementDescription.setText(violationSelectedPlan.getViolationNonPlanImplementDescription());
            nonPlanImplementDescriptionLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationPlanTitle() == 1) {
            violationTypePlanTitle.setChecked(true);
            planTitleDescription.setText(violationSelectedPlan.getViolationPlanTitleDescription());
            planTitle.setText(dbHandler.readPlanTitleFromID(violationSelectedPlan.getViolationPlanAdditionalCorrectTitle()));
            violationTypePlanTitleLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationTypeContradictionOfFacilities() == 1) {
            violationTypeContradictionOfFacilities.setChecked(true);
            violationTypeContradictionOfFacilitiesDescription.setText(violationSelectedPlan.getViolationTypeContradictionOfFacilitiesDescription());
            receivedFacility.setText(violationSelectedPlan.getViolationPlanAdditionalCorrectFacility());
            violationTypeContradictionOfFacilitiesLayout.setVisibility(View.VISIBLE);

            String s = receivedFacility.getText().toString();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    receivedFacilityToText.setText("0 ریال");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                receivedFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (violationSelectedPlan.getViolationTypeNonCompletionOfPlan() == 1) {
            violationTypeNonCompletionOfPlan.setChecked(true);
            violationTypeNonCompletionOfPlanDescription.setText(violationSelectedPlan.getViolationTypeNonCompletionOfPlanDescription());
            violationTypeNonCompletionOfPlanStartInMonth.setText(violationSelectedPlan.getViolationPlanAdditionalStartInMonth());
            violationTypeNonCompletionOfPlanLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationPlanIncompatibility() == 1) {
            violationTypeIncompatibilityPlan.setChecked(true);
            incompatibilityPlanDescription.setText(violationSelectedPlan.getViolationPlanIncompatibilityDescription());
            incompatibilityPlanDescriptionLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationConflictInPlanPlace() == 1) {
            violationTypeConflictInPlanPlace.setChecked(true);
            violationTypeConflictInPlanPlaceDescription.setText(violationSelectedPlan.getViolationConflictInPlanPlaceDescription());
            violationTypeConflictInPlanPlaceDescriptionLayout.setVisibility(View.VISIBLE);
        }
        if (violationSelectedPlan.getViolationPlanClosure() == 1) {
            violationTypePlanClosure.setChecked(true);
            violationPlanClosureDescription.setText(violationSelectedPlan.getViolationPlanClosureDescription());
            planClosureReason.setText(dbHandler.readPlanClosureReasonNameFromID(violationSelectedPlan.getViolationPlanAdditionalClosureReason()));
            violationTypePlanClosureLayout.setVisibility(View.VISIBLE);
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanTitleNames());
        planTitle.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanClosureReasonsNames());
        planClosureReason.setAdapter(adapterOfOptions);

        violationTypePersonalInfo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                personalInfoDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                personalInfoDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                personalInfoDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                personalInfoDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                personalInfoDescription.setText("");
            }
        });
        violationTypeStartDate.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypeStartDateLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypeStartDateLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypeStartDateLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypeStartDateLayout.setVisibility(View.GONE);
                            }
                        });
                startDateDescription.setText("");
                planStartDate.setText("");
            }
        });
        violationTypeNoFacilityExist.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                noFacilityExistDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                noFacilityExistDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                noFacilityExistDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                noFacilityExistDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                violationTypeContradictionOfFacilitiesDescription.setText("");
            }
        });
        violationTypeNonCooperation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                nonCooperationDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                nonCooperationDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                nonCooperationDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                nonCooperationDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                nonCooperationDescription.setText("");
            }
        });
        violationTypeNonPlanImplement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                nonPlanImplementDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                nonPlanImplementDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                nonPlanImplementDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                nonPlanImplementDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                nonPlanImplementDescription.setText("");
            }
        });
        violationTypePlanTitle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypePlanTitleLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypePlanTitleLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypePlanTitleLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypePlanTitleLayout.setVisibility(View.GONE);
                            }
                        });
                planTitleDescription.setText("");
                planTitle.setText("");
            }
        });
        violationTypeContradictionOfFacilities.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypeContradictionOfFacilitiesLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypeContradictionOfFacilitiesLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypeContradictionOfFacilitiesLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypeContradictionOfFacilitiesLayout.setVisibility(View.GONE);
                            }
                        });
                violationTypeContradictionOfFacilitiesDescription.setText("");
                receivedFacility.setText("");
            }
        });
        violationTypeNonCompletionOfPlan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypeNonCompletionOfPlanLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypeNonCompletionOfPlanLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypeNonCompletionOfPlanLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypeNonCompletionOfPlanLayout.setVisibility(View.GONE);
                            }
                        });
                violationTypeNonCompletionOfPlanDescription.setText("");
                violationTypeNonCompletionOfPlanStartInMonth.setText("");
            }
        });
        violationTypeIncompatibilityPlan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                incompatibilityPlanDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                incompatibilityPlanDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                incompatibilityPlanDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                incompatibilityPlanDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                incompatibilityPlanDescription.setText("");
            }
        });
        violationTypeConflictInPlanPlace.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypeConflictInPlanPlaceDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypeConflictInPlanPlaceDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypeConflictInPlanPlaceDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypeConflictInPlanPlaceDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
                violationTypeConflictInPlanPlaceDescription.setText("");
            }
        });
        violationTypePlanClosure.setOnCheckedChangeListener((buttonView, isChecked) -> {
            violationErrorLayout.setError(null);
            if (buttonView.isChecked()) {
                violationTypePlanClosureLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                violationTypePlanClosureLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                violationTypePlanClosureLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                violationTypePlanClosureLayout.setVisibility(View.GONE);
                            }
                        });
                violationPlanClosureDescription.setText("");
                planClosureReason.setText("");
            }
        });

        personalInfoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                personalInfoDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        startDateDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                startDateDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        violationTypeContradictionOfFacilitiesDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                violationTypeContradictionOfFacilitiesDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nonCooperationDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nonCooperationDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nonPlanImplementDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                nonPlanImplementDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planTitleDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planTitleDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        noFacilityExistDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noFacilityExistDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        violationTypeNonCompletionOfPlanDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                violationTypeNonCompletionOfPlanDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        incompatibilityPlanDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                incompatibilityPlanDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        violationTypeConflictInPlanPlaceDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                violationTypeConflictInPlanPlaceDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        violationPlanClosureDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                violationPlanClosureDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        planStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planStartDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        receivedFacility.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                receivedFacilityLayout.setError(null);

                receivedFacility.removeTextChangedListener(this);

                try {
                    if (!receivedFacility.getText().toString().isEmpty()) {
                        String originalString = s.toString();

                        Long longval;
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }
                        longval = Long.parseLong(originalString);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        receivedFacilityToText.setText(String.format("%s ریال", formattedString));
                        receivedFacility.setSelection(receivedFacility.getText().length());
                    } else receivedFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                receivedFacility.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planTitleLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        violationTypeNonCompletionOfPlanStartInMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                violationTypeNonCompletionOfPlanStartInMonthLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planClosureReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planClosureReasonLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnChooseStartDate.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_date_picker_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);


            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            NumberPicker day = dialog.findViewById(R.id.dayPicker);
            NumberPicker month = dialog.findViewById(R.id.monthPicker);
            NumberPicker year = dialog.findViewById(R.id.yearPicker);

            day.setMinValue(1);
            day.setMaxValue(31);
            day.setValue(PersianDate.today().getShDay());

            month.setMinValue(1);
            month.setMaxValue(12);
            month.setValue(PersianDate.today().getShMonth());

            year.setMinValue(1390);
            year.setMaxValue(PersianDate.today().getShYear());
            year.setValue(PersianDate.today().getShYear());

            confirm.setOnClickListener(v1 -> {
                planYear = String.valueOf(year.getValue());
                planMonth = String.valueOf(month.getValue());
                planDay = String.valueOf(day.getValue());
                planStartDate.setText(String.format("%s/%s/%s", planYear, planMonth, planDay));

                planStartDateLayout.setError(null);
                dialog.dismiss();
            });
            cancel.setOnClickListener(v1 -> dialog.cancel());
            dialog.show();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");

            else {
                if (violationTypePersonalInfo.isChecked()) {
                    violationSelectedPlan.setViolationPersonalInfo(1);
                    violationSelectedPlan.setViolationPersonalInfoDescription(personalInfoDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationPersonalInfo(0);
                    violationSelectedPlan.setViolationPersonalInfoDescription("");
                }
                if (violationTypeStartDate.isChecked()) {
                    violationSelectedPlan.setViolationStartDate(1);
                    violationSelectedPlan.setViolationStartDateDescription(startDateDescription.getText().toString());
                    violationSelectedPlan.setViolationPlanAdditionalCorrectStartDate(planStartDate.getText().toString());
                } else {
                    violationSelectedPlan.setViolationStartDate(0);
                    violationSelectedPlan.setViolationStartDateDescription("");
                    violationSelectedPlan.setViolationPlanAdditionalCorrectStartDate("");
                }
                if (violationTypeNoFacilityExist.isChecked()) {
                    violationSelectedPlan.setViolationEcosystem(1);
                    violationSelectedPlan.setViolationEcosystemDescription(noFacilityExistDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationEcosystem(0);
                    violationSelectedPlan.setViolationEcosystemDescription("");
                }
                if (violationTypeNonCooperation.isChecked()) {
                    violationSelectedPlan.setViolationNonCooperation(1);
                    violationSelectedPlan.setViolationNonCooperationDescription(nonCooperationDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationNonCooperation(0);
                    violationSelectedPlan.setViolationNonCooperationDescription("");
                }
                if (violationTypeNonPlanImplement.isChecked()) {
                    violationSelectedPlan.setViolationNonPlanImplement(1);
                    violationSelectedPlan.setViolationNonPlanImplementDescription(nonPlanImplementDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationNonPlanImplement(0);
                    violationSelectedPlan.setViolationNonPlanImplementDescription("");
                }
                if (violationTypePlanTitle.isChecked()) {
                    violationSelectedPlan.setViolationPlanTitle(1);
                    violationSelectedPlan.setViolationPlanTitleDescription(planTitleDescription.getText().toString());
                    violationSelectedPlan.setViolationPlanAdditionalCorrectTitle(dbHandler.readPlanTitleID(planTitle.getText().toString()));
                } else {
                    violationSelectedPlan.setViolationPlanTitle(0);
                    violationSelectedPlan.setViolationPlanTitleDescription("");
                    violationSelectedPlan.setViolationPlanAdditionalCorrectTitle("");
                }

                if (violationTypeNonCompletionOfPlan.isChecked()) {
                    violationSelectedPlan.setViolationTypeNonCompletionOfPlan(1);
                    violationSelectedPlan.setViolationTypeNonCompletionOfPlanDescription(violationTypeNonCompletionOfPlanDescription.getText().toString());
                    violationSelectedPlan.setViolationPlanAdditionalStartInMonth(violationTypeNonCompletionOfPlanStartInMonth.getText().toString());
                } else {
                    violationSelectedPlan.setViolationTypeNonCompletionOfPlan(0);
                    violationSelectedPlan.setViolationTypeNonCompletionOfPlanDescription("");
                    violationSelectedPlan.setViolationPlanAdditionalStartInMonth("");
                }

                if (violationTypeContradictionOfFacilities.isChecked()) {
                    violationSelectedPlan.setViolationTypeContradictionOfFacilities(1);
                    violationSelectedPlan.setViolationPlanAdditionalCorrectFacility(receivedFacility.getText().toString());
                    violationSelectedPlan.setViolationTypeContradictionOfFacilitiesDescription(violationTypeContradictionOfFacilitiesDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationTypeContradictionOfFacilities(0);
                    violationSelectedPlan.setViolationPlanAdditionalCorrectFacility("");
                    violationSelectedPlan.setViolationTypeContradictionOfFacilitiesDescription("");
                }

                if (violationTypeIncompatibilityPlan.isChecked()) {
                    violationSelectedPlan.setViolationPlanIncompatibility(1);
                    violationSelectedPlan.setViolationPlanIncompatibilityDescription(incompatibilityPlanDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationPlanIncompatibility(0);
                    violationSelectedPlan.setViolationPlanIncompatibilityDescription("");
                }
                if (violationTypeConflictInPlanPlace.isChecked()) {
                    violationSelectedPlan.setViolationConflictInPlanPlace(1);
                    violationSelectedPlan.setViolationConflictInPlanPlaceDescription(violationTypeConflictInPlanPlaceDescription.getText().toString());
                } else {
                    violationSelectedPlan.setViolationConflictInPlanPlace(0);
                    violationSelectedPlan.setViolationConflictInPlanPlaceDescription("");
                }
                if (violationTypePlanClosure.isChecked()) {
                    violationSelectedPlan.setViolationPlanClosure(1);
                    violationSelectedPlan.setViolationPlanClosureDescription(violationPlanClosureDescription.getText().toString());
                    violationSelectedPlan.setViolationPlanAdditionalClosureReason(dbHandler.readPlanClosureReasonsID(planClosureReason.getText().toString()));
                } else {
                    violationSelectedPlan.setViolationPlanClosure(0);
                    violationSelectedPlan.setViolationPlanClosureDescription("");
                    violationSelectedPlan.setViolationPlanAdditionalClosureReason("");
                }

                Bundle extras = getIntent().getExtras();
                String planId = extras.getString("planId");

                dbHandler.updateViolationPlanFinalSupervisionSituation(violationSelectedPlan);
                selectedPlan.setIsCompleted(1);
                selectedPlan.setWhichStep(0);
                dbHandler.updatePlanInfo(selectedPlan);

                Intent intent = new Intent(this, DoneActivity.class);
                intent.putExtra("planId", planId);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }

        });
        back.setOnClickListener(v -> onBackPressed());

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
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
        Intent intent = new Intent(this, ViolationPlanLocationActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (!violationTypeNonPlanImplement.isChecked() && !violationTypeNonCooperation.isChecked() &&
                !violationTypeNoFacilityExist.isChecked() && !violationTypeStartDate.isChecked() &&
                !violationTypePersonalInfo.isChecked() && !violationTypePlanTitle.isChecked() &&
                !violationTypeContradictionOfFacilities.isChecked() && !violationTypeNonCompletionOfPlan.isChecked() &&
                !violationTypeIncompatibilityPlan.isChecked() && !violationTypeConflictInPlanPlace.isChecked() &&
                !violationTypePlanClosure.isChecked()) {
            violationErrorLayout.setError("انتخاب و درج توضیحات حداقل یک مورد الزامی است!");
            nestedScrollView.smoothScrollTo(0, violationErrorLayout.getTop() - 10);
            retVal = true;
        }

        if (violationTypePlanClosure.isChecked()) {
            if (violationPlanClosureDescription.getText().toString().isEmpty()) {
                violationPlanClosureDescriptionLayout.setError("توضیحات علت جمع آوری طرح را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypePlanClosure.getTop() - 10);
                retVal = true;
            }
            if (planClosureReason.getText().toString().isEmpty()) {
                planClosureReasonLayout.setError("علت جمع آوری طرح را انتخاب نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypePlanClosure.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeConflictInPlanPlace.isChecked()) {
            if (violationTypeConflictInPlanPlaceDescription.getText().toString().isEmpty()) {
                violationTypeConflictInPlanPlaceDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeConflictInPlanPlace.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeIncompatibilityPlan.isChecked()) {
            if (incompatibilityPlanDescription.getText().toString().isEmpty()) {
                incompatibilityPlanDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeIncompatibilityPlan.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeNonCompletionOfPlan.isChecked()) {
            if (violationTypeNonCompletionOfPlanDescription.getText().toString().isEmpty()) {
                violationTypeNonCompletionOfPlanDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeNonCompletionOfPlan.getTop() - 10);
                retVal = true;
            }
            if (violationTypeNonCompletionOfPlanStartInMonth.getText().toString().isEmpty()) {
                violationTypeNonCompletionOfPlanStartInMonthLayout.setError("زمان تکمیل طرح را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeNonCompletionOfPlan.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeContradictionOfFacilities.isChecked()) {
            if (violationTypeContradictionOfFacilitiesDescription.getText().toString().isEmpty()) {
                violationTypeContradictionOfFacilitiesDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeContradictionOfFacilities.getTop() - 10);
                retVal = true;
            }
            if (receivedFacility.getText().toString().isEmpty()) {
                receivedFacilityLayout.setError("مبلغ صحیح تسهیلات دریافتی را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeNoFacilityExist.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypePlanTitle.isChecked()) {
            if (planTitleDescription.getText().toString().isEmpty()) {
                planTitleDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypePlanTitle.getTop() - 10);
                retVal = true;
            }
            if (planTitle.getText().toString().isEmpty()) {
                planTitleLayout.setError("عنوان صحیح طرح را انتخاب نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypePlanTitle.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeNoFacilityExist.isChecked()) {
            if (noFacilityExistDescription.getText().toString().isEmpty()) {
                noFacilityExistDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, noFacilityExistDescriptionLayout.getTop() - 10);
                retVal = true;
            }
        }
        if (!planDay.isEmpty() && !planMonth.isEmpty() && !planYear.isEmpty() && !planStartDate.getText().toString().isEmpty()) {
            if (Integer.parseInt(planMonth) > 6) {
                if (Integer.parseInt(planDay) > 30) {
                    planStartDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planMonth) == 12) {
                PersianDate date1 = new PersianDate();
                date1.setShYear(Integer.parseInt(planYear));
                if (Integer.parseInt(planDay) == 30 && !date1.isLeap()) {
                    planStartDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planYear) == PersianDate.today().getShYear()) {
                if (Integer.parseInt(planMonth) == PersianDate.today().getShMonth()) {
                    if (Integer.parseInt(planDay) > PersianDate.today().getShDay()) {
                        planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                        nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                        retVal = true;
                    }
                } else if (Integer.parseInt(planMonth) > PersianDate.today().getShMonth()) {
                    planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                    nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                    retVal = true;
                }
            } else if (Integer.parseInt(planYear) > PersianDate.today().getShYear()) {
                planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                retVal = true;
            }

            if (Integer.parseInt(planYear) < 1390 || Integer.parseInt(planYear) > 1500) {
                planStartDateLayout.setError("سال را به درستی وارد کنید!");
                if (Integer.parseInt(planMonth) > 12) {
                    planStartDateLayout.setError("ماه را به درستی وارد کنید!");
                    if (Integer.parseInt(planDay) > 31) {
                        planStartDateLayout.setError("روز را به درستی وارد کنید!");
                    }
                }
                nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                retVal = true;
            }

        }
        if (violationTypeStartDate.isChecked()) {
            if (startDateDescription.getText().toString().isEmpty()) {
                startDateDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                retVal = true;
            }
            if (planStartDate.getText().toString().isEmpty()) {
                planStartDateLayout.setError("تاریخ صحیح دریافت تسهیلات را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeStartDate.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypePersonalInfo.isChecked()) {
            if (personalInfoDescription.getText().toString().isEmpty()) {
                personalInfoDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypePersonalInfo.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeNonPlanImplement.isChecked()) {
            if (nonPlanImplementDescription.getText().toString().isEmpty()) {
                nonPlanImplementDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeNonPlanImplement.getTop() - 10);
                retVal = true;
            }
        }
        if (violationTypeNonCooperation.isChecked()) {
            if (nonCooperationDescription.getText().toString().isEmpty()) {
                nonCooperationDescriptionLayout.setError("توضیحات تخلف را درج نمایید!");
                nestedScrollView.smoothScrollTo(0, violationTypeNonCooperation.getTop() - 10);
                retVal = true;
            }
        }

        return retVal;
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
}