package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.PendingToSuperVisionRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.CounselingModal;
import ir.ashilethegreat.payesh.DBhandlers.CultivationModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.EducationModal;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.DBhandlers.IdentificationModal;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.DBhandlers.MarketingModal;
import ir.ashilethegreat.payesh.DBhandlers.NotificationModal;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingInfrastructureModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingTechnologyModal;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class PendingToSuperVisionActivity extends AppCompatActivity {

    public static CardView searchedContents, notFound;
    CardView searchLayout;
    public static FloatingActionButton toEnd;
    public static ExtendedFloatingActionButton search;
    Button backToHome, searchStart, searchAgain, searchClose, searchedContentsClose;
    LinearLayout /*searchedContentsPhoneNumberLayout,*/ searchedContentsNationalCodeLayout, searchedContentsFamilyLayout,
            searchedContentsNameLayout;
    TextInputLayout searchNameLayout, searchFamilyLayout, searchNationalCodeLayout;
    EditText searchName, searchFamily, searchNationalCode/*, searchPhoneNumber*/;
    TextView searchedContentsPhoneNumber, searchedContentsNationalCode, searchedContentsFamily, searchedContentsName;
    ArrayList<PlanModal> planList;
    ArrayList<PlanModal> syncPlanList;

    public static RecyclerView pendingToSupperVisionRecyclerView;
    DBHandler dbHandler;
    String userID;
    ImageView back;
    public static CardView offlineLayout;
    PendingToSuperVisionRecyclerViewAdapter adapter;
    LinearLayout syncPlansLayout, getPlansLayout;
    Dialog syncDialog;
    TextView syncProgress;
    TextView syncTotal;
    ProgressBar importProgressBar;
    ImageView sync;

    final int DEFAULT_TIMEOUT = 20000;
    String URLNextPage = "";
    String URLastPage = "";
    RequestQueue requestQueue;
    JSONObject jsonBody;
    //    boolean requestFlag = false;
    int planCount = 0;
    ArrayList<LicenseModal> licenseModalArrayList;
    ArrayList<FacilityModal> facilityModalArrayList;
    ArrayList<CounselingModal> counselingModalArrayList;
    ArrayList<CultivationModal> cultivationModalArrayList;
    ArrayList<EducationModal> educationModalArrayList;
    ArrayList<IdentificationModal> identificationModalArrayList;
    ArrayList<MarketingModal> marketingModalArrayList;
    ArrayList<NotificationModal> notificationModalArrayList;
    ArrayList<ProvidingInfrastructureModal> providingInfrastructureModalArrayList;
    ArrayList<ProvidingTechnologyModal> providingTechnologyModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_to_super_vision);

//        newRequest = findViewById(R.id.newRequest);
        pendingToSupperVisionRecyclerView = findViewById(R.id.pendingToSupperVisionRecyclerView);
        back = findViewById(R.id.back);
        toEnd = findViewById(R.id.toEnd);
        offlineLayout = findViewById(R.id.offlineLayout);
        backToHome = findViewById(R.id.backToHome);
        search = findViewById(R.id.search);
        searchLayout = findViewById(R.id.searchLayout);
        searchClose = findViewById(R.id.searchClose);
        searchName = findViewById(R.id.searchName);
        searchFamily = findViewById(R.id.searchFamily);
        searchNationalCode = findViewById(R.id.searchNationalCode);
        searchNameLayout = findViewById(R.id.searchNameLayout);
        searchFamilyLayout = findViewById(R.id.searchFamilyLayout);
        searchNationalCodeLayout = findViewById(R.id.searchNationalCodeLayout);
//        searchPhoneNumber = findViewById(R.id.searchPhoneNumber);
        searchStart = findViewById(R.id.searchStart);
        searchedContents = findViewById(R.id.searchedContents);
        searchedContentsPhoneNumber = findViewById(R.id.searchedContentsPhoneNumber);
        searchedContentsNationalCode = findViewById(R.id.searchedContentsNationalCode);
        searchedContentsFamily = findViewById(R.id.searchedContentsFamily);
        searchedContentsName = findViewById(R.id.searchedContentsName);
//        searchedContentsPhoneNumberLayout = findViewById(R.id.searchedContentsPhoneNumberLayout);
        searchedContentsNationalCodeLayout = findViewById(R.id.searchedContentsNationalCodeLayout);
        searchedContentsFamilyLayout = findViewById(R.id.searchedContentsFamilyLayout);
        searchedContentsNameLayout = findViewById(R.id.searchedContentsNameLayout);
        searchedContentsClose = findViewById(R.id.searchedContentsClose);
        searchAgain = findViewById(R.id.searchAgain);
        notFound = findViewById(R.id.notFound);
        sync = findViewById(R.id.sync);

        dbHandler = new DBHandler(this);
        requestQueue = Volley.newRequestQueue(this);
        userID = dbHandler.getUser().getId();
        planList = dbHandler.readPlans(userID, 0);

        syncPlanList = new ArrayList<>();
        licenseModalArrayList = new ArrayList<>();
        facilityModalArrayList = new ArrayList<>();
        counselingModalArrayList = new ArrayList<>();
        cultivationModalArrayList = new ArrayList<>();
        educationModalArrayList = new ArrayList<>();
        identificationModalArrayList = new ArrayList<>();
        marketingModalArrayList = new ArrayList<>();
        notificationModalArrayList = new ArrayList<>();
        providingInfrastructureModalArrayList = new ArrayList<>();
        providingTechnologyModalArrayList = new ArrayList<>();

        if (planList.size() == 0) {
            offlineLayout.setVisibility(View.VISIBLE);
            pendingToSupperVisionRecyclerView.setVisibility(View.INVISIBLE);
            search.hide();
            toEnd.setVisibility(View.INVISIBLE);
            notFound.setVisibility(View.GONE);
        } else {
            offlineLayout.setVisibility(View.INVISIBLE);
            search.show();
            pendingToSupperVisionRecyclerView.setVisibility(View.VISIBLE);
            toEnd.setVisibility(View.VISIBLE);
            notFound.setVisibility(View.GONE);
        }

        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchFamily.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFamilyLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchNationalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNationalCodeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new PendingToSuperVisionRecyclerViewAdapter(planList, this);
        pendingToSupperVisionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        pendingToSupperVisionRecyclerView.setAdapter(adapter);

        pendingToSupperVisionRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    toEnd.show();
                    search.hide();
                } else if (dy < 0) {
                    toEnd.hide();
                    if (searchLayout.getVisibility() == View.INVISIBLE && offlineLayout.getVisibility() != View.VISIBLE)
                        search.show();
                }
                if (!pendingToSupperVisionRecyclerView.canScrollVertically(1) && planList.size() > 0) {
                    toEnd.hide();
                }
            }
        });

        toEnd.setOnClickListener(v -> pendingToSupperVisionRecyclerView.smoothScrollToPosition(0));

        back.setOnClickListener(v -> onBackPressed());
        backToHome.setOnClickListener(v -> onBackPressed());

        search.setOnClickListener(v -> {
            searchLayout.setVisibility(View.VISIBLE);
//            searchedContents.setVisibility(View.GONE);
            search.hide();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        searchStart.setOnClickListener(v -> {
            if (searchName.getText().toString().isEmpty() && searchFamily.getText().toString().isEmpty() &&
                    searchNationalCode.getText().toString().isEmpty() /*&& searchPhoneNumber.getText().toString().isEmpty()*/)
                alertToastMaker("حداقل یک مورد را وارد کنید!");
            else if (checkErrors()) {
                alertToastMaker("خطاهای جستجو را بررسی کنید!");
            } else {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                searchedContentsNameLayout.setVisibility(View.GONE);
                searchedContentsFamilyLayout.setVisibility(View.GONE);
                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                String searchedNationalCode = "", searchedName = "", searchedFamily = "";
                planList = new ArrayList<>();

                if (!searchName.getText().toString().isEmpty()) {
                    searchedContentsNameLayout.setVisibility(View.VISIBLE);
                    searchedName = searchName.getText().toString();
                    searchedContentsName.setText(searchedName);

                }
                if (!searchFamily.getText().toString().isEmpty()) {
                    searchedContentsFamilyLayout.setVisibility(View.VISIBLE);
                    searchedFamily = searchFamily.getText().toString();
                    searchedContentsFamily.setText(searchedFamily);

                }
                if (!searchNationalCode.getText().toString().isEmpty()) {
                    searchedContentsNationalCodeLayout.setVisibility(View.VISIBLE);
                    searchedNationalCode = searchNationalCode.getText().toString();
                    searchedContentsNationalCode.setText(searchedNationalCode);

                }

                planList = dbHandler.loadSearchedPlans(searchedName, searchedFamily, searchedNationalCode, userID, 0);
//                shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
//                shimmerFrameLayoutPlaceHolder.startShimmer();
                pendingToSupperVisionRecyclerView.setVisibility(View.INVISIBLE);
                notFound.setVisibility(View.GONE);
                search.show();
//                searchedContentsNameLayout.setVisibility(View.GONE);
//                searchedContentsFamilyLayout.setVisibility(View.GONE);
//                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                searchedContents.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.INVISIBLE);

                if (planList.size() == 0) {
                    pendingToSupperVisionRecyclerView.setVisibility(View.GONE);
                    search.hide();
                    notFound.setVisibility(View.VISIBLE);
                    toEnd.hide();
                } else {
                    adapter = new PendingToSuperVisionRecyclerViewAdapter(planList, this);
                    pendingToSupperVisionRecyclerView.setAdapter(adapter);
                    pendingToSupperVisionRecyclerView.setVisibility(View.VISIBLE);
                    search.show();
                    notFound.setVisibility(View.GONE);
                    if (pendingToSupperVisionRecyclerView.canScrollVertically(1)) {
                        toEnd.show();
                    }
                }
            }
        });

        searchClose.setOnClickListener(v -> {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            searchLayout.setVisibility(View.INVISIBLE);
            if (notFound.getVisibility() == View.VISIBLE)
                search.hide();
            else search.show();
        });

        searchAgain.setOnClickListener(v -> search.performClick());

        searchedContentsClose.setOnClickListener(v -> {
            searchedContents.setVisibility(View.GONE);
            notFound.setVisibility(View.GONE);
            pendingToSupperVisionRecyclerView.setVisibility(View.VISIBLE);

            planList = dbHandler.readPlans(userID, 0);
            adapter = new PendingToSuperVisionRecyclerViewAdapter(planList, this);
            pendingToSupperVisionRecyclerView.setAdapter(adapter);
            search.show();
            if (pendingToSupperVisionRecyclerView.canScrollVertically(1)) {
                toEnd.show();
            }

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        sync.setOnClickListener(v -> {
            jsonBody = new JSONObject();
            syncPlans(jsonBody);
        });
    }

    @Override
    public void onBackPressed() {
        if (searchLayout.getVisibility() == View.VISIBLE)
            searchClose.performClick();
        else if (searchedContents.getVisibility() == View.VISIBLE)
            searchedContentsClose.performClick();
        else {
            super.onBackPressed();
            Intent intent = new Intent(PendingToSuperVisionActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    private boolean checkErrors() {
        boolean retVal = false;
        if (!searchName.getText().toString().isEmpty()) {
            if (searchName.getText().toString().length() < 2) {
                searchNameLayout.setError("نام نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchFamily.getText().toString().isEmpty()) {
            if (searchFamily.getText().toString().length() < 2) {
                searchFamilyLayout.setError("نام خانوادگی نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchNationalCode.getText().toString().isEmpty()) {
            if (searchNationalCode.getText().toString().length() < 10) {
                searchNationalCodeLayout.setError("کد ملی باید 10 رقم باشد!");
                retVal = true;
            }
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
        Toast toast = new Toast(PendingToSuperVisionActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(PendingToSuperVisionActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void syncPlans(JSONObject jsonBody) {
        syncDialog = new Dialog(this);
        syncDialog.setContentView(R.layout.custom_pending_to_super_vision_import_in_progress_layout);
        Objects.requireNonNull(syncDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        syncDialog.setCanceledOnTouchOutside(false);

        syncProgress = syncDialog.findViewById(R.id.syncProgress);
        syncTotal = syncDialog.findViewById(R.id.syncTotal);
        importProgressBar = syncDialog.findViewById(R.id.importProgressBar);
        getPlansLayout = syncDialog.findViewById(R.id.getPlansLayout);
        syncPlansLayout = syncDialog.findViewById(R.id.syncPlansLayout);

        Objects.requireNonNull(syncProgress).setText("0");

        planList = new ArrayList<>();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLManagement), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response.getString("status").equals("1")) {
//                            requestFlag = true;
                            JSONObject body = new JSONObject(response.getString("data"));
                            URLNextPage = body.getString("next_page_url");
                            URLastPage = body.getString("last_page_url");
                            planCount = Integer.parseInt(body.getString("total"));

                            Objects.requireNonNull(importProgressBar).setMax(planCount);
                            Objects.requireNonNull(syncTotal).setText(String.valueOf(planCount));

                            JSONArray jsonArray = body.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    importProgressBar.setProgress(Integer.parseInt(syncProgress.getText().toString()));
                                    Objects.requireNonNull(syncProgress).setText(String.valueOf(Integer.parseInt(syncProgress.getText().toString()) + 1));

                                    PlanModal plan = new PlanModal();
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    plan.setPlanID(data.getString("id"));
                                    plan.setEmployerID(data.getString("id_karfarma"));

                                    JSONObject location = data.getJSONObject("location_api");
                                    if (!location.getString("id_state").equalsIgnoreCase("null"))
                                        plan.setProvince(location.getString("id_state"));
                                    else plan.setProvince("");
                                    if (!location.getString("id_county").equalsIgnoreCase("null"))
                                        plan.setTownship(location.getString("id_county"));
                                    else plan.setTownship("");
                                    if (!location.getString("id_part").equalsIgnoreCase("null"))
                                        plan.setSection(location.getString("id_part"));
                                    else plan.setSection("");
                                     /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setCity("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setRegion("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setMainStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setSubStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setAlley("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setCode("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setPostalCode("");
                                    if (!location.getString("address").equalsIgnoreCase("null"))
                                        plan.setMoreAddress(location.getString("address"));
                                    else plan.setMoreAddress("");

                                    JSONObject employerArray = data.getJSONObject("karfarma");
                                    plan.setName(employerArray.getString("name"));
                                    plan.setFamilyName(employerArray.getString("family"));
                                    plan.setParentName(employerArray.getString("father"));
                                    plan.setNatCode(employerArray.getString("national_code"));
                                    plan.setBirthday(employerArray.getString("birth_date"));

                                    if (!employerArray.getString("phone").equalsIgnoreCase("null"))
                                        plan.setPhone(employerArray.getString("phone"));
                                    else plan.setPhone("");
                                    if (!employerArray.getString("tel").equalsIgnoreCase("null"))
                                        plan.setFixedPhone(employerArray.getString("tel"));
                                    else plan.setFixedPhone("");
                                    if (!employerArray.getString("id_degrees").equalsIgnoreCase("null"))
                                        plan.setEducation(employerArray.getString("id_degrees"));
                                    else plan.setEducation("");
                                    if (!employerArray.getString("id_fields").equalsIgnoreCase("null"))
                                        plan.setFieldOfStudy(employerArray.getString("id_fields"));
                                    else plan.setFieldOfStudy("");
                                    if (!employerArray.getString("id_proficiency").equalsIgnoreCase("null"))
                                        plan.setSpecialization(employerArray.getString("id_proficiency"));
                                    else plan.setSpecialization("");
                                    if (!employerArray.getString("id_supervisor").equalsIgnoreCase("null"))
                                        plan.setHouseholds(employerArray.getString("id_supervisor"));
                                    else plan.setHouseholds("");

                                    JSONObject planDetails = data.getJSONObject("tarh_api");
                                    if (!planDetails.getString("id_title_plan").equalsIgnoreCase("null"))
                                        plan.setPlanTitle(planDetails.getString("id_title_plan"));
                                    else plan.setPlanTitle(planDetails.getString(""));
                                    if (!planDetails.getString("date_activity").equalsIgnoreCase("null"))
                                        plan.setPlanStartDate(planDetails.getString("date_activity"));
                                    else plan.setPlanStartDate(planDetails.getString(""));
                                    if (!planDetails.getString("id_type_activity").equalsIgnoreCase("null"))
                                        plan.setPlanEconomicSection(planDetails.getString("id_type_activity"));
                                    else plan.setPlanEconomicSection(planDetails.getString(""));
                                    if (!planDetails.getString("id_activity_status").equalsIgnoreCase("null"))
                                        plan.setPlanSituation(planDetails.getString("id_activity_status"));
                                    else plan.setPlanSituation(planDetails.getString(""));

                                    JSONArray financing = data.getJSONArray("financing");
                                    if (financing.length() != 0) {
                                        plan.setPlanEchoSystemFinance("1");
                                        for (int j = 0; j < financing.length(); j++) {
                                            JSONObject financeData = financing.getJSONObject(j);
                                            FacilityModal facilityModal = new FacilityModal();
                                            facilityModal.setPlanID(data.getString("id"));

                                            facilityModal.setFinanceID(financeData.getString("id"));
                                            facilityModal.setOwnerID(financeData.getString("id_karfarma"));
                                            facilityModal.setOrganID(financeData.getString("id_executive_device"));
                                            facilityModal.setTotalFacilitiesAmount(financeData.getString("money"));
                                            facilityModal.setContributedFacilitiesAmount(financeData.getString("brought_person"));
                                            facilityModal.setSupplyFacilitiesID(financeData.getString("id_location_of_credit"));
                                            facilityModal.setDateFacilities(financeData.getString("id_location_of_credit_date"));
                                            facilityModal.setShareFacilitiesAmount(financeData.getString("loan"));
                                            facilityModal.setFixedFacilityAmount(financeData.getString("fixed_capital"));
                                            facilityModal.setFixedBankID(financeData.getString("id_bank_fixed_capital"));
                                            facilityModal.setWorkingFacilitiesAmount(financeData.getString("working_capital"));
                                            facilityModal.setWorkingBankID(financeData.getString("id_bank_working_capital"));

                                            facilityModalArrayList.add(facilityModal);
                                        }
                                    } else plan.setPlanEchoSystemFinance("0");

                                    JSONArray licensing = data.getJSONArray("license");
                                    if (licensing.length() != 0) {
                                        plan.setPlanEchoSystemLicence("1");
                                        for (int j = 0; j < licensing.length(); j++) {
                                            JSONObject financeData = licensing.getJSONObject(j);
                                            LicenseModal licenseModal = new LicenseModal();
                                            licenseModal.setPlanID(data.getString("id"));
                                            licenseModal.setLicenseID(financeData.getString("id"));
                                            licenseModal.setOwnerID(financeData.getString("id_karfarma"));
                                            licenseModal.setOrganID(financeData.getString("id_executive_device"));
                                            licenseModal.setLicenseType(financeData.getString("id_type_license"));
                                            licenseModal.setLicenseNumber(financeData.getString("license_number"));
                                            licenseModal.setLicenseDate(financeData.getString("date_license_number"));

                                            licenseModalArrayList.add(licenseModal);
                                        }
                                    } else plan.setPlanEchoSystemLicence("0");

                                    JSONArray marketing = data.getJSONArray("marketing");
                                    if (marketing.length() != 0) {
                                        plan.setPlanEchoSystemMarketing("1");
                                        for (int j = 0; j < marketing.length(); j++) {
                                            JSONObject marketingData = marketing.getJSONObject(j);
                                            MarketingModal marketingModal = new MarketingModal();
                                            marketingModal.setOwnerID(data.getString("id_karfarma"));
                                            marketingModal.setOrganID(marketingData.getString("id_executive_device"));
                                            marketingModal.setPlanID(data.getString("id"));

                                            marketingModalArrayList.add(marketingModal);
                                        }
                                    } else plan.setPlanEchoSystemMarketing("0");

                                    JSONArray technologySupply = data.getJSONArray("technology_supply");
                                    if (technologySupply.length() != 0) {
                                        plan.setPlanEchoSystemProvidingTechnology("1");
                                        for (int j = 0; j < technologySupply.length(); j++) {
                                            JSONObject technologySupplyData = technologySupply.getJSONObject(j);

                                            ProvidingTechnologyModal providingTechnologyModal = new ProvidingTechnologyModal();
                                            providingTechnologyModal.setOwnerID(data.getString("id_karfarma"));
                                            providingTechnologyModal.setOrganID(technologySupplyData.getString("id_executive_device"));
                                            providingTechnologyModal.setPlanID(data.getString("id"));

                                            providingTechnologyModalArrayList.add(providingTechnologyModal);
                                        }
                                    } else plan.setPlanEchoSystemProvidingTechnology("0");

                                    JSONArray providingInfrastructure = data.getJSONArray("providing_infrastructure");
                                    if (providingInfrastructure.length() != 0) {
                                        plan.setPlanEchoSystemProvidingInfrastructure("1");

                                        for (int j = 0; j < providingInfrastructure.length(); j++) {
                                            JSONObject providingInfrastructureData = providingInfrastructure.getJSONObject(j);

                                            ProvidingInfrastructureModal providingInfrastructureModal = new ProvidingInfrastructureModal();
                                            providingInfrastructureModal.setOwnerID(data.getString("id_karfarma"));
                                            providingInfrastructureModal.setOrganID(providingInfrastructureData.getString("id_executive_device"));
                                            providingInfrastructureModal.setPlanID(data.getString("id"));

                                            providingInfrastructureModalArrayList.add(providingInfrastructureModal);
                                        }
                                    } else plan.setPlanEchoSystemProvidingInfrastructure("0");

                                    JSONArray identification = data.getJSONArray("identity");
                                    if (identification.length() != 0) {
                                        plan.setPlanEchoSystemIdentification("1");

                                        for (int j = 0; j < identification.length(); j++) {
                                            JSONObject identificationData = identification.getJSONObject(j);

                                            IdentificationModal identificationModal = new IdentificationModal();
                                            identificationModal.setOwnerID(data.getString("id_karfarma"));
                                            identificationModal.setOrganID(identificationData.getString("id_executive_device"));
                                            identificationModal.setPlanID(data.getString("id"));

                                            identificationModalArrayList.add(identificationModal);
                                        }
                                    } else plan.setPlanEchoSystemIdentification("0");

                                    JSONArray consulting = data.getJSONArray("consulting");
                                    if (consulting.length() != 0) {
                                        plan.setPlanEchoSystemCounseling("1");

                                        for (int j = 0; j < consulting.length(); j++) {
                                            JSONObject consultingData = consulting.getJSONObject(j);

                                            CounselingModal counselingModal = new CounselingModal();
                                            counselingModal.setOwnerID(data.getString("id_karfarma"));
                                            counselingModal.setOrganID(consultingData.getString("id_executive_device"));
                                            counselingModal.setPlanID(data.getString("id"));

                                            counselingModalArrayList.add(counselingModal);
                                        }
                                    } else plan.setPlanEchoSystemCounseling("0");

                                    JSONArray cultivation = data.getJSONArray("culture_building");
                                    if (cultivation.length() != 0) {
                                        plan.setPlanEchoSystemCultivation("1");

                                        for (int j = 0; j < cultivation.length(); j++) {
                                            JSONObject cultivationData = cultivation.getJSONObject(j);

                                            CultivationModal cultivationModal = new CultivationModal();
                                            cultivationModal.setOwnerID(data.getString("id_karfarma"));
                                            cultivationModal.setOrganID(cultivationData.getString("id_executive_device"));
                                            cultivationModal.setPlanID(data.getString("id"));

                                            cultivationModalArrayList.add(cultivationModal);
                                        }
                                    } else plan.setPlanEchoSystemCultivation("0");

                                    JSONArray notice = data.getJSONArray("notices");
                                    if (notice.length() != 0) {
                                        plan.setPlanEchoSystemNotices("1");

                                        for (int j = 0; j < notice.length(); j++) {
                                            JSONObject noticeData = notice.getJSONObject(j);

                                            NotificationModal notificationModal = new NotificationModal();
                                            notificationModal.setOwnerID(data.getString("id_karfarma"));
                                            notificationModal.setOrganID(noticeData.getString("id_executive_device"));
                                            notificationModal.setPlanID(data.getString("id"));

                                            notificationModalArrayList.add(notificationModal);
                                        }
                                    } else plan.setPlanEchoSystemNotices("0");

                                    JSONArray education = data.getJSONArray("education");
                                    if (education.length() != 0) {
                                        plan.setPlanEchoSystemEducation("1");

                                        for (int j = 0; j < education.length(); j++) {
                                            JSONObject educationData = education.getJSONObject(j);

                                            EducationModal educationModal = new EducationModal();
                                            educationModal.setOwnerID(data.getString("id_karfarma"));
                                            educationModal.setOrganID(educationData.getString("id_executive_device"));
                                            educationModal.setPlanID(data.getString("id"));

                                            educationModalArrayList.add(educationModal);
                                        }
                                    } else plan.setPlanEchoSystemEducation("0");

                                    plan.setIsCompleted(0);
                                    plan.setWhichStep(0);
                                    plan.setUserID(userID);
                                    plan.setPlanFinalSuperVisionSituation(0);

                                    syncPlanList.add(plan);

//                                    dbHandler.putPlanForSupperVision(plan);

                                }
                                if (!URLNextPage.equals("null")) {
                                    syncMore(jsonBody);
                                } else {
//                                    syncDialog.dismiss();
//                                    recreate();
                                    databaseSync();
                                }

                                search.show();
                            }
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس دریافت طرح های ورودی");
                        e.printStackTrace();
                    }
                },
                error -> {
                    errorToastMaker("ارتباط با سرور برقرار نشد!\nارتباط اینترنتی خود را بررسی کنید!");
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                UserModal user = dbHandler.getUser();
                headers.put("Authorization", "Bearer " + user.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        syncDialog.show();
    }

    private void syncMore(JSONObject jsonBody) {
        JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, URLNextPage, jsonBody,
                response1 -> {
                    try {
                        if (response1.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response1.getString("status").equals("1")) {
//                            requestFlag = true;

                            JSONObject body = new JSONObject(response1.getString("data"));
                            URLNextPage = body.getString("next_page_url");
                            URLastPage = body.getString("last_page_url");

                            JSONArray jsonArray = body.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    PlanModal plan = new PlanModal();
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    Objects.requireNonNull(syncProgress).setText(String.valueOf(Integer.parseInt(syncProgress.getText().toString()) + 1));
                                    importProgressBar.setProgress(Integer.parseInt(syncProgress.getText().toString()));

                                    plan.setPlanID(data.getString("id"));
                                    plan.setEmployerID(data.getString("id_karfarma"));
//
                                    JSONObject location = data.getJSONObject("location_api");
                                    if (!location.getString("id_state").equalsIgnoreCase("null"))
                                        plan.setProvince(location.getString("id_state"));
                                    else plan.setProvince("");
                                    if (!location.getString("id_county").equalsIgnoreCase("null"))
                                        plan.setTownship(location.getString("id_county"));
                                    else plan.setTownship("");
                                    if (!location.getString("id_part").equalsIgnoreCase("null"))
                                        plan.setSection(location.getString("id_part"));
                                    else plan.setSection("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setCity("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setRegion("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setMainStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setSubStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setAlley("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setCode("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/
                                    plan.setPostalCode("");
                                    if (!location.getString("address").equalsIgnoreCase("null"))
                                        plan.setMoreAddress(location.getString("address"));
                                    else plan.setMoreAddress("");
//
                                    JSONObject employerArray = data.getJSONObject("karfarma");
                                    plan.setName(employerArray.getString("name"));
                                    plan.setFamilyName(employerArray.getString("family"));
                                    plan.setParentName(employerArray.getString("father"));
                                    plan.setNatCode(employerArray.getString("national_code"));
                                    plan.setBirthday(employerArray.getString("birth_date"));

                                    if (!employerArray.getString("phone").equalsIgnoreCase("null"))
                                        plan.setPhone(employerArray.getString("phone"));
                                    else plan.setPhone("");
                                    if (!employerArray.getString("tel").equalsIgnoreCase("null"))
                                        plan.setFixedPhone(employerArray.getString("tel"));
                                    else plan.setFixedPhone("");
                                    if (!employerArray.getString("id_degrees").equalsIgnoreCase("null"))
                                        plan.setEducation(employerArray.getString("id_degrees"));
                                    else plan.setEducation("");
                                    if (!employerArray.getString("id_fields").equalsIgnoreCase("null"))
                                        plan.setFieldOfStudy(employerArray.getString("id_fields"));
                                    else plan.setFieldOfStudy("");
                                    if (!employerArray.getString("id_proficiency").equalsIgnoreCase("null"))
                                        plan.setSpecialization(employerArray.getString("id_proficiency"));
                                    else plan.setSpecialization("");
                                    if (!employerArray.getString("id_supervisor").equalsIgnoreCase("null"))
                                        plan.setHouseholds(employerArray.getString("id_supervisor"));
                                    else plan.setHouseholds("");

                                    JSONObject planDetails = data.getJSONObject("tarh_api");
                                    if (!planDetails.getString("id_title_plan").equalsIgnoreCase("null"))
                                        plan.setPlanTitle(planDetails.getString("id_title_plan"));
                                    else
                                        plan.setPlanTitle(planDetails.getString(""));
                                    if (!planDetails.getString("date_activity").equalsIgnoreCase("null"))
                                        plan.setPlanStartDate(planDetails.getString("date_activity"));
                                    else
                                        plan.setPlanStartDate(planDetails.getString(""));
                                    if (!planDetails.getString("id_type_activity").equalsIgnoreCase("null"))
                                        plan.setPlanEconomicSection(planDetails.getString("id_type_activity"));
                                    else
                                        plan.setPlanEconomicSection(planDetails.getString(""));
                                    if (!planDetails.getString("id_activity_status").equalsIgnoreCase("null"))
                                        plan.setPlanSituation(planDetails.getString("id_activity_status"));
                                    else
                                        plan.setPlanSituation(planDetails.getString(""));

                                    JSONArray financing = data.getJSONArray("financing");
                                    if (financing.length() != 0) {
                                        plan.setPlanEchoSystemFinance("1");
                                        for (int j = 0; j < financing.length(); j++) {
                                            JSONObject financeData = financing.getJSONObject(j);
                                            FacilityModal facilityModal = new FacilityModal();
                                            facilityModal.setPlanID(data.getString("id"));

                                            facilityModal.setFinanceID(financeData.getString("id"));
                                            facilityModal.setOwnerID(financeData.getString("id_karfarma"));
                                            facilityModal.setOrganID(financeData.getString("id_executive_device"));
                                            facilityModal.setTotalFacilitiesAmount(financeData.getString("money"));
                                            facilityModal.setContributedFacilitiesAmount(financeData.getString("brought_person"));
                                            facilityModal.setSupplyFacilitiesID(financeData.getString("id_location_of_credit"));
                                            facilityModal.setDateFacilities(financeData.getString("id_location_of_credit_date"));
                                            facilityModal.setShareFacilitiesAmount(financeData.getString("loan"));
                                            facilityModal.setFixedFacilityAmount(financeData.getString("fixed_capital"));
                                            facilityModal.setFixedBankID(financeData.getString("id_bank_fixed_capital"));
                                            facilityModal.setWorkingFacilitiesAmount(financeData.getString("working_capital"));
                                            facilityModal.setWorkingBankID(financeData.getString("id_bank_working_capital"));

                                            facilityModalArrayList.add(facilityModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemFinance("0");

                                    JSONArray licensing = data.getJSONArray("license");
                                    if (licensing.length() != 0) {
                                        plan.setPlanEchoSystemLicence("1");
                                        for (int j = 0; j < licensing.length(); j++) {
                                            JSONObject financeData = licensing.getJSONObject(j);
                                            LicenseModal licenseModal = new LicenseModal();
                                            licenseModal.setPlanID(data.getString("id"));
                                            licenseModal.setLicenseID(financeData.getString("id"));
                                            licenseModal.setOwnerID(financeData.getString("id_karfarma"));
                                            licenseModal.setOrganID(financeData.getString("id_executive_device"));
                                            licenseModal.setLicenseType(financeData.getString("id_type_license"));
                                            licenseModal.setLicenseNumber(financeData.getString("license_number"));
                                            licenseModal.setLicenseDate(financeData.getString("date_license_number"));


                                            licenseModalArrayList.add(licenseModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemLicence("0");

                                    JSONArray marketing = data.getJSONArray("marketing");
                                    if (marketing.length() != 0) {
                                        plan.setPlanEchoSystemMarketing("1");
                                        for (int j = 0; j < marketing.length(); j++) {
                                            JSONObject marketingData = marketing.getJSONObject(j);
                                            MarketingModal marketingModal = new MarketingModal();
                                            marketingModal.setOwnerID(data.getString("id_karfarma"));
                                            marketingModal.setOrganID(marketingData.getString("id_executive_device"));
                                            marketingModal.setPlanID(data.getString("id"));

                                            marketingModalArrayList.add(marketingModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemMarketing("0");

                                    JSONArray technologySupply = data.getJSONArray("technology_supply");
                                    if (technologySupply.length() != 0) {
                                        plan.setPlanEchoSystemProvidingTechnology("1");
                                        for (int j = 0; j < technologySupply.length(); j++) {
                                            JSONObject technologySupplyData = technologySupply.getJSONObject(j);

                                            ProvidingTechnologyModal providingTechnologyModal = new ProvidingTechnologyModal();
                                            providingTechnologyModal.setOwnerID(data.getString("id_karfarma"));
                                            providingTechnologyModal.setOrganID(technologySupplyData.getString("id_executive_device"));
                                            providingTechnologyModal.setPlanID(data.getString("id"));

                                            providingTechnologyModalArrayList.add(providingTechnologyModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemProvidingTechnology("0");

                                    JSONArray providingInfrastructure = data.getJSONArray("providing_infrastructure");
                                    if (providingInfrastructure.length() != 0) {
                                        plan.setPlanEchoSystemProvidingInfrastructure("1");

                                        for (int j = 0; j < providingInfrastructure.length(); j++) {
                                            JSONObject providingInfrastructureData = providingInfrastructure.getJSONObject(j);

                                            ProvidingInfrastructureModal providingInfrastructureModal = new ProvidingInfrastructureModal();
                                            providingInfrastructureModal.setOwnerID(data.getString("id_karfarma"));
                                            providingInfrastructureModal.setOrganID(providingInfrastructureData.getString("id_executive_device"));
                                            providingInfrastructureModal.setPlanID(data.getString("id"));

                                            providingInfrastructureModalArrayList.add(providingInfrastructureModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemProvidingInfrastructure("0");

                                    JSONArray identification = data.getJSONArray("identity");
                                    if (identification.length() != 0) {
                                        plan.setPlanEchoSystemIdentification("1");

                                        for (int j = 0; j < identification.length(); j++) {
                                            JSONObject identificationData = identification.getJSONObject(j);

                                            IdentificationModal identificationModal = new IdentificationModal();
                                            identificationModal.setOwnerID(data.getString("id_karfarma"));
                                            identificationModal.setOrganID(identificationData.getString("id_executive_device"));
                                            identificationModal.setPlanID(data.getString("id"));

                                            identificationModalArrayList.add(identificationModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemIdentification("0");

                                    JSONArray consulting = data.getJSONArray("consulting");
                                    if (consulting.length() != 0) {
                                        plan.setPlanEchoSystemCounseling("1");

                                        for (int j = 0; j < consulting.length(); j++) {
                                            JSONObject consultingData = consulting.getJSONObject(j);

                                            CounselingModal counselingModal = new CounselingModal();
                                            counselingModal.setOwnerID(data.getString("id_karfarma"));
                                            counselingModal.setOrganID(consultingData.getString("id_executive_device"));
                                            counselingModal.setPlanID(data.getString("id"));

                                            counselingModalArrayList.add(counselingModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemCounseling("0");

                                    JSONArray cultivation = data.getJSONArray("culture_building");
                                    if (cultivation.length() != 0) {
                                        plan.setPlanEchoSystemCultivation("1");

                                        for (int j = 0; j < cultivation.length(); j++) {
                                            JSONObject cultivationData = cultivation.getJSONObject(j);

                                            CultivationModal cultivationModal = new CultivationModal();
                                            cultivationModal.setOwnerID(data.getString("id_karfarma"));
                                            cultivationModal.setOrganID(cultivationData.getString("id_executive_device"));
                                            cultivationModal.setPlanID(data.getString("id"));

                                            cultivationModalArrayList.add(cultivationModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemCultivation("0");

                                    JSONArray notice = data.getJSONArray("notices");
                                    if (notice.length() != 0) {
                                        plan.setPlanEchoSystemNotices("1");

                                        for (int j = 0; j < notice.length(); j++) {
                                            JSONObject noticeData = notice.getJSONObject(j);

                                            NotificationModal notificationModal = new NotificationModal();
                                            notificationModal.setOwnerID(data.getString("id_karfarma"));
                                            notificationModal.setOrganID(noticeData.getString("id_executive_device"));
                                            notificationModal.setPlanID(data.getString("id"));

                                            notificationModalArrayList.add(notificationModal);
                                        }
                                    } else
                                        plan.setPlanEchoSystemNotices("0");

                                    plan.setIsCompleted(0);
                                    plan.setWhichStep(0);
                                    plan.setUserID(userID);
                                    plan.setPlanFinalSuperVisionSituation(0);

                                    syncPlanList.add(plan);
//                                    dbHandler.putPlanForSupperVision(plan);
                                }
                                if (!URLNextPage.equals("null")) {
                                    syncMore(jsonBody);
                                } else {
//                                    syncDialog.dismiss();
//                                    recreate();
                                    databaseSync();
                                }
                            }

                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای به روزسانی لیست طرح ها");
                        e.printStackTrace();
                    }
                },
                error -> {
                    errorToastMaker("خطای سرویس طرح های ورودی");
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                UserModal user = dbHandler.getUser();
                headers.put("Authorization", "Bearer " + user.getToken());
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };
        requestQueue.add(jsonRequest1);
        jsonRequest1.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    private void databaseSync() {
        syncPlansLayout.setVisibility(View.VISIBLE);
        getPlansLayout.setVisibility(View.GONE);
        for (int i = 0; i < planList.size(); i++) {
            for (int j = 0; j < syncPlanList.size(); j++) {
                if (planList.get(i).getPlanID().equals(syncPlanList.get(j).getPlanID())) {
                    syncPlanList.remove(j);
                    break;
                }
            }
        }

    }

    private void refreshAccessToken() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", dbHandler.getUser().getUsername());
            jsonBody.put("password", dbHandler.getUser().getPassword());

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
                recreate();
            } catch (JSONException e) {
                // this will never happen but if so, show error to user.
            }
        }, error -> {
            // show error to user. refresh failed.
            Log.e("test", new String(error.networkResponse.data));

        });
        requestQueue.add(refreshTokenRequest);
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        for (int i = 0; i < planList.size(); i++) {
//            if (!planList.get(i).getPlanPinnedPosition().equals("-1")) {
//                planList.get(i).setPlanPinnedPosition(i + "");
//                dbHandler.updatePlanInfo(planList.get(i));
//            }
//        }
//    }
}