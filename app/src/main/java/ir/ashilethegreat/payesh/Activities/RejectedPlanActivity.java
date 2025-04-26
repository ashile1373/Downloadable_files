package ir.ashilethegreat.payesh.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.RejectedPlanModal;
import ir.ashilethegreat.payesh.R;

public class RejectedPlanActivity extends AppCompatActivity {

    AutoCompleteTextView rejectionReason;
    EditText rejectedTypeNonCooperationDescription, rejectedTypeFailureToImplementDescription, rejectedTypePersonalInfoDescription, rejectedTypeNoFacilityExistDescription,
            rejectedTypeNonCompletionOfPlanStartInMonth, rejectedNotAnsweredDescription, rejectedTypeNonCompletionOfPlanDescription;
    TextInputLayout rejectionReasonLayout, rejectedTypeNonCooperationDescriptionLayout, rejectedTypePersonalInfoDescriptionLayout,
            rejectedTypeNoFacilityExistDescriptionLayout, rejectedTypeNonCompletionOfPlanStartInMonthLayout, rejectedNotAnsweredDescriptionLayout,
            rejectedErrorLayout, rejectedTypeFailureToImplementDescriptionLayout, rejectedTypeNonCompletionOfPlanDescriptionLayout;
    NestedScrollView nestedScrollView;
    CheckBox rejectedTypeNonCooperation, rejectedTypePersonalInfo, rejectedTypeNoFacilityExist, rejectedTypeFailureToImplement, rejectedTypeNonCompletionOfPlan;


    ArrayAdapter<String> adapterOfOptions;
    LinearLayout back, rejectedAnsweringLayout, rejectedTypeNonCompletionOfPlanLayout;
    TextView cancelRequest;
    Button nextStep;
    DBHandler dbHandler;
    String planId;
    int whichStep = 2;
    RejectedPlanModal rejectedSelectedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejected_plan);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.light_blue_600));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nextStep = findViewById(R.id.nextStep);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        rejectionReasonLayout = findViewById(R.id.rejectionReasonLayout);
        rejectedAnsweringLayout = findViewById(R.id.rejectedAnsweringLayout);
        rejectedTypeFailureToImplementDescriptionLayout = findViewById(R.id.rejectedTypeFailureToImplementDescriptionLayout);
        rejectedErrorLayout = findViewById(R.id.rejectedErrorLayout);
        rejectedTypeNonCompletionOfPlanLayout = findViewById(R.id.rejectedTypeNonCompletionOfPlanLayout);
        rejectedNotAnsweredDescriptionLayout = findViewById(R.id.rejectedNotAnsweredDescriptionLayout);
        rejectedTypeNonCooperationDescriptionLayout = findViewById(R.id.rejectedTypeNonCooperationDescriptionLayout);
        rejectedTypePersonalInfoDescriptionLayout = findViewById(R.id.rejectedTypePersonalInfoDescriptionLayout);
        rejectedTypeNoFacilityExistDescriptionLayout = findViewById(R.id.rejectedTypeNoFacilityExistDescriptionLayout);
        rejectedTypeNonCompletionOfPlanStartInMonthLayout = findViewById(R.id.rejectedTypeNonCompletionOfPlanStartInMonthLayout);
        rejectedTypeNonCompletionOfPlanDescriptionLayout = findViewById(R.id.rejectedTypeNonCompletionOfPlanDescriptionLayout);
        rejectedTypeNonCooperationDescription = findViewById(R.id.rejectedTypeNonCooperationDescription);
        rejectedTypeFailureToImplementDescription = findViewById(R.id.rejectedTypeFailureToImplementDescription);
        rejectedTypePersonalInfoDescription = findViewById(R.id.rejectedTypePersonalInfoDescription);
        rejectedTypeNoFacilityExistDescription = findViewById(R.id.rejectedTypeNoFacilityExistDescription);
        rejectedTypeNonCompletionOfPlanDescription = findViewById(R.id.rejectedTypeNonCompletionOfPlanDescription);
        rejectedTypeNonCompletionOfPlanStartInMonth = findViewById(R.id.rejectedTypeNonCompletionOfPlanStartInMonth);
        rejectionReason = findViewById(R.id.rejectionReason);
        rejectedNotAnsweredDescription = findViewById(R.id.rejectedNotAnsweredDescription);
        rejectedTypeNonCooperation = findViewById(R.id.rejectedTypeNonCooperation);
        rejectedTypePersonalInfo = findViewById(R.id.rejectedTypePersonalInfo);
        rejectedTypeNoFacilityExist = findViewById(R.id.rejectedTypeNoFacilityExist);
        rejectedTypeFailureToImplement = findViewById(R.id.rejectedTypeFailureToImplementPlan);
        rejectedTypeNonCompletionOfPlan = findViewById(R.id.rejectedTypeNonCompletionOfPlan);

        dbHandler = new DBHandler(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        PlanModal selectedPlan = dbHandler.planSelectRow(planId);
        rejectedSelectedPlan = dbHandler.rejectedPlanSelectRow(planId, selectedPlan.getUserID());

        if (!rejectedSelectedPlan.getRejectionReason().equals(""))
            rejectionReason.setText(dbHandler.readRejectedTypeNameFromID(rejectedSelectedPlan.getRejectionReason()));

        if (rejectedSelectedPlan.getRejectionReason().equals("2")) {
            rejectedAnsweringLayout.setVisibility(View.VISIBLE);

            if (rejectedSelectedPlan.getRejectedTypeNonCooperation().equals("1")) {
                rejectedTypeNonCooperationDescriptionLayout.setVisibility(View.VISIBLE);
                rejectedTypeNonCooperationDescription.setText(rejectedSelectedPlan.getRejectedTypeNonCooperationDescription());
                rejectedTypeNonCooperation.setChecked(true);
            }
            if (rejectedSelectedPlan.getRejectedTypeFailureToImplement().equals("1")) {
                rejectedTypeFailureToImplementDescriptionLayout.setVisibility(View.VISIBLE);
                rejectedTypeFailureToImplementDescription.setText(rejectedSelectedPlan.getRejectedTypeFailureToImplementDescription());
                rejectedTypeFailureToImplement.setChecked(true);
            }
            if (rejectedSelectedPlan.getRejectedTypePersonalInfo().equals("1")) {
                rejectedTypePersonalInfoDescriptionLayout.setVisibility(View.VISIBLE);
                rejectedTypePersonalInfoDescription.setText(rejectedSelectedPlan.getRejectedTypePersonalInfoDescription());
                rejectedTypePersonalInfo.setChecked(true);
            }
            if (rejectedSelectedPlan.getRejectedTypeNoFacilityExist().equals("1")) {
                rejectedTypeNoFacilityExistDescriptionLayout.setVisibility(View.VISIBLE);
                rejectedTypeNoFacilityExistDescription.setText(rejectedSelectedPlan.getRejectedTypeNoFacilityExistDescription());
                rejectedTypeNoFacilityExist.setChecked(true);
            }
            if (rejectedSelectedPlan.getRejectedTypeNonCompletionOfPlan().equals("1")) {
                rejectedTypeNonCompletionOfPlanLayout.setVisibility(View.VISIBLE);
                rejectedTypeNonCompletionOfPlanDescription.setText(rejectedSelectedPlan.getRejectedTypeNonCompletionOfPlanDescription());
                rejectedTypeNonCompletionOfPlan.setChecked(true);

                rejectedTypeNonCompletionOfPlanStartInMonth.setText(rejectedSelectedPlan.getRejectionTypePlanAdditionalStartInMonth());
            }

        } else if (rejectedSelectedPlan.getRejectionReason().equals("1")) {
            rejectedNotAnsweredDescriptionLayout.setVisibility(View.VISIBLE);
            rejectedNotAnsweredDescription.setText(rejectedSelectedPlan.getRejectedNotAnsweredDescription());
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readRejectedTypesNames());
        rejectionReason.setAdapter(adapterOfOptions);

        rejectionReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectionReasonLayout.setError(null);
                rejectedErrorLayout.setError(null);
                if (!rejectionReason.getText().toString().isEmpty()) {
                    if (dbHandler.readRejectedTypeID(rejectionReason.getText().toString()).equals("1")) {
                        rejectedNotAnsweredDescriptionLayout.setVisibility(View.VISIBLE);
                        rejectedAnsweringLayout.setVisibility(View.GONE);
                        rejectedTypeNonCooperation.setChecked(false);
                        rejectedTypePersonalInfo.setChecked(false);
                        rejectedTypeNoFacilityExist.setChecked(false);
                        rejectedTypeNonCompletionOfPlan.setChecked(false);
                        rejectedTypeFailureToImplement.setChecked(false);

                        rejectedTypeNonCooperationDescription.setText("");
                        rejectedTypePersonalInfoDescription.setText("");
                        rejectedTypeNoFacilityExistDescription.setText("");
                        rejectedTypeNonCompletionOfPlanDescription.setText("");
                        rejectedTypeFailureToImplementDescription.setText("");
                        rejectedTypeNonCompletionOfPlanStartInMonth.setText("");

                    } else {
                        rejectedNotAnsweredDescriptionLayout.setVisibility(View.GONE);
                        rejectedAnsweringLayout.setVisibility(View.VISIBLE);
                        rejectedNotAnsweredDescription.setText("");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        rejectedNotAnsweredDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedNotAnsweredDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypeNonCooperationDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypeNonCooperationDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypeFailureToImplementDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypeFailureToImplementDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypePersonalInfoDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypePersonalInfoDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypeNoFacilityExistDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypeNoFacilityExistDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypeNonCompletionOfPlanDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypeNonCompletionOfPlanDescriptionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        rejectedTypeNonCompletionOfPlanStartInMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rejectedTypeNonCompletionOfPlanStartInMonthLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        rejectedTypeNonCooperation.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rejectedErrorLayout.setError(null);
            if (isChecked) {
                rejectedTypeNonCooperationDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                rejectedTypeNonCooperationDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                rejectedTypeNonCooperation.setChecked(false);
                rejectedTypeNonCooperationDescription.setText("");
                rejectedTypeNonCooperationDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rejectedTypeNonCooperationDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
            }
        });
        rejectedTypeFailureToImplement.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rejectedErrorLayout.setError(null);
            if (isChecked) {
                rejectedTypeFailureToImplement.setChecked(true);
                rejectedTypeFailureToImplementDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                rejectedTypeFailureToImplementDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                rejectedTypeFailureToImplement.setChecked(false);
                rejectedTypeFailureToImplementDescription.setText("");
                rejectedTypeFailureToImplementDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rejectedTypeFailureToImplementDescriptionLayout.setVisibility(View.GONE);
                            }
                        });

            }
        });
        rejectedTypePersonalInfo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rejectedErrorLayout.setError(null);
            if (isChecked) {
                rejectedTypePersonalInfo.setChecked(true);
                rejectedTypePersonalInfoDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                rejectedTypePersonalInfoDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                rejectedTypePersonalInfo.setChecked(false);
                rejectedTypePersonalInfoDescription.setText("");
                rejectedTypePersonalInfoDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rejectedTypePersonalInfoDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
            }
        });
        rejectedTypeNoFacilityExist.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rejectedErrorLayout.setError(null);

            if (isChecked) {
                rejectedTypeNoFacilityExist.setChecked(true);
                rejectedTypeNoFacilityExistDescriptionLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                rejectedTypeNoFacilityExistDescriptionLayout.setVisibility(View.VISIBLE);
                            }
                        });

            } else {
                rejectedTypeNoFacilityExist.setChecked(false);
                rejectedTypeNoFacilityExistDescription.setText("");

                rejectedTypeNoFacilityExistDescriptionLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rejectedTypeNoFacilityExistDescriptionLayout.setVisibility(View.GONE);
                            }
                        });
            }
        });
        rejectedTypeNonCompletionOfPlan.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rejectedErrorLayout.setError(null);
            if (isChecked) {
                rejectedTypeNonCompletionOfPlan.setChecked(true);
                rejectedTypeNonCompletionOfPlanLayout.animate()
                        .alpha(1.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                super.onAnimationStart(animation);
                                rejectedTypeNonCompletionOfPlanLayout.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                rejectedTypeNonCompletionOfPlan.setChecked(false);
                rejectedTypeNonCompletionOfPlanDescription.setText("");
                rejectedTypeNonCompletionOfPlanStartInMonth.setText("");
                rejectedTypeNonCompletionOfPlanLayout.animate()
                        .alpha(0.0f)
                        .setDuration(300)
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                rejectedTypeNonCompletionOfPlanLayout.setVisibility(View.GONE);
                            }
                        });
            }
        });

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف نمایید.");

            else {
                rejectedSelectedPlan.setRejectionReason(dbHandler.readRejectedTypeID(rejectionReason.getText().toString()));
                if (dbHandler.readRejectedTypeID(rejectionReason.getText().toString()).equals("2")) {
                    rejectedSelectedPlan.setRejectedNotAnsweredDescription("");
                    if (rejectedTypeNonCooperation.isChecked()) {
                        rejectedSelectedPlan.setRejectedTypeNonCooperation("1");
                        rejectedSelectedPlan.setRejectedTypeNonCooperationDescription(rejectedTypeNonCooperationDescription.getText().toString());
                    } else {
                        rejectedSelectedPlan.setRejectedTypeNonCooperation("0");
                        rejectedSelectedPlan.setRejectedTypeNonCooperationDescription("");
                    }

                    if (rejectedTypeFailureToImplement.isChecked()) {
                        rejectedSelectedPlan.setRejectedTypeFailureToImplement("1");
                        rejectedSelectedPlan.setRejectedTypeFailureToImplementDescription(rejectedTypeFailureToImplementDescription.getText().toString());
                        rejectedSelectedPlan.setRejectionTypePlanAdditionalStartInMonth(rejectedTypeNonCompletionOfPlanStartInMonth.getText().toString());
                    } else {
                        rejectedSelectedPlan.setRejectedTypeFailureToImplement("0");
                        rejectedSelectedPlan.setRejectedTypeFailureToImplementDescription("");
                        rejectedSelectedPlan.setRejectionTypePlanAdditionalStartInMonth("");
                    }

                    if (rejectedTypePersonalInfo.isChecked()) {
                        rejectedSelectedPlan.setRejectedTypePersonalInfo("1");
                        rejectedSelectedPlan.setRejectedTypePersonalInfoDescription(rejectedTypePersonalInfoDescription.getText().toString());
                    } else {
                        rejectedSelectedPlan.setRejectedTypePersonalInfo("0");
                        rejectedSelectedPlan.setRejectedTypePersonalInfoDescription("");
                    }

                    if (rejectedTypeNoFacilityExist.isChecked()) {
                        rejectedSelectedPlan.setRejectedTypeNoFacilityExist("1");
                        rejectedSelectedPlan.setRejectedTypeNoFacilityExistDescription(rejectedTypeNoFacilityExistDescription.getText().toString());
                    } else {
                        rejectedSelectedPlan.setRejectedTypeNoFacilityExist("0");
                        rejectedSelectedPlan.setRejectedTypeNoFacilityExistDescription("");
                    }
                    if (rejectedTypeNonCompletionOfPlan.isChecked()) {
                        rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlan("1");
                        rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlanDescription(rejectedTypeNonCompletionOfPlanDescription.getText().toString());
                    } else {
                        rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlan("0");
                        rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlanDescription("");
                    }

                } else {
                    rejectedSelectedPlan.setRejectedNotAnsweredDescription(rejectedNotAnsweredDescription.getText().toString());
                    rejectedSelectedPlan.setRejectedTypeNonCooperation("0");
                    rejectedSelectedPlan.setRejectedTypeNonCooperationDescription("");
                    rejectedSelectedPlan.setRejectedTypeFailureToImplement("0");
                    rejectedSelectedPlan.setRejectedTypeFailureToImplementDescription("");
                    rejectedSelectedPlan.setRejectedTypePersonalInfo("0");
                    rejectedSelectedPlan.setRejectedTypePersonalInfoDescription("");
                    rejectedSelectedPlan.setRejectedTypeNoFacilityExist("0");
                    rejectedSelectedPlan.setRejectedTypeNoFacilityExistDescription("");
                    rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlan("0");
                    rejectedSelectedPlan.setRejectedTypeNonCompletionOfPlanDescription("");

                    rejectedSelectedPlan.setRejectionTypePlanAdditionalStartInMonth("");
                }

                Bundle extras = getIntent().getExtras();
                String planId = extras.getString("planId");
                dbHandler.updateRejectionPlanFinalSupervisionSituation(rejectedSelectedPlan);

                selectedPlan.setIsCompleted(1);
                selectedPlan.setStatusRegistration(isNetworkAvailable());
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (dbHandler.readRejectedTypeID(rejectionReason.getText().toString()).equals("2")) {
            if (!rejectedTypeNonCooperation.isChecked() && !rejectedTypePersonalInfo.isChecked() &&
                    !rejectedTypeNoFacilityExist.isChecked() && !rejectedTypeFailureToImplement.isChecked() &&
                    !rejectedTypeNonCompletionOfPlan.isChecked()) {
                rejectedErrorLayout.setError("انتخاب حداقل یک مورد الزامیست!");
                nestedScrollView.smoothScrollTo(0, rejectedErrorLayout.getTop() - 10);
                retVal = true;
            }
            if (rejectedTypeNonCooperation.isChecked()) {
                if (rejectedTypeNonCooperationDescription.getText().toString().isEmpty()) {
                    rejectedTypeNonCooperationDescriptionLayout.setError("توضیحات عدم همکاری مجری را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypeNonCooperationDescriptionLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (rejectedTypeFailureToImplement.isChecked()) {
                if (rejectedTypeFailureToImplementDescription.getText().toString().isEmpty()) {
                    rejectedTypeFailureToImplementDescriptionLayout.setError("توضیحات عدم اجرای طرح را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypeFailureToImplementDescriptionLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (rejectedTypePersonalInfo.isChecked()) {
                if (rejectedTypePersonalInfoDescription.getText().toString().isEmpty()) {
                    rejectedTypePersonalInfoDescriptionLayout.setError("توضیحات مغایرت در اطلاعات فردی را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypePersonalInfoDescriptionLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (rejectedTypeNoFacilityExist.isChecked()) {
                if (rejectedTypeNoFacilityExistDescription.getText().toString().isEmpty()) {
                    rejectedTypeNoFacilityExistDescriptionLayout.setError("توضیحات عدم دریافت تسهیلات را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypeNoFacilityExistDescriptionLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (rejectedTypeNonCompletionOfPlan.isChecked()) {
                if (rejectedTypeNonCompletionOfPlanDescription.getText().toString().isEmpty()) {
                    rejectedTypeNonCompletionOfPlanDescriptionLayout.setError("توضیحات عدم تکمیل طرح در حال حاضر را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypeNonCompletionOfPlanDescriptionLayout.getTop() - 10);
                    retVal = true;
                }
                if (rejectedTypeNonCompletionOfPlanStartInMonth.getText().toString().isEmpty()) {
                    rejectedTypeNonCompletionOfPlanStartInMonthLayout.setError("زمان تکمیل طرح را وارد نمایید!");
                    nestedScrollView.smoothScrollTo(0, rejectedTypeNonCompletionOfPlanStartInMonthLayout.getTop() - 10);
                    retVal = true;
                }
            }
        }
        if (dbHandler.readRejectedTypeID(rejectionReason.getText().toString()).equals("1")) {
            if (rejectedNotAnsweredDescription.getText().toString().isEmpty()) {
                rejectedNotAnsweredDescriptionLayout.setError("توضیحات عدم پاسخ را وارد نمایید!");
                nestedScrollView.smoothScrollTo(0, rejectedNotAnsweredDescriptionLayout.getTop() - 10);
                retVal = true;
            }
        }

        if (rejectionReason.getText().toString().isEmpty()) {
            rejectionReasonLayout.setError("وضعیت تماس را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, rejectionReasonLayout.getTop() - 10);
            retVal = true;
        }

        return retVal;
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