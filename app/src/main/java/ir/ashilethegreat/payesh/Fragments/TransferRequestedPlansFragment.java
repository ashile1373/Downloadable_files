package ir.ashilethegreat.payesh.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.ashilethegreat.payesh.Activities.HomeActivity;
import ir.ashilethegreat.payesh.Activities.LoginActivity;
import ir.ashilethegreat.payesh.Adapters.TransferRequestedPlansRecyclerViewAdapter;
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

public class TransferRequestedPlansFragment extends Fragment {

    boolean requestFlag = false;
    ImageView back;
    RecyclerView transferPlansRecyclerView;
    ArrayList<PlanModal> planList;
    ArrayList<UserModal> senderUserList;
    ArrayList<UserModal> receiverUserList;
    ArrayList<EducationModal> educationModalArrayList;
    ArrayList<FacilityModal> facilityModalArrayList;
    ArrayList<LicenseModal> licenseModalArrayList;
    ArrayList<CounselingModal> counselingModalArrayList;
    ArrayList<MarketingModal> marketingModalArrayList;
    ArrayList<ProvidingInfrastructureModal> providingInfrastructureModalArrayList;
    ArrayList<ProvidingTechnologyModal> providingTechnologyModalArrayList;
    ArrayList<CultivationModal> cultivationModalArrayList;
    ArrayList<NotificationModal> notificationModalArrayList;
    ArrayList<IdentificationModal> identificationModalArrayList;
    ShimmerFrameLayout shimmerFrameLayoutPlaceHolder;
    CardView offlineLayout, noServiceLayout, searchLayout;
    public static CardView allDone, searchedContents, notFound;
    FloatingActionButton toEnd;
    public static ExtendedFloatingActionButton search;
    TransferRequestedPlansRecyclerViewAdapter adapter;
    Button backToLogin, backToHome, backToHome2, searchStart, searchAgain, searchClose, searchedContentsClose;
    LinearLayout /*searchedContentsPhoneNumberLayout,*/ searchedContentsNationalCodeLayout, searchedContentsFamilyLayout,
            searchedContentsNameLayout;
    TextInputLayout searchNameLayout, searchFamilyLayout, searchNationalCodeLayout;
    EditText searchName, searchFamily, searchNationalCode/*, searchPhoneNumber*/;
    TextView /*searchedContentsPhoneNumber,*/ searchedContentsNationalCode, searchedContentsFamily, searchedContentsName;

    DBHandler dbHandler;
    String userID;
    final int DEFAULT_TIMEOUT = 20000;
    String URLNextPage = "";
    String URLastPage = "";
    RequestQueue requestQueue;
    JSONObject jsonBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_requested_plans, container, false);

        back = view.findViewById(R.id.back);
        transferPlansRecyclerView = view.findViewById(R.id.transferPlansRecyclerView);
        shimmerFrameLayoutPlaceHolder = view.findViewById(R.id.shimmerFrameLayoutPlaceHolder);
        toEnd = view.findViewById(R.id.toEnd);
        offlineLayout = view.findViewById(R.id.offlineLayout);
        noServiceLayout = view.findViewById(R.id.noServiceLayout);
        allDone = view.findViewById(R.id.allDone);
        backToLogin = view.findViewById(R.id.backToLogin);
        backToHome = view.findViewById(R.id.backToHome);
        backToHome2 = view.findViewById(R.id.backToHome2);
        search = view.findViewById(R.id.search);
        searchLayout = view.findViewById(R.id.searchLayout);
        searchClose = view.findViewById(R.id.searchClose);
        searchName = view.findViewById(R.id.searchName);
        searchFamily = view.findViewById(R.id.searchFamily);
        searchNationalCode = view.findViewById(R.id.searchNationalCode);
        searchNameLayout = view.findViewById(R.id.searchNameLayout);
        searchFamilyLayout = view.findViewById(R.id.searchFamilyLayout);
        searchNationalCodeLayout = view.findViewById(R.id.searchNationalCodeLayout);
        searchStart = view.findViewById(R.id.searchStart);
        searchedContents = view.findViewById(R.id.searchedContents);
        searchedContentsNationalCode = view.findViewById(R.id.searchedContentsNationalCode);
        searchedContentsFamily = view.findViewById(R.id.searchedContentsFamily);
        searchedContentsName = view.findViewById(R.id.searchedContentsName);
        searchedContentsNationalCodeLayout = view.findViewById(R.id.searchedContentsNationalCodeLayout);
        searchedContentsFamilyLayout = view.findViewById(R.id.searchedContentsFamilyLayout);
        searchedContentsNameLayout = view.findViewById(R.id.searchedContentsNameLayout);
        searchedContentsClose = view.findViewById(R.id.searchedContentsClose);
        searchAgain = view.findViewById(R.id.searchAgain);
        notFound = view.findViewById(R.id.notFound);

        dbHandler = new DBHandler(requireActivity());
        requestQueue = Volley.newRequestQueue(requireActivity());
        UserModal userModal = dbHandler.getUser();
        userID = userModal.getId();

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

        if (userModal.getLoginMode() == 1) {
            offlineLayout.setVisibility(View.GONE);
            searchedContents.setVisibility(View.GONE);
            shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
            toEnd.setVisibility(View.VISIBLE);
            transferPlansRecyclerView.setVisibility(View.VISIBLE);


            noServiceLayout.setVisibility(View.INVISIBLE);
            searchLayout.setVisibility(View.INVISIBLE);

            planList = new ArrayList<>();


            jsonBody = new JSONObject();

            // GET PLANS FROM SERVER
            // AND PUT IN INTO THE LIST
            loadPlans(jsonBody);
            //..............................

        } else {
            offlineLayout.setVisibility(View.VISIBLE);
            searchedContents.setVisibility(View.GONE);
            shimmerFrameLayoutPlaceHolder.setVisibility(View.GONE);
            toEnd.setVisibility(View.GONE);
            transferPlansRecyclerView.setVisibility(View.GONE);
            search.hide();
            noServiceLayout.setVisibility(View.INVISIBLE);
        }

        backToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
        });

        backToHome.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HomeActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
            requireActivity().finish();
        });

        backToHome2.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), HomeActivity.class);
            startActivity(intent);
            requireActivity().overridePendingTransition(0, 0);
            requireActivity().finish();
        });

        toEnd.setOnClickListener(v -> transferPlansRecyclerView.smoothScrollToPosition(planList.size()));

        search.setOnClickListener(v -> {
            searchLayout.setVisibility(View.VISIBLE);
//            searchedContents.setVisibility(View.GONE);
            search.hide();
            View v2 = requireActivity().getCurrentFocus();
            if (v2 != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
                View v2 = requireActivity().getCurrentFocus();
                if (v2 != null) {
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                planList = new ArrayList<>();

                shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
                shimmerFrameLayoutPlaceHolder.startShimmer();
                transferPlansRecyclerView.setVisibility(View.INVISIBLE);
                notFound.setVisibility(View.GONE);
                search.show();
                searchedContentsNameLayout.setVisibility(View.GONE);
                searchedContentsFamilyLayout.setVisibility(View.GONE);
                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                searchedContents.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.INVISIBLE);


                jsonBody = new JSONObject();
                try {
                    if (!searchName.getText().toString().trim().isEmpty()) {
                        jsonBody.put("name", searchName.getText().toString().trim());
                        searchedContentsNameLayout.setVisibility(View.VISIBLE);
                        searchedContentsName.setText(searchName.getText().toString().trim());
                    }
                    if (!searchFamily.getText().toString().trim().isEmpty()) {
                        jsonBody.put("family", searchFamily.getText().toString().trim());
                        searchedContentsFamilyLayout.setVisibility(View.VISIBLE);
                        searchedContentsFamily.setText(searchFamily.getText().toString().trim());
                    }
                    if (!searchNationalCode.getText().toString().trim().isEmpty()) {
                        jsonBody.put("national_code", searchNationalCode.getText().toString().trim());
                        searchedContentsNationalCodeLayout.setVisibility(View.VISIBLE);
                        searchedContentsNationalCode.setText(searchNationalCode.getText().toString().trim());
                    }
//                    if (!searchPhoneNumber.getText().toString().isEmpty()) {
//                        jsonBody.put("phone", searchPhoneNumber.getText().toString());
//                        searchedContentsPhoneNumberLayout.setVisibility(View.VISIBLE);
//                        searchedContentsPhoneNumber.setText(searchPhoneNumber.getText().toString());
//                    }
                } catch (JSONException e) {
                    errorToastMaker("خطا در ساخت اطلاعات ارسالی!");
                }
                //LOAD PLANS ACCORDING TO SEARCH
                loadPlans(jsonBody);

            }
        });

        searchClose.setOnClickListener(v -> {
            View v2 = requireActivity().getCurrentFocus();
            if (v2 != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
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
            search.hide();

            if (noServiceLayout.getVisibility() != View.VISIBLE)
                search.show();

            adapter = new TransferRequestedPlansRecyclerViewAdapter(planList, senderUserList, receiverUserList, counselingModalArrayList, cultivationModalArrayList,
                    educationModalArrayList, facilityModalArrayList, identificationModalArrayList, licenseModalArrayList, marketingModalArrayList,
                    notificationModalArrayList, providingInfrastructureModalArrayList, providingTechnologyModalArrayList, requireActivity());
            transferPlansRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            transferPlansRecyclerView.setAdapter(adapter);

            View v2 = requireActivity().getCurrentFocus();
            if (v2 != null) {
                InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            jsonBody = new JSONObject();

            loadPlans(jsonBody);
        });

        transferPlansRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (!transferPlansRecyclerView.canScrollVertically(1) && planList.size() > 0) {
                    toEnd.hide();
                    loadMore(jsonBody);
                }

            }
        });

        return view;
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
                requireActivity().recreate();
            } catch (JSONException e) {
                // this will never happen but if so, show error to user.
            }
        }, error -> {
            // show error to user. refresh failed.
            Log.e("test", new String(error.networkResponse.data));

        });
        requestQueue.add(refreshTokenRequest);
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, requireActivity().findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(requireActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, requireActivity().findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(requireActivity());
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void loadPlans(JSONObject jsonBody) {
        shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
        shimmerFrameLayoutPlaceHolder.startShimmer();
        transferPlansRecyclerView.setVisibility(View.INVISIBLE);
        planList = new ArrayList<>();
        senderUserList = new ArrayList<>();
        receiverUserList = new ArrayList<>();
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

        JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLTransferRequestedPlanList), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response.getString("status").equals("1")) {
                            requestFlag = true;
                            JSONObject body = new JSONObject(response.getString("data"));
                            URLNextPage = body.getString("next_page_url");
                            URLastPage = body.getString("last_page_url");

                            JSONArray jsonArray = body.getJSONArray("data");
                            if (jsonArray.length() == 0) {
                                if (searchedContents.getVisibility() == View.VISIBLE)
                                    notFound.setVisibility(View.VISIBLE);
                                else
                                    allDone.setVisibility(View.VISIBLE);
                                transferPlansRecyclerView.setVisibility(View.INVISIBLE);
                                search.hide();
                                toEnd.setVisibility(View.INVISIBLE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    PlanModal plan = new PlanModal();
                                    UserModal sender = new UserModal();
                                    UserModal receiver = new UserModal();
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    plan.setUserID(data.getString("id"));

                                    plan.setEmployerID(data.getString("id_karfarma"));

                                    JSONObject senderData = data.getJSONObject("user_from_api");
                                    sender.setFirstName(senderData.getString("name"));
                                    sender.setLastName(senderData.getString("family"));

                                    JSONObject receiverData = data.getJSONObject("user_to_api");
                                    receiver.setFirstName(receiverData.getString("name"));
                                    receiver.setLastName(receiverData.getString("family"));

                                    senderUserList.add(sender);
                                    receiverUserList.add(receiver);

                                    JSONObject inspection = data.getJSONObject("inspection");

                                    plan.setPlanID(inspection.getString("id"));

                                    JSONObject location = inspection.getJSONObject("location_api");
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

                                    JSONObject employerArray = inspection.getJSONObject("karfarma");
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

                                    JSONObject planDetails = inspection.getJSONObject("tarh_api");
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

                                    JSONArray financing = inspection.getJSONArray("financing");
                                    if (financing.length() != 0) {
                                        plan.setPlanEchoSystemFinance("1");
                                        for (int j = 0; j < financing.length(); j++) {
                                            JSONObject financeData = financing.getJSONObject(j);
                                            FacilityModal facilityModal = new FacilityModal();
                                            facilityModal.setPlanID(inspection.getString("id"));

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

                                    JSONArray licensing = inspection.getJSONArray("license");
                                    if (licensing.length() != 0) {
                                        plan.setPlanEchoSystemLicence("1");
                                        for (int j = 0; j < licensing.length(); j++) {
                                            JSONObject financeData = licensing.getJSONObject(j);
                                            LicenseModal licenseModal = new LicenseModal();
                                            licenseModal.setPlanID(inspection.getString("id"));
                                            licenseModal.setLicenseID(financeData.getString("id"));
                                            licenseModal.setOwnerID(financeData.getString("id_karfarma"));
                                            licenseModal.setOrganID(financeData.getString("id_executive_device"));
                                            licenseModal.setLicenseType(financeData.getString("id_type_license"));
                                            licenseModal.setLicenseNumber(financeData.getString("license_number"));
                                            licenseModal.setLicenseDate(financeData.getString("date_license_number"));


                                            licenseModalArrayList.add(licenseModal);
                                        }
                                    } else plan.setPlanEchoSystemLicence("0");

                                    JSONArray marketing = inspection.getJSONArray("marketing");
                                    if (marketing.length() != 0) {
                                        plan.setPlanEchoSystemMarketing("1");
                                        for (int j = 0; j < marketing.length(); j++) {
                                            JSONObject marketingData = marketing.getJSONObject(j);
                                            MarketingModal marketingModal = new MarketingModal();
                                            marketingModal.setOwnerID(data.getString("id_karfarma"));
                                            marketingModal.setOrganID(marketingData.getString("id_executive_device"));
                                            marketingModal.setPlanID(inspection.getString("id"));

                                            marketingModalArrayList.add(marketingModal);
                                        }
                                    } else plan.setPlanEchoSystemMarketing("0");

                                    JSONArray technologySupply = inspection.getJSONArray("technology_supply");
                                    if (technologySupply.length() != 0) {
                                        plan.setPlanEchoSystemProvidingTechnology("1");
                                        for (int j = 0; j < technologySupply.length(); j++) {
                                            JSONObject technologySupplyData = technologySupply.getJSONObject(j);

                                            ProvidingTechnologyModal providingTechnologyModal = new ProvidingTechnologyModal();
                                            providingTechnologyModal.setOwnerID(data.getString("id_karfarma"));
                                            providingTechnologyModal.setOrganID(technologySupplyData.getString("id_executive_device"));
                                            providingTechnologyModal.setPlanID(inspection.getString("id"));

                                            providingTechnologyModalArrayList.add(providingTechnologyModal);
                                        }
                                    } else plan.setPlanEchoSystemProvidingTechnology("0");

                                    JSONArray providingInfrastructure = inspection.getJSONArray("providing_infrastructure");
                                    if (providingInfrastructure.length() != 0) {
                                        plan.setPlanEchoSystemProvidingInfrastructure("1");

                                        for (int j = 0; j < providingInfrastructure.length(); j++) {
                                            JSONObject providingInfrastructureData = providingInfrastructure.getJSONObject(j);

                                            ProvidingInfrastructureModal providingInfrastructureModal = new ProvidingInfrastructureModal();
                                            providingInfrastructureModal.setOwnerID(data.getString("id_karfarma"));
                                            providingInfrastructureModal.setOrganID(providingInfrastructureData.getString("id_executive_device"));
                                            providingInfrastructureModal.setPlanID(inspection.getString("id"));

                                            providingInfrastructureModalArrayList.add(providingInfrastructureModal);
                                        }
                                    } else plan.setPlanEchoSystemProvidingInfrastructure("0");

                                    JSONArray identification = inspection.getJSONArray("identity");
                                    if (identification.length() != 0) {
                                        plan.setPlanEchoSystemIdentification("1");

                                        for (int j = 0; j < identification.length(); j++) {
                                            JSONObject identificationData = identification.getJSONObject(j);

                                            IdentificationModal identificationModal = new IdentificationModal();
                                            identificationModal.setOwnerID(data.getString("id_karfarma"));
                                            identificationModal.setOrganID(identificationData.getString("id_executive_device"));
                                            identificationModal.setPlanID(inspection.getString("id"));

                                            identificationModalArrayList.add(identificationModal);
                                        }
                                    } else plan.setPlanEchoSystemIdentification("0");

                                    JSONArray consulting = inspection.getJSONArray("consulting");
                                    if (consulting.length() != 0) {
                                        plan.setPlanEchoSystemCounseling("1");

                                        for (int j = 0; j < consulting.length(); j++) {
                                            JSONObject consultingData = consulting.getJSONObject(j);

                                            CounselingModal counselingModal = new CounselingModal();
                                            counselingModal.setOwnerID(data.getString("id_karfarma"));
                                            counselingModal.setOrganID(consultingData.getString("id_executive_device"));
                                            counselingModal.setPlanID(inspection.getString("id"));

                                            counselingModalArrayList.add(counselingModal);
                                        }
                                    } else plan.setPlanEchoSystemCounseling("0");

                                    JSONArray cultivation = inspection.getJSONArray("culture_building");
                                    if (cultivation.length() != 0) {
                                        plan.setPlanEchoSystemCultivation("1");

                                        for (int j = 0; j < cultivation.length(); j++) {
                                            JSONObject cultivationData = cultivation.getJSONObject(j);

                                            CultivationModal cultivationModal = new CultivationModal();
                                            cultivationModal.setOwnerID(data.getString("id_karfarma"));
                                            cultivationModal.setOrganID(cultivationData.getString("id_executive_device"));
                                            cultivationModal.setPlanID(inspection.getString("id"));

                                            cultivationModalArrayList.add(cultivationModal);
                                        }
                                    } else plan.setPlanEchoSystemCultivation("0");

                                    JSONArray notice = inspection.getJSONArray("notices");
                                    if (notice.length() != 0) {
                                        plan.setPlanEchoSystemNotices("1");

                                        for (int j = 0; j < notice.length(); j++) {
                                            JSONObject noticeData = notice.getJSONObject(j);

                                            NotificationModal notificationModal = new NotificationModal();
                                            notificationModal.setOwnerID(data.getString("id_karfarma"));
                                            notificationModal.setOrganID(noticeData.getString("id_executive_device"));
                                            notificationModal.setPlanID(inspection.getString("id"));

                                            notificationModalArrayList.add(notificationModal);
                                        }
                                    } else plan.setPlanEchoSystemNotices("0");

                                    JSONArray education = inspection.getJSONArray("education");
                                    if (education.length() != 0) {
                                        plan.setPlanEchoSystemEducation("1");

                                        for (int j = 0; j < education.length(); j++) {
                                            JSONObject educationData = education.getJSONObject(j);

                                            EducationModal educationModal = new EducationModal();
                                            educationModal.setOwnerID(data.getString("id_karfarma"));
                                            educationModal.setOrganID(educationData.getString("id_executive_device"));
                                            educationModal.setPlanID(inspection.getString("id"));

                                            educationModalArrayList.add(educationModal);
                                        }
                                    } else plan.setPlanEchoSystemEducation("0");

                                    planList.add(plan);

                                }
                                if (!URLNextPage.equals("null")) {
                                    planList.add(null);
                                    senderUserList.add(null);
                                    receiverUserList.add(null);
                                }
                                search.show();
                            }
                            shimmerFrameLayoutPlaceHolder.setVisibility(View.INVISIBLE);
                            shimmerFrameLayoutPlaceHolder.stopShimmer();
                            transferPlansRecyclerView.setVisibility(View.VISIBLE);

                            adapter = new TransferRequestedPlansRecyclerViewAdapter(planList, senderUserList, receiverUserList, counselingModalArrayList, cultivationModalArrayList,
                                    educationModalArrayList, facilityModalArrayList, identificationModalArrayList, licenseModalArrayList, marketingModalArrayList,
                                    notificationModalArrayList, providingInfrastructureModalArrayList, providingTechnologyModalArrayList, requireActivity());
                            transferPlansRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
                            transferPlansRecyclerView.setAdapter(adapter);
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس دریافت اطلاعات");
                        e.printStackTrace();
                    }
                },
                error -> {
                    shimmerFrameLayoutPlaceHolder.setVisibility(View.INVISIBLE);
                    shimmerFrameLayoutPlaceHolder.stopShimmer();
                    offlineLayout.setVisibility(View.INVISIBLE);
                    toEnd.setVisibility(View.INVISIBLE);
                    noServiceLayout.setVisibility(View.VISIBLE);
                    errorToastMaker("ارتباط با سرور برقرار نشد!\nارتباط اینترنتی خود را بررسی کنید!");
                    search.hide();
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
        requestQueue.add(jsonRequest1);
        jsonRequest1.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void loadMore(JSONObject jsonBody) {

        if (planList.get(planList.size() - 1) == null) {
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, URLNextPage, jsonBody,
                    response -> {

                        try {
                            if (response.getString("status").equals("Token is Expired")) {
                                refreshAccessToken();
                            } else if (response.getString("status").equals("1")) {
                                requestFlag = true;
                                planList.remove(planList.size() - 1);
                                senderUserList.remove(senderUserList.size()-1);
                                receiverUserList.remove(receiverUserList.size()-1);
                                adapter.notifyItemRemoved(planList.size());
                                JSONObject body = new JSONObject(response.getString("data"));
                                URLNextPage = body.getString("next_page_url");
                                URLastPage = body.getString("last_page_url");

                                JSONArray jsonArray = body.getJSONArray("data");
                                if (jsonArray.length() == 0) {
                                    allDone.setVisibility(View.VISIBLE);
                                    transferPlansRecyclerView.setVisibility(View.INVISIBLE);
                                    toEnd.setVisibility(View.INVISIBLE);
                                    search.hide();
                                } else {
                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        PlanModal plan = new PlanModal();
                                        JSONObject data = jsonArray.getJSONObject(i);
                                        UserModal sender = new UserModal();
                                        UserModal receiver = new UserModal();

                                        plan.setEmployerID(data.getString("id_karfarma"));

                                        JSONObject senderData = data.getJSONObject("user_from_api");
                                        sender.setFirstName(senderData.getString("name"));
                                        sender.setLastName(senderData.getString("family"));

                                        JSONObject receiverData = data.getJSONObject("user_to_api");
                                        receiver.setFirstName(receiverData.getString("name"));
                                        receiver.setLastName(receiverData.getString("family"));

                                        senderUserList.add(sender);
                                        receiverUserList.add(receiver);

                                        JSONObject inspection = data.getJSONObject("inspection");

                                        plan.setPlanID(inspection.getString("id"));
//
                                        JSONObject location = inspection.getJSONObject("location_api");
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
                                        else*/ plan.setCity("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setRegion("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setMainStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setSubStreet("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setAlley("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setCode("");
                                        /*if (!location.getString("id_part").equalsIgnoreCase("null"))
                                            plan.setSection(location.getString("id_part"));
                                        else*/ plan.setPostalCode("");
                                        if (!location.getString("address").equalsIgnoreCase("null"))
                                            plan.setMoreAddress(location.getString("address"));
                                        else plan.setMoreAddress("");
//
                                        JSONObject employerArray = inspection.getJSONObject("karfarma");
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

                                        JSONObject planDetails = inspection.getJSONObject("tarh_api");
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

                                        JSONArray financing = inspection.getJSONArray("financing");
                                        if (financing.length() != 0) {
                                            plan.setPlanEchoSystemFinance("1");
                                            for (int j = 0; j < financing.length(); j++) {
                                                JSONObject financeData = financing.getJSONObject(j);
                                                FacilityModal facilityModal = new FacilityModal();
                                                facilityModal.setPlanID(inspection.getString("id"));

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

                                        JSONArray licensing = inspection.getJSONArray("license");
                                        if (licensing.length() != 0) {
                                            plan.setPlanEchoSystemLicence("1");
                                            for (int j = 0; j < licensing.length(); j++) {
                                                JSONObject financeData = licensing.getJSONObject(j);
                                                LicenseModal licenseModal = new LicenseModal();
                                                licenseModal.setPlanID(inspection.getString("id"));
                                                licenseModal.setLicenseID(financeData.getString("id"));
                                                licenseModal.setOwnerID(financeData.getString("id_karfarma"));
                                                licenseModal.setOrganID(financeData.getString("id_executive_device"));
                                                licenseModal.setLicenseType(financeData.getString("id_type_license"));
                                                licenseModal.setLicenseNumber(financeData.getString("license_number"));
                                                licenseModal.setLicenseDate(financeData.getString("date_license_number"));


                                                licenseModalArrayList.add(licenseModal);
                                            }
                                        } else plan.setPlanEchoSystemLicence("0");

                                        JSONArray marketing = inspection.getJSONArray("marketing");
                                        if (marketing.length() != 0) {
                                            plan.setPlanEchoSystemMarketing("1");
                                            for (int j = 0; j < marketing.length(); j++) {
                                                JSONObject marketingData = marketing.getJSONObject(j);
                                                MarketingModal marketingModal = new MarketingModal();
                                                marketingModal.setOwnerID(data.getString("id_karfarma"));
                                                marketingModal.setOrganID(marketingData.getString("id_executive_device"));
                                                marketingModal.setPlanID(inspection.getString("id"));

                                                marketingModalArrayList.add(marketingModal);
                                            }
                                        } else plan.setPlanEchoSystemMarketing("0");

                                        JSONArray technologySupply = inspection.getJSONArray("technology_supply");
                                        if (technologySupply.length() != 0) {
                                            plan.setPlanEchoSystemProvidingTechnology("1");
                                            for (int j = 0; j < technologySupply.length(); j++) {
                                                JSONObject technologySupplyData = technologySupply.getJSONObject(j);

                                                ProvidingTechnologyModal providingTechnologyModal = new ProvidingTechnologyModal();
                                                providingTechnologyModal.setOwnerID(data.getString("id_karfarma"));
                                                providingTechnologyModal.setOrganID(technologySupplyData.getString("id_executive_device"));
                                                providingTechnologyModal.setPlanID(inspection.getString("id"));

                                                providingTechnologyModalArrayList.add(providingTechnologyModal);
                                            }
                                        } else plan.setPlanEchoSystemProvidingTechnology("0");

                                        JSONArray providingInfrastructure = inspection.getJSONArray("providing_infrastructure");
                                        if (providingInfrastructure.length() != 0) {
                                            plan.setPlanEchoSystemProvidingInfrastructure("1");

                                            for (int j = 0; j < providingInfrastructure.length(); j++) {
                                                JSONObject providingInfrastructureData = providingInfrastructure.getJSONObject(j);

                                                ProvidingInfrastructureModal providingInfrastructureModal = new ProvidingInfrastructureModal();
                                                providingInfrastructureModal.setOwnerID(data.getString("id_karfarma"));
                                                providingInfrastructureModal.setOrganID(providingInfrastructureData.getString("id_executive_device"));
                                                providingInfrastructureModal.setPlanID(inspection.getString("id"));

                                                providingInfrastructureModalArrayList.add(providingInfrastructureModal);
                                            }
                                        } else plan.setPlanEchoSystemProvidingInfrastructure("0");

                                        JSONArray identification = inspection.getJSONArray("identity");
                                        if (identification.length() != 0) {
                                            plan.setPlanEchoSystemIdentification("1");

                                            for (int j = 0; j < identification.length(); j++) {
                                                JSONObject identificationData = identification.getJSONObject(j);

                                                IdentificationModal identificationModal = new IdentificationModal();
                                                identificationModal.setOwnerID(data.getString("id_karfarma"));
                                                identificationModal.setOrganID(identificationData.getString("id_executive_device"));
                                                identificationModal.setPlanID(inspection.getString("id"));

                                                identificationModalArrayList.add(identificationModal);
                                            }
                                        } else plan.setPlanEchoSystemIdentification("0");

                                        JSONArray consulting = inspection.getJSONArray("consulting");
                                        if (consulting.length() != 0) {
                                            plan.setPlanEchoSystemCounseling("1");

                                            for (int j = 0; j < consulting.length(); j++) {
                                                JSONObject consultingData = consulting.getJSONObject(j);

                                                CounselingModal counselingModal = new CounselingModal();
                                                counselingModal.setOwnerID(data.getString("id_karfarma"));
                                                counselingModal.setOrganID(consultingData.getString("id_executive_device"));
                                                counselingModal.setPlanID(inspection.getString("id"));

                                                counselingModalArrayList.add(counselingModal);
                                            }
                                        } else plan.setPlanEchoSystemCounseling("0");

                                        JSONArray cultivation = inspection.getJSONArray("culture_building");
                                        if (cultivation.length() != 0) {
                                            plan.setPlanEchoSystemCultivation("1");

                                            for (int j = 0; j < cultivation.length(); j++) {
                                                JSONObject cultivationData = cultivation.getJSONObject(j);

                                                CultivationModal cultivationModal = new CultivationModal();
                                                cultivationModal.setOwnerID(data.getString("id_karfarma"));
                                                cultivationModal.setOrganID(cultivationData.getString("id_executive_device"));
                                                cultivationModal.setPlanID(inspection.getString("id"));

                                                cultivationModalArrayList.add(cultivationModal);
                                            }
                                        } else plan.setPlanEchoSystemCultivation("0");

                                        JSONArray notice = inspection.getJSONArray("notices");
                                        if (notice.length() != 0) {
                                            plan.setPlanEchoSystemNotices("1");

                                            for (int j = 0; j < notice.length(); j++) {
                                                JSONObject noticeData = notice.getJSONObject(j);

                                                NotificationModal notificationModal = new NotificationModal();
                                                notificationModal.setOwnerID(data.getString("id_karfarma"));
                                                notificationModal.setOrganID(noticeData.getString("id_executive_device"));
                                                notificationModal.setPlanID(inspection.getString("id"));

                                                notificationModalArrayList.add(notificationModal);
                                            }
                                        } else plan.setPlanEchoSystemNotices("0");

                                        plan.setIsCompleted(0);
                                        plan.setWhichStep(0);
                                        plan.setUserID(userID);
                                        plan.setPlanFinalSuperVisionSituation(0);

                                        planList.add(plan);

                                    }
                                    if (!URLNextPage.equals("null")) {
                                        planList.add(null);
                                        senderUserList.add(null);
                                        receiverUserList.add(null);
                                    }
                                }

                            }
                        } catch (JSONException e) {
                            errorToastMaker("خطای به روزسانی لیست طرح ها");
                            e.printStackTrace();
                        }
                    },
                    error -> errorToastMaker("خطای سرویس طرح های ورودی")
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
            if (requestFlag)
                requestQueue.add(jsonRequest);
            requestFlag = false;
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }
    }

    private boolean checkErrors() {
        boolean retVal = false;
        if (!searchName.getText().toString().isEmpty()) {
            if (searchName.getText().toString().trim().length() < 2) {
                searchNameLayout.setError("نام نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchFamily.getText().toString().isEmpty()) {
            if (searchFamily.getText().toString().trim().length() < 2) {
                searchFamilyLayout.setError("نام خانوادگی نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchNationalCode.getText().toString().isEmpty()) {
            if (searchNationalCode.getText().toString().trim().length() < 10) {
                searchNationalCodeLayout.setError("کد ملی باید 10 رقم باشد!");
                retVal = true;
            }
        }
        return retVal;
    }
}