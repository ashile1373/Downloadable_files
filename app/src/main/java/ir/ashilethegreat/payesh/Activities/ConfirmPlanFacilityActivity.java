package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.BailRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.PendingToSuperVisionRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.BailModal;
import ir.ashilethegreat.payesh.DBhandlers.ConfirmPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;
import saman.zamani.persiandate.PersianDate;

public class ConfirmPlanFacilityActivity extends AppCompatActivity {

    ArrayAdapter<String> adapterOfOptions;
    LinearLayout back;
    TextView cancelRequest;
    Button nextStep;
    TextInputLayout receivedFacilitySituationLayout, receivedFacilityLayout, notReceivedFacilityReasonLayout,
            monthlyIncomeLayout, bankTitleLayout, bankBranchLayout, bailErrorLayout, bailDescriptionLayout,
            paymentDateLayout, dateMarginLayout, facilityDeviationExistLayout, facilityMarginReasonLayout;
    AutoCompleteTextView receivedFacilitySituation, bankTitle, facilityDeviationExist;
    EditText receivedFacility, notReceivedFacilityReason, monthlyIncome, bankBranch, paymentDate, dateMargin, bailDescription, facilityMarginReason;
    TextView receivedFacilityToText, monthlyIncomeToText;
    ExtendedFloatingActionButton btnChoosePaymentDate, btnEnterBail;
    NestedScrollView nestedScrollView;
    public static RecyclerView bailsRecyclerView;

    DBHandler dbHandler;
    String planId, planDay = "", planMonth = "", planYear = "";
    int whichStep = 2;
    ConfirmPlanModal confirmSelectedPlan;
    BailRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_plan_facility);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.toast_green));

        back = findViewById(R.id.back);
        cancelRequest = findViewById(R.id.cancelRequest);
        nextStep = findViewById(R.id.nextStep);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        receivedFacilitySituationLayout = findViewById(R.id.receivedFacilitySituationLayout);
        receivedFacilityLayout = findViewById(R.id.receivedFacilityLayout);
        notReceivedFacilityReasonLayout = findViewById(R.id.notReceivedFacilityReasonLayout);
        bailErrorLayout = findViewById(R.id.bailErrorLayout);
        bailsRecyclerView = findViewById(R.id.bailsRecyclerView);
        btnEnterBail = findViewById(R.id.btnEnterBail);
        monthlyIncomeLayout = findViewById(R.id.monthlyIncomeLayout);
        bailDescriptionLayout = findViewById(R.id.bailDescriptionLayout);
        bankTitleLayout = findViewById(R.id.bankTitleLayout);
        bankBranchLayout = findViewById(R.id.bankBranchLayout);
        paymentDateLayout = findViewById(R.id.paymentDateLayout);
        dateMarginLayout = findViewById(R.id.dateMarginLayout);
        facilityMarginReasonLayout = findViewById(R.id.facilityMarginReasonLayout);
        facilityDeviationExistLayout = findViewById(R.id.facilityDeviationExistLayout);
        receivedFacilitySituation = findViewById(R.id.receivedFacilitySituation);
        bankTitle = findViewById(R.id.bankTitle);
        facilityDeviationExist = findViewById(R.id.facilityDeviationExist);
        receivedFacility = findViewById(R.id.receivedFacility);
        notReceivedFacilityReason = findViewById(R.id.notReceivedFacilityReason);
        monthlyIncome = findViewById(R.id.monthlyIncome);
        bankBranch = findViewById(R.id.bankBranch);
        bailDescription = findViewById(R.id.bailDescription);
        paymentDate = findViewById(R.id.paymentDate);
        dateMargin = findViewById(R.id.dateMargin);
        facilityMarginReason = findViewById(R.id.facilityMarginReason);
        receivedFacilityToText = findViewById(R.id.receivedFacilityToText);
        monthlyIncomeToText = findViewById(R.id.monthlyIncomeToText);
        btnChoosePaymentDate = findViewById(R.id.btnChoosePaymentDate);

        dbHandler = new DBHandler(this);

        planId = Objects.requireNonNull(getIntent().getExtras()).getString("planId");
        PlanModal selectedPlan = dbHandler.planSelectRow(planId);
        confirmSelectedPlan = dbHandler.confirmPlanSelectRow(planId, selectedPlan.getUserID());

        if (!confirmSelectedPlan.getConfirmPlanReceivedFacilitySituation().equals("")) {
            receivedFacilitySituation.setText(dbHandler.readReceivedFacilitySituationTypeNameFromID(confirmSelectedPlan.getConfirmPlanReceivedFacilitySituation()));
            if (confirmSelectedPlan.getConfirmPlanReceivedFacilitySituation().equals("2")) {
                notReceivedFacilityReason.setText(confirmSelectedPlan.getConfirmPlanNotReceivedFacilityReason());
                notReceivedFacilityReasonLayout.setVisibility(View.VISIBLE);
            }
        }
        if (!confirmSelectedPlan.getConfirmPlanBankTitle().equals(""))
            bankTitle.setText(dbHandler.readBankNameFromID(confirmSelectedPlan.getConfirmPlanBankTitle()));

        if (dbHandler.readPlanBails(planId, selectedPlan.getUserID()).size() > 0) {
            bailsRecyclerView.setVisibility(View.VISIBLE);
            bailDescriptionLayout.setVisibility(View.VISIBLE);
            bailDescription.setText(confirmSelectedPlan.getConfirmPlanProvidedBail());

            adapter = new BailRecyclerViewAdapter(dbHandler.readPlanBails(planId, selectedPlan.getUserID()), this);
            bailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            bailsRecyclerView.setAdapter(adapter);
        }
        if (!confirmSelectedPlan.getConfirmPlanFacilityDeviationExist().equals("")) {
            facilityDeviationExist.setText(dbHandler.readHouseholdsFromID(confirmSelectedPlan.getConfirmPlanFacilityDeviationExist()));
            if (confirmSelectedPlan.getConfirmPlanFacilityDeviationExist().equals("1")) {
                facilityMarginReasonLayout.setVisibility(View.VISIBLE);
                facilityMarginReason.setText(confirmSelectedPlan.getConfirmPlanFacilityMarginReason());
            }
        }

        receivedFacility.setText(confirmSelectedPlan.getConfirmPlanReceivedFacility());
        monthlyIncome.setText(confirmSelectedPlan.getConfirmPlanMonthlyIncome());
        bankBranch.setText(confirmSelectedPlan.getConfirmPlanBankBranch());
        paymentDate.setText(confirmSelectedPlan.getConfirmPlanPaymentDate());
        dateMargin.setText(confirmSelectedPlan.getConfirmPlanDateMargin());
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
                alertToastMaker("خطا در نمایش مبلغ تسهیلات دریافتی (ریال)");
                receivedFacility.setText("");
            }
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            String formattedString = formatter.format(longval);
            receivedFacilityToText.setText(String.format("%s ریال", formattedString));
        }
        s = monthlyIncome.getText().toString();
        longval = 0L;
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
                alertToastMaker("خطا در نمایش میانگین درآمد ماهیانه (ریال)");
                monthlyIncome.setText("");
            }
            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            String formattedString = formatter.format(longval);
            monthlyIncomeToText.setText(String.format("%s ریال", formattedString));
        }

        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readReceivedFacilitySituationTypesNames());
        receivedFacilitySituation.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readBanksNames());
        bankTitle.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readHouseholdsNames());
        facilityDeviationExist.setAdapter(adapterOfOptions);

        receivedFacilitySituation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                receivedFacilitySituationLayout.setError(null);
                if (dbHandler.readReceivedFacilitySituationTypeID(receivedFacilitySituation.getText().toString()).equals("2")) {
                    notReceivedFacilityReasonLayout.setVisibility(View.VISIBLE);
                } else {
                    notReceivedFacilityReason.setText("");
                    notReceivedFacilityReasonLayout.setVisibility(View.GONE);
                }
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

                        Long longVal;
                        if (originalString.contains(",")) {
                            originalString = originalString.replaceAll(",", "");
                        }
                        longVal = Long.parseLong(originalString);

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longVal);

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
        notReceivedFacilityReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notReceivedFacilityReasonLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        monthlyIncome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                monthlyIncomeLayout.setError(null);
                monthlyIncome.removeTextChangedListener(this);

                try {
                    if (!monthlyIncome.getText().toString().isEmpty()) {
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
                        monthlyIncomeToText.setText(String.format("%s ریال", formattedString));
                        monthlyIncome.setSelection(monthlyIncome.getText().length());
                    } else monthlyIncomeToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                monthlyIncome.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bankTitleLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankBranch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bankBranchLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        paymentDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                paymentDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateMargin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateMarginLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        facilityDeviationExist.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facilityDeviationExistLayout.setError(null);
                if (dbHandler.readHouseholdsID(facilityDeviationExist.getText().toString()).equals("1"))
                    facilityMarginReasonLayout.setVisibility(View.VISIBLE);
                else {
                    facilityMarginReason.setText("");
                    facilityMarginReasonLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        facilityMarginReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                facilityMarginReasonLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancelRequest.setOnClickListener(v -> {
            Intent intent = new Intent(this, PendingToSuperVisionActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
            finish();
        });


        btnChoosePaymentDate.setOnClickListener(v -> {
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
                paymentDate.setText(String.format("%s/%s/%s", planYear, planMonth, planDay));

                paymentDateLayout.setError(null);
                dialog.dismiss();
            });
            cancel.setOnClickListener(v1 -> dialog.cancel());
            dialog.show();
        });

        btnEnterBail.setOnClickListener(v -> {
            Dialog dialog = new Dialog(this);

            dialog.setContentView(R.layout.custom_enter_bail_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);


            Button confirm = dialog.findViewById(R.id.confirm);
            Button cancel = dialog.findViewById(R.id.cancel);
            TextInputLayout bailLayout = dialog.findViewById(R.id.bailLayout);
            AutoCompleteTextView bail = dialog.findViewById(R.id.bail);
            bail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bailLayout.setError(null);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapterOfOptions = new ArrayAdapter<>(this, R.layout.spinner_layout, dbHandler.readPlanBailsNames());
            bail.setAdapter(adapterOfOptions);

            confirm.setOnClickListener(v1 -> {

                if (bail.getText().toString().isEmpty())
                    bailLayout.setError("نوع وثیقه را انتخاب نمایید!");

                if (bailLayout.getError() == null) {
                    bailErrorLayout.setError(null);

                    BailModal bailModal = new BailModal();
                    bailModal.setConfirmPlanID(planId);
                    bailModal.setConfirmPlanBailUserID(selectedPlan.getUserID());
                    //Next line is useless just for comparing
                    bailModal.setConfirmBailID(0);
                    bailModal.setConfirmBailName(dbHandler.readPlanBailsID(bail.getText().toString()));

                    ArrayList<BailModal> bails = dbHandler.comparePlanBails(planId, selectedPlan.getUserID());

                    if (!isContains(bails, bailModal)) {
                        dbHandler.insertPlanBail(bailModal);

                        if (bailsRecyclerView.getVisibility() != View.VISIBLE) {
                            bailsRecyclerView.setVisibility(View.VISIBLE);
                            bailDescriptionLayout.setVisibility(View.VISIBLE);
                        }

                        adapter = new BailRecyclerViewAdapter(dbHandler.readPlanBails(planId, selectedPlan.getUserID()), this);
                        bailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                        bailsRecyclerView.setAdapter(adapter);

                    } else alertToastMaker("این نوع وثیقه قبلاً انتخاب شده است!");

                    dialog.dismiss();
                }
            });

            cancel.setOnClickListener(v1 -> dialog.cancel());

            dialog.show();
        });

        nextStep.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");

            else {
                confirmSelectedPlan.setConfirmPlanReceivedFacilitySituation(dbHandler.readReceivedFacilitySituationTypeID(receivedFacilitySituation.getText().toString()));
                confirmSelectedPlan.setConfirmPlanReceivedFacility(receivedFacility.getText().toString());
                confirmSelectedPlan.setConfirmPlanNotReceivedFacilityReason(notReceivedFacilityReason.getText().toString());
                confirmSelectedPlan.setConfirmPlanProvidedBail(bailDescription.getText().toString());
                confirmSelectedPlan.setConfirmPlanMonthlyIncome(monthlyIncome.getText().toString());
                confirmSelectedPlan.setConfirmPlanBankTitle(dbHandler.readBankID(bankTitle.getText().toString()));
                confirmSelectedPlan.setConfirmPlanBankBranch(bankBranch.getText().toString());
                confirmSelectedPlan.setConfirmPlanPaymentDate(paymentDate.getText().toString());
                confirmSelectedPlan.setConfirmPlanDateMargin(dateMargin.getText().toString());
                confirmSelectedPlan.setConfirmPlanFacilityDeviationExist(dbHandler.readHouseholdsID(facilityDeviationExist.getText().toString()));
                confirmSelectedPlan.setConfirmPlanFacilityMarginReason(facilityMarginReason.getText().toString());

                Bundle extras = getIntent().getExtras();
                String planId = extras.getString("planId");

                dbHandler.updateConfirmPlanFinalSupervisionSituation(confirmSelectedPlan);

                selectedPlan.setWhichStep(whichStep);
                dbHandler.updatePlanInfo(selectedPlan);

                Intent intent = new Intent(this, ConfirmPlanRequirementsActivity.class);
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
        Intent intent = new Intent(ConfirmPlanFacilityActivity.this, ConfirmPlanLicenseActivity.class);
        intent.putExtra("planId", planId);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private boolean checkErrors() {
        boolean retVal = false;

        if (dbHandler.readHouseholdsID(facilityDeviationExist.getText().toString()).equals("1")) {
            if (facilityMarginReason.getText().toString().isEmpty()) {
                facilityMarginReasonLayout.setError("دلایل انحراف از طرح و هزینه نشدن تسهیلات را وارد نمایید!");
                nestedScrollView.smoothScrollTo(0, facilityMarginReasonLayout.getTop() - 10);
                retVal = true;
            }
        }

        if (facilityDeviationExist.getText().toString().isEmpty()) {
            facilityDeviationExistLayout.setError("یک مورد را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, facilityDeviationExistLayout.getTop() - 10);
            retVal = true;
        }


        if (monthlyIncome.getText().toString().isEmpty()) {
            monthlyIncomeLayout.setError("میانگین درآمد ماهیانه را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, monthlyIncomeLayout.getTop() - 10);
            retVal = true;
        }

        if (bailDescription.getText().toString().isEmpty()) {
            bailDescriptionLayout.setError("توضیحات وثیقه ارائه شده را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, bailDescriptionLayout.getTop() - 10);
            retVal = true;
        }

        if (bailsRecyclerView.getVisibility() != View.VISIBLE) {
            bailErrorLayout.setError("وثیقه بانکی ارائه شده را درج نمایید!");
            nestedScrollView.smoothScrollTo(0, bailErrorLayout.getTop() - 10);
            retVal = true;
        }

        if (dateMargin.getText().toString().isEmpty()) {
            dateMarginLayout.setError("فاصله زمانی درخواست تا پرداخت تسهیلات را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, btnChoosePaymentDate.getTop() - 10);
            retVal = true;
        }

        if (!planDay.isEmpty() && !planMonth.isEmpty() && !planYear.isEmpty() && !paymentDate.getText().toString().isEmpty()) {
            if (Integer.parseInt(planMonth) > 6) {
                if (Integer.parseInt(planDay) > 30) {
                    paymentDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planMonth) == 12) {
                PersianDate date1 = new PersianDate();
                date1.setShYear(Integer.parseInt(planYear));
                if (Integer.parseInt(planDay) == 30 && !date1.isLeap()) {
                    paymentDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                    retVal = true;
                }
            }
            if (Integer.parseInt(planYear) == PersianDate.today().getShYear()) {
                if (Integer.parseInt(planMonth) == PersianDate.today().getShMonth()) {
                    if (Integer.parseInt(planDay) > PersianDate.today().getShDay()) {
                        paymentDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                        nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                        retVal = true;
                    }
                } else if (Integer.parseInt(planMonth) > PersianDate.today().getShMonth()) {
                    paymentDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                    nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                    retVal = true;
                }
            } else if (Integer.parseInt(planYear) > PersianDate.today().getShYear()) {
                paymentDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                retVal = true;
            }

            if (Integer.parseInt(planYear) < 1300 || Integer.parseInt(planYear) > 1500) {
                paymentDateLayout.setError("سال را به درستی وارد کنید!");
                if (Integer.parseInt(planMonth) > 12) {
                    paymentDateLayout.setError("ماه را به درستی وارد کنید!");
                    if (Integer.parseInt(planDay) > 31) {
                        paymentDateLayout.setError("روز را به درستی وارد کنید!");
                    }
                }
                nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
                retVal = true;
            }

        }

        if (paymentDate.getText().toString().isEmpty()) {
            paymentDateLayout.setError("تاریخ پرداخت تسهیلات را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, paymentDateLayout.getTop() - 10);
            retVal = true;
        }

        if (bankBranch.getText().toString().isEmpty()) {
            bankBranchLayout.setError("شعبه را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, bankBranchLayout.getTop() - 10);
            retVal = true;
        }

        if (bankTitle.getText().toString().isEmpty()) {
            bankTitleLayout.setError("مؤسسه عامل را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, bankTitleLayout.getTop() - 10);
            retVal = true;
        }

        if (dbHandler.readReceivedFacilitySituationTypeID(receivedFacilitySituation.getText().toString()).equals("2")) {
            if (notReceivedFacilityReason.getText().toString().isEmpty()) {
                notReceivedFacilityReasonLayout.setError("دلیل عدم پرداخت مابقی تسهیلات را وارد نمایید!");
                nestedScrollView.smoothScrollTo(0, notReceivedFacilityReasonLayout.getTop() - 10);
                retVal = true;
            }
        }

        if (receivedFacility.getText().toString().isEmpty()) {
            receivedFacilityLayout.setError("مبلغ تسهیلات دریافتی را وارد نمایید!");
            nestedScrollView.smoothScrollTo(0, receivedFacilityLayout.getTop() - 10);
            retVal = true;
        }

        if (receivedFacilitySituation.getText().toString().isEmpty()) {
            receivedFacilitySituationLayout.setError("وضعیت دریافت تسهیلات را انتخاب نمایید!");
            nestedScrollView.smoothScrollTo(0, receivedFacilitySituationLayout.getTop() - 10);
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
        Toast toast = new Toast(ConfirmPlanFacilityActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private boolean isContains(ArrayList<BailModal> bailList, BailModal bailModal) {

        for (int i = 0; i < bailList.size(); i++) {
            if (bailList.get(i).getConfirmPlanID().equals(bailModal.getConfirmPlanID()) &&
                    bailList.get(i).getConfirmPlanBailUserID().equals(bailModal.getConfirmPlanBailUserID()) &&
                    bailList.get(i).getConfirmBailName().equals(bailModal.getConfirmBailName()))
                return true;
        }
        return false;
    }
}