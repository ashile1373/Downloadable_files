package ir.ashilethegreat.payesh.Activities;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.ConfirmPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class ConfirmPlanRequirementsActivity extends AppCompatActivity {

    TextInputLayout ownerSuggestionOneLayout, ownerSuggestionTwoLayout, educationNeededLayout, educationOneLayout,
            educationTwoLayout, marketingLevelLayout, finalEvaluationLayout, planStrengthLayout, planWeaknessLayout,
            valueChainErrorLayout, challengeErrorLayout;
    AutoCompleteTextView educationNeeded, marketingLevel;
    EditText ownerSuggestionOne, ownerSuggestionTwo, educationOne, educationTwo, finalEvaluation, planStrength, planWeakness;
    CheckBox notUpToDateDataChallenge, humanResourcesChallenge, culturalProblemsChallenge, legalIssuesChallenge, ruleChangingChallenge,
            marketingChallenge, tariffChallenge, importSimilarProductChallenge, financialChallenge, rAndDChallenge,
            supplyRawMaterialsChallenge, infrastructureChallenge, otherChallenge, supply, designValueChain, productionValueChain, processingValueChain, marketingValueChain, othersValueChain;
    ExtendedFloatingActionButton addSuggestion, addEducation;
    LinearLayout back;
    Button nextStep;
    TextView cancelRequest;
    ArrayAdapter<String> adapterOfOptions;
    NestedScrollView nestedScrollView;

    DBHandler dbHandler;
    String planId;
    ConfirmPlanModal confirmSelectedPlan;
    PlanModal selectedPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_plan_requirements);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_green));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        notUpToDateDataChallenge = findViewById(R.id.notUpToDateDataChallenge);
        humanResourcesChallenge = findViewById(R.id.humanResourcesChallenge);
        culturalProblemsChallenge = findViewById(R.id.culturalProblemsChallenge);
        legalIssuesChallenge = findViewById(R.id.legalIssuesChallenge);
        ruleChangingChallenge = findViewById(R.id.ruleChangingChallenge);
        marketingChallenge = findViewById(R.id.marketingChallenge);
        tariffChallenge = findViewById(R.id.tariffChallenge);
        importSimilarProductChallenge = findViewById(R.id.importSimilarProductChallenge);
        financialChallenge = findViewById(R.id.financialChallenge);
        rAndDChallenge = findViewById(R.id.rAndDChallenge);
        supplyRawMaterialsChallenge = findViewById(R.id.supplyRawMaterialsChallenge);
        infrastructureChallenge = findViewById(R.id.infrastructureChallenge);
        otherChallenge = findViewById(R.id.otherChallenge);
        ownerSuggestionOneLayout = findViewById(R.id.ownerSuggestionOneLayout);
        ownerSuggestionTwoLayout = findViewById(R.id.ownerSuggestionTwoLayout);
        educationNeededLayout = findViewById(R.id.educationNeededLayout);
        educationOneLayout = findViewById(R.id.educationOneLayout);
        educationTwoLayout = findViewById(R.id.educationTwoLayout);
        marketingLevelLayout = findViewById(R.id.marketingLevelLayout);
        finalEvaluationLayout = findViewById(R.id.finalEvaluationLayout);
        planStrengthLayout = findViewById(R.id.planStrengthLayout);
        planWeaknessLayout = findViewById(R.id.planWeaknessLayout);
        challengeErrorLayout = findViewById(R.id.challengeErrorLayout);
        valueChainErrorLayout = findViewById(R.id.valueChainErrorLayout);
        educationNeeded = findViewById(R.id.educationNeeded);
        marketingLevel = findViewById(R.id.marketingLevel);
        ownerSuggestionOne = findViewById(R.id.ownerSuggestionOne);
        ownerSuggestionTwo = findViewById(R.id.ownerSuggestionTwo);
        educationOne = findViewById(R.id.educationOne);
        educationTwo = findViewById(R.id.educationTwo);
        finalEvaluation = findViewById(R.id.finalEvaluation);
        addSuggestion = findViewById(R.id.addSuggestion);
        addEducation = findViewById(R.id.addEducation);
        planStrength = findViewById(R.id.planStrength);
        planWeakness = findViewById(R.id.planWeakness);
        supply = findViewById(R.id.supply);
        designValueChain = findViewById(R.id.design);
        productionValueChain = findViewById(R.id.production);
        processingValueChain = findViewById(R.id.processing);
        marketingValueChain = findViewById(R.id.marketing);
        othersValueChain = findViewById(R.id.others);
        nextStep = findViewById(R.id.nextStep);

        dbHandler = new DBHandler(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        PlanModal planModal = dbHandler.planSelectRow(planId);
        confirmSelectedPlan = dbHandler.confirmPlanSelectRow(planId, planModal.getUserID());
        selectedPlan = dbHandler.planSelectRow(planId);

        ownerSuggestionOne.setText(confirmSelectedPlan.getConfirmPlanOwnerSuggestionOne());
        if (!confirmSelectedPlan.getConfirmPlanOwnerSuggestionTwo().isEmpty()) {
            ownerSuggestionTwoLayout.setVisibility(View.VISIBLE);
            ownerSuggestionTwo.setText(confirmSelectedPlan.getConfirmPlanOwnerSuggestionTwo());
            addSuggestion.hide();
        }
        if (!confirmSelectedPlan.getConfirmPlanEducationNeeded().isEmpty()) {
            educationNeeded.setText(dbHandler.readHouseholdsFromID(confirmSelectedPlan.getConfirmPlanEducationNeeded()));
            if (confirmSelectedPlan.getConfirmPlanEducationNeeded().equals("1")) {
                addEducation.show();
                educationOneLayout.setVisibility(View.VISIBLE);
                educationOne.setText(confirmSelectedPlan.getConfirmPlanEducationOne());
                if (!confirmSelectedPlan.getConfirmPlanEducationTwo().isEmpty()) {
                    educationTwoLayout.setVisibility(View.VISIBLE);
                    educationTwo.setText(confirmSelectedPlan.getConfirmPlanEducationTwo());
                    addEducation.hide();
                }
            }
        }
        if (!confirmSelectedPlan.getConfirmPlanMarketingLevel().isEmpty())
            marketingLevel.setText(dbHandler.readPlanMarketingLevelNameFromID(confirmSelectedPlan.getConfirmPlanMarketingLevel()));
        finalEvaluation.setText(confirmSelectedPlan.getConfirmPlanFinalEvaluation());
        planStrength.setText(confirmSelectedPlan.getConfirmPlanStrength());
        planWeakness.setText(confirmSelectedPlan.getConfirmPlanWeakness());

        if (confirmSelectedPlan.getConfirmPlanSupplyValueChain() == 1)
            supply.setChecked(true);
        if (confirmSelectedPlan.getConfirmPlanDesignValueChain() == 1)
            designValueChain.setChecked(true);
        if (confirmSelectedPlan.getConfirmPlanProductionValueChain() == 1)
            productionValueChain.setChecked(true);
        if (confirmSelectedPlan.getConfirmPlanProcessingValueChain() == 1)
            processingValueChain.setChecked(true);
        if (confirmSelectedPlan.getConfirmPlanMarketingValueChain() == 1)
            marketingValueChain.setChecked(true);
        if (confirmSelectedPlan.getConfirmPlanOthersValueChain() == 1)
            othersValueChain.setChecked(true);

        if (confirmSelectedPlan.getConfirmNotUpToDateDataChallenge() == 1)
            notUpToDateDataChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmHumanResourcesChallenge() == 1)
            humanResourcesChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmCulturalProblemsChallenge() == 1)
            culturalProblemsChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmLegalIssuesChallenge() == 1)
            legalIssuesChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmRuleChangingChallenge() == 1)
            ruleChangingChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmMarketingChallenge() == 1)
            marketingChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmTariffChallenge() == 1)
            tariffChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmImportSimilarProductChallenge() == 1)
            importSimilarProductChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmFinancialChallenge() == 1)
            financialChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmRAndDChallenge() == 1)
            rAndDChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmSupplyRawMaterialsChallenge() == 1)
            supplyRawMaterialsChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmInfrastructureChallenge() == 1)
            infrastructureChallenge.setChecked(true);
        if (confirmSelectedPlan.getConfirmOtherChallenge() == 1)
            otherChallenge.setChecked(true);

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readHouseholdsNames());
        educationNeeded.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanMarketingLevelNames());
        marketingLevel.setAdapter(adapterOfOptions);

        educationNeeded.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                educationNeededLayout.setError(null);
                if (dbHandler.readHouseholdsID(educationNeeded.getText().toString()).equals("1")) {
                    educationOneLayout.setVisibility(View.VISIBLE);
                    addEducation.show();
                } else {
                    educationOne.setText("");
                    educationTwo.setText("");
                    educationOneLayout.setVisibility(View.GONE);
                    educationTwoLayout.setVisibility(View.GONE);
                    addEducation.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        educationOne.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                educationOneLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        educationTwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                educationTwoLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        marketingLevel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                marketingLevelLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        finalEvaluation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                finalEvaluationLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planStrength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planStrengthLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        planWeakness.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planWeaknessLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        supply.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });
        designValueChain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });
        productionValueChain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });
        processingValueChain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });
        marketingValueChain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });
        othersValueChain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            valueChainErrorLayout.setError(null);
        });

        notUpToDateDataChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        humanResourcesChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        culturalProblemsChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        legalIssuesChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        ruleChangingChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        marketingChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        tariffChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        importSimilarProductChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        financialChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        rAndDChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        supplyRawMaterialsChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        infrastructureChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });
        otherChallenge.setOnCheckedChangeListener((buttonView, isChecked) -> {
            challengeErrorLayout.setError(null);
        });

        addEducation.setOnClickListener(v -> {
            addEducation.hide();
            educationTwoLayout.setVisibility(View.VISIBLE);
        });

        addSuggestion.setOnClickListener(v -> {
            addSuggestion.hide();
            ownerSuggestionTwoLayout.setVisibility(View.VISIBLE);
        });

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(ConfirmPlanRequirementsActivity.this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");

            else {
                confirmSelectedPlan.setConfirmPlanOwnerSuggestionOne(ownerSuggestionOne.getText().toString());
                confirmSelectedPlan.setConfirmPlanOwnerSuggestionTwo(ownerSuggestionTwo.getText().toString());
                confirmSelectedPlan.setConfirmPlanEducationNeeded(dbHandler.readHouseholdsID(educationNeeded.getText().toString()));
                if (dbHandler.readHouseholdsID(educationNeeded.getText().toString()).equals("1")) {
                    confirmSelectedPlan.setConfirmPlanEducationOne(educationOne.getText().toString());
                    confirmSelectedPlan.setConfirmPlanEducationTwo(educationTwo.getText().toString());
                } else {
                    confirmSelectedPlan.setConfirmPlanEducationOne("");
                    confirmSelectedPlan.setConfirmPlanEducationTwo("");
                }
                confirmSelectedPlan.setConfirmPlanMarketingLevel(dbHandler.readPlanMarketingLevelID(marketingLevel.getText().toString()));
                confirmSelectedPlan.setConfirmPlanFinalEvaluation(finalEvaluation.getText().toString());
                confirmSelectedPlan.setConfirmPlanStrength(planStrength.getText().toString());
                confirmSelectedPlan.setConfirmPlanWeakness(planWeakness.getText().toString());

                if (supply.isChecked())
                    confirmSelectedPlan.setConfirmPlanSupplyValueChain(1);
                else confirmSelectedPlan.setConfirmPlanSupplyValueChain(0);
                if (designValueChain.isChecked())
                    confirmSelectedPlan.setConfirmPlanDesignValueChain(1);
                else confirmSelectedPlan.setConfirmPlanDesignValueChain(0);
                if (productionValueChain.isChecked())
                    confirmSelectedPlan.setConfirmPlanProductionValueChain(1);
                else confirmSelectedPlan.setConfirmPlanProductionValueChain(0);
                if (processingValueChain.isChecked())
                    confirmSelectedPlan.setConfirmPlanProcessingValueChain(1);
                else confirmSelectedPlan.setConfirmPlanProcessingValueChain(0);
                if (marketingValueChain.isChecked())
                    confirmSelectedPlan.setConfirmPlanMarketingValueChain(1);
                else confirmSelectedPlan.setConfirmPlanMarketingValueChain(0);
                if (othersValueChain.isChecked())
                    confirmSelectedPlan.setConfirmPlanOthersValueChain(1);
                else confirmSelectedPlan.setConfirmPlanOthersValueChain(0);

                if (notUpToDateDataChallenge.isChecked())
                    confirmSelectedPlan.setConfirmNotUpToDateDataChallenge(1);
                else confirmSelectedPlan.setConfirmNotUpToDateDataChallenge(0);
                if (humanResourcesChallenge.isChecked())
                    confirmSelectedPlan.setConfirmHumanResourcesChallenge(1);
                else confirmSelectedPlan.setConfirmHumanResourcesChallenge(0);
                if (culturalProblemsChallenge.isChecked())
                    confirmSelectedPlan.setConfirmCulturalProblemsChallenge(1);
                else confirmSelectedPlan.setConfirmCulturalProblemsChallenge(0);
                if (legalIssuesChallenge.isChecked())
                    confirmSelectedPlan.setConfirmLegalIssuesChallenge(1);
                else confirmSelectedPlan.setConfirmLegalIssuesChallenge(0);
                if (ruleChangingChallenge.isChecked())
                    confirmSelectedPlan.setConfirmRuleChangingChallenge(1);
                else confirmSelectedPlan.setConfirmRuleChangingChallenge(0);
                if (marketingChallenge.isChecked())
                    confirmSelectedPlan.setConfirmMarketingChallenge(1);
                else confirmSelectedPlan.setConfirmMarketingChallenge(0);
                if (tariffChallenge.isChecked())
                    confirmSelectedPlan.setConfirmTariffChallenge(1);
                else confirmSelectedPlan.setConfirmTariffChallenge(0);
                if (importSimilarProductChallenge.isChecked())
                    confirmSelectedPlan.setConfirmImportSimilarProductChallenge(1);
                else confirmSelectedPlan.setConfirmImportSimilarProductChallenge(0);
                if (financialChallenge.isChecked())
                    confirmSelectedPlan.setConfirmFinancialChallenge(1);
                else confirmSelectedPlan.setConfirmFinancialChallenge(0);
                if (rAndDChallenge.isChecked())
                    confirmSelectedPlan.setConfirmRAndDChallenge(1);
                else confirmSelectedPlan.setConfirmRAndDChallenge(0);
                if (supplyRawMaterialsChallenge.isChecked())
                    confirmSelectedPlan.setConfirmSupplyRawMaterialsChallenge(1);
                else confirmSelectedPlan.setConfirmSupplyRawMaterialsChallenge(0);
                if (infrastructureChallenge.isChecked())
                    confirmSelectedPlan.setConfirmInfrastructureChallenge(1);
                else confirmSelectedPlan.setConfirmInfrastructureChallenge(0);
                if (otherChallenge.isChecked())
                    confirmSelectedPlan.setConfirmOtherChallenge(1);
                else confirmSelectedPlan.setConfirmOtherChallenge(0);

                Bundle extras = getIntent().getExtras();
                String planId = extras.getString("planId");

                dbHandler.updateConfirmPlanFinalSupervisionSituation(confirmSelectedPlan);

                selectedPlan.setWhichStep(0);
                selectedPlan.setIsCompleted(1);
                dbHandler.updatePlanInfo(selectedPlan);

                Intent intent = new Intent(ConfirmPlanRequirementsActivity.this, DoneActivity.class);
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
        Intent intent = new Intent(ConfirmPlanRequirementsActivity.this, ConfirmPlanFacilityActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (planWeakness.getText().toString().isEmpty()) {
            planWeaknessLayout.setError("نقاط ضعف طرح را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, planWeaknessLayout.getTop() - 10);
            retVal = true;
        }
        if (planStrength.getText().toString().isEmpty()) {
            planStrengthLayout.setError("نقاط قوت طرح را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, planStrengthLayout.getTop() - 10);
            retVal = true;
        }
        if (finalEvaluation.getText().toString().isEmpty()) {
            finalEvaluationLayout.setError("ارزیابی نهایی و اجرایی ناظر را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, finalEvaluationLayout.getTop() - 10);
            retVal = true;
        }
        if (marketingLevel.getText().toString().isEmpty()) {
            marketingLevelLayout.setError("یک مورد را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, marketingLevelLayout.getTop() - 10);
            retVal = true;
        }
        if (dbHandler.readHouseholdsID(educationNeeded.getText().toString()).equals("1")) {
            if (educationOne.getText().toString().isEmpty()) {
                educationOneLayout.setError("نوع مشاوره/آموزش مورد نیاز درج شود!");
                nestedScrollView.smoothScrollTo(0, educationOneLayout.getTop() - 10);
                retVal = true;
            }
        }
        if (!supply.isChecked() && !designValueChain.isChecked() && !productionValueChain.isChecked() &&
                !processingValueChain.isChecked() && !marketingValueChain.isChecked() && !othersValueChain.isChecked()) {
            valueChainErrorLayout.setError("انتخاب حداقل یک مورد از موارد حلقه ارزش الزامی است!");
            nestedScrollView.smoothScrollTo(0, valueChainErrorLayout.getTop() - 10);
            retVal = true;
        }

        if (educationNeeded.getText().toString().isEmpty()) {
            educationNeededLayout.setError("یک مورد را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, educationNeededLayout.getTop() - 10);
            retVal = true;
        }

        if (!notUpToDateDataChallenge.isChecked() && !humanResourcesChallenge.isChecked() && !culturalProblemsChallenge.isChecked() &&
                !legalIssuesChallenge.isChecked() && !ruleChangingChallenge.isChecked() && !marketingChallenge.isChecked() &&
                !tariffChallenge.isChecked() && !importSimilarProductChallenge.isChecked() && !financialChallenge.isChecked() &&
                !rAndDChallenge.isChecked() && !supplyRawMaterialsChallenge.isChecked() && !infrastructureChallenge.isChecked() &&
                !otherChallenge.isChecked()) {
            challengeErrorLayout.setError("انتخاب حداقل یک مورد از مشکلات و چالش ها از دید شاغل الزامی است!");
            nestedScrollView.smoothScrollTo(0, challengeErrorLayout.getTop() - 10);
            retVal = true;
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
        Toast toast = new Toast(ConfirmPlanRequirementsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}