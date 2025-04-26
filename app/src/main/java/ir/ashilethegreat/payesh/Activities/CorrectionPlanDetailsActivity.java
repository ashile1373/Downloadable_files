package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;
import saman.zamani.persiandate.PersianDate;

public class CorrectionPlanDetailsActivity extends AppCompatActivity {

    LinearLayout back;
    TextView cancelRequest;
    Button nextStep;
    ExtendedFloatingActionButton btnChooseStartDate;
    NestedScrollView nestedScrollView;

    ArrayAdapter<String> adapterOfOptions;
    AutoCompleteTextView fieldOfStudy, households, education, specialization, planTitle, planEconomicSection, planSituation;
    TextInputLayout phoneLayout, fixedPhoneLayout, householdsLayout, educationLayout, fieldOfStudyLayout, specializationLayout,
            planStartDateLayout, planEconomicSectionLayout, planSituationLayout, planTitleLayout;
    EditText name, familyName, parentName, natCode, phone, fixedPhone, birthday, planStartDate;

    DBHandler dbHandler;
    String planId, planDay = "", planMonth = "", planYear = "";
    int isCorrectionNeeded = 0, goTo;
    PlanModal selectedPlan;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correction_plan_details);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_yellow));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nextStep = findViewById(R.id.nextStep);
        phoneLayout = findViewById(R.id.phoneLayout);
        btnChooseStartDate = findViewById(R.id.btnChooseStartDate);
        fixedPhoneLayout = findViewById(R.id.fixedPhoneLayout);
        planStartDateLayout = findViewById(R.id.planStartDateLayout);
        householdsLayout = findViewById(R.id.householdsLayout);
        educationLayout = findViewById(R.id.educationOneLayout);
        fieldOfStudyLayout = findViewById(R.id.fieldOfStudyLayout);
        specializationLayout = findViewById(R.id.specializationLayout);
        households = findViewById(R.id.households);
        specialization = findViewById(R.id.specialization);
        name = findViewById(R.id.name);
        familyName = findViewById(R.id.familyName);
        parentName = findViewById(R.id.parentName);
        natCode = findViewById(R.id.natCode);
        phone = findViewById(R.id.phone);
        fixedPhone = findViewById(R.id.fixedPhone);
        birthday = findViewById(R.id.birthday);
        education = findViewById(R.id.educationOne);
        fieldOfStudy = findViewById(R.id.fieldOfStudy);
        planStartDate = findViewById(R.id.planStartDate);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        planSituationLayout = findViewById(R.id.planSituationLayout);
        planTitleLayout = findViewById(R.id.planTitleLayout);
        planEconomicSectionLayout = findViewById(R.id.planEconomicSectionLayout);
        planEconomicSection = findViewById(R.id.planEconomicSection);
        planSituation = findViewById(R.id.planSituation);
        planTitle = findViewById(R.id.planTitle);

        dbHandler = new DBHandler(CorrectionPlanDetailsActivity.this);
        requestQueue = Volley.newRequestQueue(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        isCorrectionNeeded = Objects.requireNonNull(getIntent().getExtras()).getInt("isCorrectionNeeded");
        goTo = Objects.requireNonNull(getIntent().getExtras()).getInt("goTo");

        selectedPlan = dbHandler.planSelectRow(planId);
        PlanModal correctionPlanModal = dbHandler.correctionPlanSelectRow(planId, selectedPlan.getUserID());

        name.setText(selectedPlan.getName());
        familyName.setText(selectedPlan.getFamilyName());
        parentName.setText(selectedPlan.getParentName());
        natCode.setText(selectedPlan.getNatCode());
        birthday.setText(selectedPlan.getBirthday().replace("-", "/"));
        if (!correctionPlanModal.getPhone().isEmpty())
            phone.setText(correctionPlanModal.getPhone());
        else {
            if (!selectedPlan.getPhone().isEmpty())
                phone.setText(selectedPlan.getPhone());
        }
        if (!correctionPlanModal.getFixedPhone().isEmpty())
            fixedPhone.setText(correctionPlanModal.getFixedPhone());
        else {
            if (!selectedPlan.getFixedPhone().isEmpty())
                fixedPhone.setText(selectedPlan.getFixedPhone());
        }
        if (!correctionPlanModal.getPlanStartDate().isEmpty())
            planStartDate.setText(correctionPlanModal.getPlanStartDate());
        else {
            if (!selectedPlan.getPlanStartDate().isEmpty())
                planStartDate.setText(selectedPlan.getPlanStartDate());
        }
        if (!correctionPlanModal.getEducation().isEmpty()) {
            education.setText(dbHandler.readEducationFromID(correctionPlanModal.getEducation()));
            if (correctionPlanModal.getEducation().equals("6") || correctionPlanModal.getEducation().equals("7") || correctionPlanModal.getEducation().equals("8") ||
                    correctionPlanModal.getEducation().equals("9") || correctionPlanModal.getEducation().equals("10")) {
                fieldOfStudyLayout.setVisibility(View.VISIBLE);
                fieldOfStudy.setThreshold(1);
                specializationLayout.setVisibility(View.VISIBLE);
                if (!correctionPlanModal.getFieldOfStudy().isEmpty())
                    fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(correctionPlanModal.getFieldOfStudy()));
                else {
                    if (selectedPlan.getFieldOfStudy() != null) {
                        if (!selectedPlan.getFieldOfStudy().isEmpty())
                            fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(selectedPlan.getFieldOfStudy()));
                    } else {
                        fieldOfStudy.setText("");
                        alertToastMaker("خطا در نمایش مقادیر!\nسوال «رشته ی تحصیلی» را بررسی کنید.");
                    }
                }
                if (!correctionPlanModal.getSpecialization().isEmpty())
                    specialization.setText(dbHandler.readSpecializationFromID(correctionPlanModal.getSpecialization()));
                else {
                    if (selectedPlan.getSpecialization() != null) {
                        if (!selectedPlan.getSpecialization().isEmpty())
                            specialization.setText(dbHandler.readSpecializationFromID(selectedPlan.getSpecialization()));
                    } else {
                        specialization.setText("");
                        alertToastMaker("خطا در نمایش مقادیر!\nسوال «نوع تخصص» را بررسی کنید.");
                    }
                }
            }
        } else {
            if (selectedPlan.getEducation() != null) {
                if (!selectedPlan.getEducation().isEmpty()) {
                    education.setText(dbHandler.readEducationFromID(selectedPlan.getEducation()));
                    if (selectedPlan.getEducation().equals("6") || selectedPlan.getEducation().equals("7") || selectedPlan.getEducation().equals("8") ||
                            selectedPlan.getEducation().equals("9") || selectedPlan.getEducation().equals("10")) {
                        fieldOfStudyLayout.setVisibility(View.VISIBLE);
                        fieldOfStudy.setThreshold(1);
                        specializationLayout.setVisibility(View.VISIBLE);
                        if (!correctionPlanModal.getFieldOfStudy().isEmpty())
                            fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(correctionPlanModal.getFieldOfStudy()));
                        else {
                            if (selectedPlan.getFieldOfStudy() != null) {
                                if (!selectedPlan.getFieldOfStudy().isEmpty())
                                    fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(selectedPlan.getFieldOfStudy()));
                            } else {
                                fieldOfStudy.setText("");
                                alertToastMaker("خطا در نمایش مقادیر!\nسوال «رشته ی تحصیلی» را بررسی کنید.");
                            }
                        }
                        if (!correctionPlanModal.getSpecialization().isEmpty())
                            specialization.setText(dbHandler.readSpecializationFromID(correctionPlanModal.getSpecialization()));
                        else {
                            if (selectedPlan.getSpecialization() != null) {
                                if (!selectedPlan.getSpecialization().isEmpty())
                                    specialization.setText(dbHandler.readSpecializationFromID(selectedPlan.getSpecialization()));
                            } else {
                                specialization.setText("");
                                alertToastMaker("خطا در نمایش مقادیر!\nسوال «نوع تخصص» را بررسی کنید.");
                            }
                        }
                    }
                }
            } else {
                education.setText("");
                alertToastMaker("خطا در نمایش مقادیر!\nسوال «تحصیلات مجری» را بررسی کنید.");
            }
        }
        if (!correctionPlanModal.getHouseholds().isEmpty())
            households.setText(dbHandler.readHouseholdsFromID(correctionPlanModal.getHouseholds()));
        else {
            if (selectedPlan.getHouseholds() != null) {
                if (!selectedPlan.getHouseholds().isEmpty())
                    households.setText(dbHandler.readHouseholdsFromID(selectedPlan.getHouseholds()));
            } else {
                households.setText("");
                alertToastMaker("خطا در نمایش مقادیر!\nسوال «مجری سرپرست خانوار است؟» را بررسی کنید.");
            }
        }
        if (!correctionPlanModal.getPlanTitle().isEmpty())
            planTitle.setText(dbHandler.readPlanTitleFromID(correctionPlanModal.getPlanTitle()));
        else {
            if (selectedPlan.getPlanTitle() != null) {
                if (!selectedPlan.getPlanTitle().isEmpty())
                    planTitle.setText(dbHandler.readPlanTitleFromID(selectedPlan.getPlanTitle()));
            } else {
                planTitle.setText("");
                alertToastMaker("خطا در نمایش مقادیر!\nسوال «عنوان طرح» را بررسی کنید.");
            }
        }
        if (!correctionPlanModal.getPlanEconomicSection().isEmpty())
            planEconomicSection.setText(dbHandler.readPlanEconomicSectionFromID(correctionPlanModal.getPlanEconomicSection()));
        else {
            if (selectedPlan.getPlanEconomicSection() != null) {
                if (!selectedPlan.getPlanEconomicSection().isEmpty())
                    planEconomicSection.setText(dbHandler.readPlanEconomicSectionFromID(selectedPlan.getPlanEconomicSection()));
            } else {
                planEconomicSection.setText("");
                alertToastMaker("خطا در نمایش مقادیر!\nسوال «بخش اقتصادی» را بررسی کنید.");
            }
        }
        if (!correctionPlanModal.getPlanSituation().isEmpty())
            planSituation.setText(dbHandler.readPlanSituationFromID(correctionPlanModal.getPlanSituation()));
        else {
            if (selectedPlan.getPlanSituation() != null) {
                if (!selectedPlan.getPlanSituation().isEmpty())
                    planSituation.setText(dbHandler.readPlanSituationFromID(selectedPlan.getPlanSituation()));
            } else {
                planSituation.setText("");
                alertToastMaker("خطا در نمایش مقادیر!\nسوال «وضعیت کنونی طرح» را بررسی کنید.");
            }
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readEducationNames());
        education.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readFieldOfStudyNames());
        fieldOfStudy.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readSpecializationNames());
        specialization.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readHouseholdsNames());
        households.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanTitleNames());
        planTitle.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanEconomicSectionNames());
        planEconomicSection.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanSituationNames());
        planSituation.setAdapter(adapterOfOptions);

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fixedPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fixedPhoneLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        education.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                educationLayout.setError(null);
                if (dbHandler.readEducationID(education.getText().toString()).equals("6") || dbHandler.readEducationID(education.getText().toString()).equals("7") ||
                        dbHandler.readEducationID(education.getText().toString()).equals("8") || dbHandler.readEducationID(education.getText().toString()).equals("9") ||
                        dbHandler.readEducationID(education.getText().toString()).equals("10")) {
                    fieldOfStudyLayout.setVisibility(View.VISIBLE);
                    fieldOfStudy.setText("");
                    adapterOfOptions = new ArrayAdapter<>(CorrectionPlanDetailsActivity.this, R.layout.spinner_layout, dbHandler.readFieldOfStudyNames());
                    fieldOfStudy.setAdapter(adapterOfOptions);

                    specializationLayout.setVisibility(View.VISIBLE);
                    specialization.setText("");
                    adapterOfOptions = new ArrayAdapter<>(CorrectionPlanDetailsActivity.this, R.layout.spinner_layout, dbHandler.readSpecializationNames());
                    specialization.setAdapter(adapterOfOptions);
                } else {
                    fieldOfStudyLayout.setVisibility(View.GONE);
                    fieldOfStudy.setText("");
                    specializationLayout.setVisibility(View.GONE);
                    specialization.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fieldOfStudy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fieldOfStudyLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        households.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                householdsLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        specialization.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                specializationLayout.setError(null);
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
        planEconomicSection.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planEconomicSectionLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        planSituation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planSituationLayout.setError(null);
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

            year.setMinValue(1300);
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

        back.setOnClickListener(v -> onBackPressed());

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            intent.putExtra("planId", planId);
            overridePendingTransition(0, 0);
            finish();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");
            else {

                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                if (!selectedPlan.getPhone().equals(phone.getText().toString()))
                    correctionPlanModal.setPhone(phone.getText().toString());
                else correctionPlanModal.setPhone("");
                if (!selectedPlan.getFixedPhone().equals(fixedPhone.getText().toString()))
                    correctionPlanModal.setFixedPhone(fixedPhone.getText().toString());
                else correctionPlanModal.setFixedPhone("");
                if (!selectedPlan.getPlanStartDate().equals(planStartDate.getText().toString()))
                    correctionPlanModal.setPlanStartDate(planStartDate.getText().toString());
                else correctionPlanModal.setPlanStartDate("");
                if (!selectedPlan.getEducation().equals(dbHandler.readEducationID(education.getText().toString())))
                    correctionPlanModal.setEducation(dbHandler.readEducationID(education.getText().toString()));
                else correctionPlanModal.setEducation("");
                if (!selectedPlan.getFieldOfStudy().equals(dbHandler.readFieldOfStudyID(fieldOfStudy.getText().toString())))
                    correctionPlanModal.setFieldOfStudy(dbHandler.readFieldOfStudyID(fieldOfStudy.getText().toString()));
                else correctionPlanModal.setFieldOfStudy("");
                if (!selectedPlan.getSpecialization().equals(dbHandler.readSpecializationID(specialization.getText().toString())))
                    correctionPlanModal.setSpecialization(dbHandler.readSpecializationID(specialization.getText().toString()));
                else correctionPlanModal.setSpecialization("");
                if (!selectedPlan.getHouseholds().equals(dbHandler.readHouseholdsID(households.getText().toString())))
                    correctionPlanModal.setHouseholds(dbHandler.readHouseholdsID(households.getText().toString()));
                else correctionPlanModal.setHouseholds("");
                if (!selectedPlan.getPlanTitle().equals(dbHandler.readPlanTitleID(planTitle.getText().toString())))
                    correctionPlanModal.setPlanTitle(dbHandler.readPlanTitleID(planTitle.getText().toString()));
                else correctionPlanModal.setPlanTitle("");
                if (!selectedPlan.getPlanEconomicSection().equals(dbHandler.readPlanEconomicSectionID(planEconomicSection.getText().toString())))
                    correctionPlanModal.setPlanEconomicSection(dbHandler.readPlanEconomicSectionID(planEconomicSection.getText().toString()));
                else correctionPlanModal.setPlanEconomicSection("");
                if (!selectedPlan.getPlanSituation().equals(dbHandler.readPlanSituationID(planSituation.getText().toString())))
                    correctionPlanModal.setPlanSituation(dbHandler.readPlanSituationID(planSituation.getText().toString()));
                else correctionPlanModal.setPlanSituation("");

                dbHandler.updateCorrectionPlanInfo(correctionPlanModal);


                if (goTo == 1 || goTo == 3) {
                    successfulToastMaker("اطلاعات پایه ای با موفقیت ویرایش شد.");

                    Intent intent = new Intent(this, ConfirmPlanLicenseActivity.class);
                    intent.putExtra("planId", planId);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
                else if (goTo == 2 || goTo == 4) {
                    successfulToastMaker("اطلاعات پایه ای با موفقیت ویرایش شد.");
                    Intent intent = new Intent(this, ViolationPlanLocationActivity.class);
                    intent.putExtra("planId", planId);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (goTo == 1) {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            intent.putExtra("planId", planId);
            startActivity(intent);
            finish();
        }
        if (goTo == 2) {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            intent.putExtra("planId", planId);
            startActivity(intent);
            finish();
        }
        if (goTo == 3) {
            overridePendingTransition(0, 0);
            finish();
        }
        if (goTo == 4) {
            overridePendingTransition(0, 0);
            finish();
        }
        super.onBackPressed();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (planSituation.getText().toString().isEmpty()) {
            planSituationLayout.setError("وضعیت کنونی طرح را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, planSituationLayout.getTop() - 10);
            retVal = true;
        }
        if (planEconomicSection.getText().toString().isEmpty()) {
            planEconomicSectionLayout.setError("بخش اقتصادی طرح را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, planEconomicSectionLayout.getTop() - 10);
            retVal = true;
        }
        if (planTitle.getText().toString().isEmpty()) {
            planTitleLayout.setError("عنوان طرح را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, planTitleLayout.getTop() - 10);
            retVal = true;
        }
        if (dbHandler.readPlanTitleID(planTitle.getText().toString()).equals("")) {
            planTitleLayout.setError("چنین موردی در لیست عناوین طرح ها نیست!");
            nestedScrollView.smoothScrollTo(0, planTitleLayout.getTop() - 10);
            retVal = true;
        }
        if (households.getText().toString().isEmpty()) {
            householdsLayout.setError("یک مورد را انتخاب کنید!");
            nestedScrollView.smoothScrollTo(0, householdsLayout.getTop() - 10);
            retVal = true;
        }
        if (dbHandler.readEducationID(education.getText().toString()).equals("6") || dbHandler.readEducationID(education.getText().toString()).equals("7") ||
                dbHandler.readEducationID(education.getText().toString()).equals("8") || dbHandler.readEducationID(education.getText().toString()).equals("9") ||
                dbHandler.readEducationID(education.getText().toString()).equals("10")) {
            if (specialization.getText().toString().isEmpty()) {
                specializationLayout.setError("یک مورد را انتخاب کنید!");
                nestedScrollView.smoothScrollTo(0, specializationLayout.getTop() - 10);
                retVal = true;
            }
            if (dbHandler.readFieldOfStudyID(fieldOfStudy.getText().toString()).equals("")) {
                fieldOfStudyLayout.setError("چنین موردی در لیست رشته های تحصیلی نیست!");
                nestedScrollView.smoothScrollTo(0, fieldOfStudyLayout.getTop() - 10);
                retVal = true;
            }
            if (fieldOfStudy.getText().toString().isEmpty()) {
                fieldOfStudyLayout.setError("یک مورد را انتخاب کنید!");
                nestedScrollView.smoothScrollTo(0, fieldOfStudyLayout.getTop() - 10);
                retVal = true;
            }
        }
        if (education.getText().toString().isEmpty()) {
            educationLayout.setError("یک مورد را انتخاب کنید!");
            nestedScrollView.smoothScrollTo(0, educationLayout.getTop() - 10);
            retVal = true;
        }
        if (planStartDate.getText().toString().isEmpty()) {
            householdsLayout.setError("تاریخ شروع کار را وارد کنید!");
            nestedScrollView.smoothScrollTo(0, householdsLayout.getTop() - 10);
            retVal = true;
        }
        if (fixedPhone.getText().toString().length() < 11 || !fixedPhone.getText().toString().startsWith("0")) {
            fixedPhoneLayout.setError("شماره تماس ثابت را به درستی بنویسید!");
            nestedScrollView.smoothScrollTo(0, fixedPhoneLayout.getTop() - 10);
            retVal = true;
        }
        if (phone.getText().toString().length() < 11 || !phone.getText().toString().startsWith("09")) {
            phoneLayout.setError("شماره تماس را به درستی بنویسید!");
            nestedScrollView.smoothScrollTo(0, phoneLayout.getTop() - 10);
            retVal = true;
        }

        if (!planDay.isEmpty() && !planMonth.isEmpty() && !planYear.isEmpty() && !planStartDate.getText().toString().isEmpty()) {
            if (Integer.parseInt(planMonth) > 6) {
                if (Integer.parseInt(planDay) > 30) {
                    planStartDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planMonth) == 12) {
                PersianDate date1 = new PersianDate();
                date1.setShYear(Integer.parseInt(planYear));
                if (Integer.parseInt(planDay) == 30 && !date1.isLeap()) {
                    planStartDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planYear) == PersianDate.today().getShYear()) {
                if (Integer.parseInt(planMonth) == PersianDate.today().getShMonth()) {
                    if (Integer.parseInt(planDay) > PersianDate.today().getShDay()) {
                        planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                        nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                        retVal = true;
                    }
                } else if (Integer.parseInt(planMonth) > PersianDate.today().getShMonth()) {
                    planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                    nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                    retVal = true;
                }
            } else if (Integer.parseInt(planYear) > PersianDate.today().getShYear()) {
                planStartDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                retVal = true;
            }

            if (Integer.parseInt(planYear) < 1300 || Integer.parseInt(planYear) > 1500) {
                planStartDateLayout.setError("سال را به درستی وارد کنید!");
                if (Integer.parseInt(planMonth) > 12) {
                    planStartDateLayout.setError("ماه را به درستی وارد کنید!");
                    if (Integer.parseInt(planDay) > 31) {
                        planStartDateLayout.setError("روز را به درستی وارد کنید!");
                    }
                }
                nestedScrollView.smoothScrollTo(0, planStartDateLayout.getTop() - 10);
                retVal = true;
            }

        }

        return retVal;
    }

    public void errorToastMaker(String s) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(s);
        Toast toast = new Toast(CorrectionPlanDetailsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String s) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(s);
        Toast toast = new Toast(CorrectionPlanDetailsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void successfulToastMaker(String s) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_successful, findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(s);
        Toast toast = new Toast(CorrectionPlanDetailsActivity.this);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private boolean checkPhoneNumber(String s) {
        if (s.length() == 0) return true;
        else return s.length() == 11 && s.startsWith("09");
    }

}
