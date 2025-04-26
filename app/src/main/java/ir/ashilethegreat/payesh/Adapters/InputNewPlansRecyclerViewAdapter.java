package ir.ashilethegreat.payesh.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.Activities.InputRequestsActivity;
import ir.ashilethegreat.payesh.Activities.InquiryActivity;
import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
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
import ir.ashilethegreat.payesh.Fragments.InputNewPlansFragment;
import ir.ashilethegreat.payesh.R;

public class InputNewPlansRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DEFAULT_TIMEOUT = 20000;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ArrayList<PlanModal> planList;
    ArrayList<UserModal> userList;
    ArrayList<String> userNames;
    ArrayList<LicenseModal> licenseList;
    ArrayList<FacilityModal> facilityList;
    ArrayList<CounselingModal> counselingList;
    ArrayList<CultivationModal> cultivationList;
    ArrayList<EducationModal> educationList;
    ArrayList<IdentificationModal> identificationList;
    ArrayList<MarketingModal> marketingList;
    ArrayList<NotificationModal> notificationList;
    ArrayList<ProvidingInfrastructureModal> providingInfrastructureList;
    ArrayList<ProvidingTechnologyModal> providingTechnologyList;
    final DBHandler dbHandler;
    final Context context;
    final String loginToken;
    boolean holdTheClick = false;
    final RequestQueue requestQueue;
    Dialog moreDetails, transferDialog;
    JSONObject jsonBody;
    int selectedPosition;


    public InputNewPlansRecyclerViewAdapter(ArrayList<PlanModal> planList, ArrayList<CounselingModal> counselingArrayList, ArrayList<CultivationModal> cultivationArrayList,
                                            ArrayList<EducationModal> educationArrayList, ArrayList<FacilityModal> facilityArrayList, ArrayList<IdentificationModal> identificationArrayList,
                                            ArrayList<LicenseModal> licenseArrayList, ArrayList<MarketingModal> marketingArrayList,
                                            ArrayList<NotificationModal> notificationArrayList, ArrayList<ProvidingInfrastructureModal> providingInfrastructureArrayList,
                                            ArrayList<ProvidingTechnologyModal> providingTechnologyArrayList, Context context) {
        this.planList = planList;
        counselingList = counselingArrayList;
        cultivationList = cultivationArrayList;
        educationList = educationArrayList;
        facilityList = facilityArrayList;
        identificationList = identificationArrayList;
        licenseList = licenseArrayList;
        marketingList = marketingArrayList;
        notificationList = notificationArrayList;
        providingInfrastructureList = providingInfrastructureArrayList;
        providingTechnologyList = providingTechnologyArrayList;
        this.context = context;
        this.userList = new ArrayList<>();
        this.userNames = new ArrayList<>();
        dbHandler = new DBHandler(context);
        this.loginToken = dbHandler.getUser().getToken();
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_input_requests_recyclerview_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_input_requests_shimmer_layout, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PlanModal plan = planList.get(viewHolder.getAbsoluteAdapterPosition());
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_ITEM -> ((ItemViewHolder) viewHolder).bind(plan);
            case VIEW_TYPE_LOADING -> ((LoadingViewHolder) viewHolder).bind();
        }
    }

    @Override
    public int getItemCount() {
        if (planList == null) return 0;
        return planList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (planList.get(position) == null) return VIEW_TYPE_LOADING;
        return VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView PlanPosition;
        final TextView name;
        final TextView planTitle;
        final TextView phoneNumber;
        final TextView address;
        final LinearLayout addToSupperVisionsLayout;
        final LinearLayout callLayout;
        final RelativeLayout bottomBody;
        final ProgressBar loading;
        ImageView more;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            addToSupperVisionsLayout = itemView.findViewById(R.id.addToSupperVisionsLayout);
            address = itemView.findViewById(R.id.address);
            callLayout = itemView.findViewById(R.id.callLayout);
            loading = itemView.findViewById(R.id.loading);
            more = itemView.findViewById(R.id.more);
            bottomBody = itemView.findViewById(R.id.bottomBody);
        }

        void bind(PlanModal plan) {
            PlanPosition.setText(String.valueOf(plan.getPlanID()));
            name.setText(String.format("%s %s", plan.getName(), plan.getFamilyName()));
            planTitle.setText(dbHandler.readPlanTitleFromID(plan.getPlanTitle()));

            if (!plan.getPhone().equalsIgnoreCase("null") && !plan.getPhone().equalsIgnoreCase(""))
                phoneNumber.setText(plan.getPhone());
            else phoneNumber.setText("مشخص نشده");

            address.setText(String.format("%s،%s", dbHandler.readProvinceNameFromID(plan.getProvince()), dbHandler.readCityNameFromID(plan.getTownship())));
            loading.setVisibility(View.INVISIBLE);
            bottomBody.setVisibility(View.VISIBLE);

            addToSupperVisionsLayout.setOnClickListener(v -> {
                if (plan.getProvince().trim().equalsIgnoreCase("null") || plan.getTownship().trim().equalsIgnoreCase("null") ||
                        plan.getProvince().trim().equals("") || plan.getTownship().trim().equalsIgnoreCase("") ||
                        plan.getSection().trim().equalsIgnoreCase("null") || plan.getSection().trim().equalsIgnoreCase("") ||
                        plan.getName().trim().equalsIgnoreCase("null") || plan.getName().trim().equalsIgnoreCase("") ||
                        plan.getFamilyName().trim().equalsIgnoreCase("null") || plan.getFamilyName().trim().equalsIgnoreCase("") ||
                        plan.getParentName().trim().equalsIgnoreCase("null") || plan.getParentName().trim().equalsIgnoreCase("") ||
                        plan.getNatCode().trim().equalsIgnoreCase("null") || plan.getNatCode().trim().equalsIgnoreCase("") ||
                        plan.getBirthday().trim().equalsIgnoreCase("null") || plan.getBirthday().trim().equalsIgnoreCase("") ||
                        plan.getPlanTitle().trim().equalsIgnoreCase("null") || plan.getPlanTitle().trim().equalsIgnoreCase("")) {
                    errorToastMaker("به دلیل تکمیل نبودن بخشی از اطلاعات اصلی، امکان اضافه نمودن طرح به لیست وجود ندارد!", new View(context));
                } else if (!holdTheClick) {
                    holdTheClick = true;
                    loading.setVisibility(View.VISIBLE);
                    bottomBody.setVisibility(View.INVISIBLE);
                    if (dbHandler.planSelectRowExist(plan.getPlanID())) {
                        createDeleteDialog(plan, getBindingAdapterPosition(), itemView, loading, bottomBody);
                    } else
                        removeSelectedPlan(plan, getBindingAdapterPosition(), itemView, loading, bottomBody);
                }
            });
//
            callLayout.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if (plan.getPhone() != null) {
                    if (!plan.getPhone().isEmpty() && plan.getPhone().startsWith("0"))
                        intent.setData(Uri.parse("tel:" + plan.getPhone()));
                    else if (!plan.getPhone().isEmpty() && !plan.getPhone().startsWith("0"))
                        intent.setData(Uri.parse("tel:0" + plan.getPhone()));
                } else {
                    errorToastMaker("شماره ای برای تماس وجود ندارد!", v);
                }
                context.startActivity(intent);

            });

            more.setOnClickListener(v -> {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.custom_manage_input_plans_layout);

                LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
                LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
                LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);

                Objects.requireNonNull(moreDetails).setOnClickListener(view1 -> {
                    createDetailsDialog(plan);
                    bottomSheetDialog.cancel();
                });

                Objects.requireNonNull(inquiryLayout).setOnClickListener(view1 -> {
                    Intent intent = new Intent(context, InquiryActivity.class);
                    intent.putExtra("natCode", plan.getNatCode());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    bottomSheetDialog.cancel();
                });

                Objects.requireNonNull(transferPlansManagementLayout).setOnClickListener(view1 -> {
                    createTransferDialog(plan, getBindingAdapterPosition());
                    bottomSheetDialog.cancel();
                });

                bottomSheetDialog.show();
            });
        }
    }

    private static class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        void bind() {
        }

    }

    private void removeSelectedPlan(PlanModal plan, int position, View view, View loading, View bottomLayout) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_inspection", plan.getPlanID());
            jsonBody.put("national_code", plan.getNatCode());
        } catch (JSONException e) {
            errorToastMaker("خطای ساخت اطلاعات ارسالی", view);
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLRemoveSelectedPlans), jsonBody,
                response -> {
                    Log.i("test", "response:" + response.toString());

//                    Log.i("test", plan.getInspectionID());
                    try {
                        if (response.getString("status").equals("Token is Expired"))
                            refreshAccessToken();
                        if (response.getString("status").equals("1")) {
                            dbHandler.deletePlan(plan.getPlanID(), plan.getUserID());
                            dbHandler.deleteFinalSuperVisionPlan(plan.getPlanID(), plan.getUserID());
                            addPlanToSuperVisionList(plan, position);

                            InputNewPlansFragment.planCounter--;
                            if (InputNewPlansFragment.planCounter > 0)
                                Objects.requireNonNull(InputRequestsActivity.tabLayout.getTabAt(0)).getOrCreateBadge().setNumber(InputNewPlansFragment.planCounter);
                            else
                                Objects.requireNonNull(InputRequestsActivity.tabLayout.getTabAt(0)).removeBadge();

                            dbHandler.close();
                        } else if (response.getString("status").equals("0")) {
                            alertToastMaker("در حال حاضر طرح قابل اضافه کردن به لیست نیست!", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomLayout.setVisibility(View.VISIBLE);
                        } else {
                            alertToastMaker("در ارتباط با سرور مشکلی رخ داد. \n دوباره تلاش کنید.", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomLayout.setVisibility(View.VISIBLE);
                        }
                        holdTheClick = false;
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس گرفتن طرح", view);
                        loading.setVisibility(View.INVISIBLE);
                        bottomLayout.setVisibility(View.VISIBLE);
                        holdTheClick = false;
                    }
                },
                error -> {
//                    Log.i("test",error.networkResponse.toString());
                    error.printStackTrace();
                    errorToastMaker("ارتباط اینترنتی خود را بررسی کرده و دوباره تلاش کنید.", view);
                    loading.setVisibility(View.INVISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                    holdTheClick = false;
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + loginToken);
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void createDeleteDialog(PlanModal plan, int position, View view, View loading, View bottomLayout) {
        Dialog delete = new Dialog(context);
        delete.setContentView(R.layout.custom_input_requests_confirmation_delete_dialog_layout);
        Objects.requireNonNull(delete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ExtendedFloatingActionButton confirm, cancel;

        confirm = delete.findViewById(R.id.confirm);
        cancel = delete.findViewById(R.id.cancel);

        holdTheClick = false;

        cancel.setOnClickListener(v -> {
            loading.setVisibility(View.INVISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);
            delete.dismiss();
        });
        confirm.setOnClickListener(v -> {
            removeSelectedPlan(plan, position, view, loading, bottomLayout);
            delete.dismiss();
        });

        delete.show();
    }

    private void addPlanToSuperVisionList(PlanModal plan, int position) {
        dbHandler.putPlanForSupperVision(plan);

        for (int i = 0; i < counselingList.size(); i++) {
            if (counselingList.get(i).getPlanID().equals(plan.getPlanID()))
                dbHandler.insertEcosystemCounseling(counselingList.get(i));

        }

        for (int i = 0; i < cultivationList.size(); i++) {
            if (cultivationList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemCultivation(cultivationList.get(i));
            }
        }

        for (int i = 0; i < educationList.size(); i++) {
            if (educationList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemEducation(educationList.get(i));
            }
        }

        for (int i = 0; i < facilityList.size(); i++) {
            if (facilityList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemFacility(facilityList.get(i));
            }
        }

        for (int i = 0; i < identificationList.size(); i++) {
            if (identificationList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemIdentification(identificationList.get(i));
            }
        }

        for (int i = 0; i < licenseList.size(); i++) {
            if (licenseList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemLicense(licenseList.get(i));
            }
        }

        for (int i = 0; i < marketingList.size(); i++) {
            if (marketingList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemMarketing(marketingList.get(i));
            }
        }

        for (int i = 0; i < notificationList.size(); i++) {
            if (notificationList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemNotification(notificationList.get(i));
            }
        }

        for (int i = 0; i < providingInfrastructureList.size(); i++) {
            if (providingInfrastructureList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemProvidingInfrastructure(providingInfrastructureList.get(i));
            }
        }

        for (int i = 0; i < providingTechnologyList.size(); i++) {
            if (providingTechnologyList.get(i).getPlanID().equals(plan.getPlanID())) {
                dbHandler.insertEcosystemProvidingTechnology(providingTechnologyList.get(i));
            }
        }

        dbHandler.close();
        planList.remove(plan);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, planList.size());
        successfulToastMaker("به لیست نظارت ها اضافه شد.", new View(context));
        if (planList.size() > 0)
            InputNewPlansFragment.search.show();
        if (planList.size() == 0 && InputNewPlansFragment.searchedContents.getVisibility() != View.VISIBLE) {
            InputNewPlansFragment.allDone.setVisibility(View.VISIBLE);
            InputNewPlansFragment.search.hide();
        } else if (planList.size() == 0 && InputNewPlansFragment.searchedContents.getVisibility() == View.VISIBLE) {
            InputNewPlansFragment.notFound.setVisibility(View.VISIBLE);
            InputNewPlansFragment.search.hide();
        }
    }

    private void createDetailsDialog(PlanModal plan) {
        Intent intent = new Intent(context, MorePlanDetailsActivity.class);
        MorePlanDetailsActivity.planModal = plan;
        MorePlanDetailsActivity.providingTechnologyModalArrayList = providingTechnologyList;
        MorePlanDetailsActivity.facilityModalArrayList = facilityList;
        MorePlanDetailsActivity.licenseModalArrayList = licenseList;
        MorePlanDetailsActivity.counselingModalArrayList = counselingList;
        MorePlanDetailsActivity.cultivationModalArrayList = cultivationList;
        MorePlanDetailsActivity.educationModalArrayList = educationList;
        MorePlanDetailsActivity.marketingModalArrayList = marketingList;
        MorePlanDetailsActivity.identificationModalArrayList = identificationList;
        MorePlanDetailsActivity.providingInfrastructureModalArrayList = providingInfrastructureList;
        MorePlanDetailsActivity.notificationModalArrayList = notificationList;
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    private void createTransferDialog(PlanModal plan, int planPosition) {

        transferDialog = new Dialog(context);
        transferDialog.setContentView(R.layout.custom_transfer_plans_management_users_list_layout);
        Objects.requireNonNull(transferDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout userChooseLayout;
        AutoCompleteTextView userChoose;
        ExtendedFloatingActionButton transfer, cancel;
        ProgressBar userLoadingProgressbar, transferProgressbar;
        LinearLayout bottomBar;

        userChooseLayout = transferDialog.findViewById(R.id.userChooseLayout);
        userChoose = transferDialog.findViewById(R.id.userChoose);
        transfer = transferDialog.findViewById(R.id.transfer);
        cancel = transferDialog.findViewById(R.id.cancel);
        userLoadingProgressbar = transferDialog.findViewById(R.id.userLoadingProgressbar);
        transferProgressbar = transferDialog.findViewById(R.id.transferProgressbar);
        bottomBar = transferDialog.findViewById(R.id.bottomBar);

        userLoadingProgressbar.setVisibility(View.VISIBLE);
        userChooseLayout.setVisibility(View.INVISIBLE);

        jsonBody = new JSONObject();
        JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLTransferUserList), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                            transferDialog.dismiss();
                        } else if (response.getString("status").equals("1")) {

                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() == 0) {
                                alertToastMaker("شخصی برای انتقال طرح یافت نشد!", new View(context));
                                userChooseLayout.setVisibility(View.VISIBLE);
                                userLoadingProgressbar.setVisibility(View.INVISIBLE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject data = jsonArray.getJSONObject(i);

                                    UserModal userModal = new UserModal();
                                    userModal.setId(data.getString("id"));
                                    userModal.setFirstName(data.getString("name"));
                                    userModal.setLastName(data.getString("family"));

                                    userList.add(userModal);

                                    userNames.add(i + 1 + "- " + data.getString("name") + " " + data.getString("family"));

                                }
                            }
                            ArrayAdapter<String> adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, userNames);
                            userChoose.setAdapter(adapterOfOptions);
                            userChooseLayout.setVisibility(View.VISIBLE);
                            userLoadingProgressbar.setVisibility(View.INVISIBLE);

                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس دریافت اطلاعات اشخاص", new View(context));
                        e.printStackTrace();
                        transferDialog.dismiss();
                    }
                },
                error -> {
                    errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                    transferDialog.dismiss();
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

        userChoose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userChooseLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userChoose.setOnItemClickListener((parent, view, position, id) -> {
//            alertToastMaker(String.valueOf(position), new View(context));
            selectedPosition = position;
        });

        transfer.setOnClickListener(v -> {
            if (userChoose.getText().toString().isEmpty())
                userChooseLayout.setError("یک مورد را انتخاب کنید!");
            else {
                Dialog confirmDialog = new Dialog(context);
                confirmDialog.setContentView(R.layout.custom_input_requests_transfer_confirmation_dialog_layout);
                Objects.requireNonNull(confirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ExtendedFloatingActionButton confirmTransfer, cancelTransfer;
                ProgressBar progressBar;
                LinearLayout bottomButtons;

                confirmTransfer = confirmDialog.findViewById(R.id.confirm);
                cancelTransfer = confirmDialog.findViewById(R.id.cancel);
                progressBar = confirmDialog.findViewById(R.id.progressBar);
                bottomButtons = confirmDialog.findViewById(R.id.bottomButtons);

                confirmTransfer.setOnClickListener(v12 -> {

                    bottomBar.setVisibility(View.INVISIBLE);
                    transferProgressbar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    bottomButtons.setVisibility(View.INVISIBLE);

                    jsonBody = new JSONObject();
                    try {
                        jsonBody.put("id_user", userList.get(selectedPosition).getId());
                    } catch (JSONException ignored) {

                    }
                    JsonObjectRequest transferRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLPlanTransferRequest) + plan.getPlanID(), jsonBody,
                            response -> {
                                try {
                                    if (response.getString("status").equals("Token is Expired")) {
                                        refreshAccessToken();
                                        bottomBar.setVisibility(View.VISIBLE);
                                        transferProgressbar.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        bottomButtons.setVisibility(View.VISIBLE);
                                    } else if (response.getString("status").equals("1")) {

                                        dbHandler.deletePlan(planList.get(planPosition).getPlanID(), planList.get(planPosition).getUserID());
                                        planList.remove(plan);
                                        notifyItemRemoved(planPosition);
                                        notifyItemRangeChanged(planPosition, planList.size());

                                        InputNewPlansFragment.planCounter--;
                                        if (InputNewPlansFragment.planCounter > 0)
                                            Objects.requireNonNull(InputRequestsActivity.tabLayout.getTabAt(0)).getOrCreateBadge().setNumber(InputNewPlansFragment.planCounter);
                                        else
                                            Objects.requireNonNull(InputRequestsActivity.tabLayout.getTabAt(0)).removeBadge();

                                        if (planList.size() > 0)
                                            InputNewPlansFragment.search.show();
                                        if (planList.size() == 0 && InputNewPlansFragment.searchedContents.getVisibility() != View.VISIBLE) {
                                            InputNewPlansFragment.allDone.setVisibility(View.VISIBLE);
                                            InputNewPlansFragment.search.hide();
                                        } else if (planList.size() == 0 && InputNewPlansFragment.searchedContents.getVisibility() == View.VISIBLE) {
                                            InputNewPlansFragment.notFound.setVisibility(View.VISIBLE);
                                            InputNewPlansFragment.search.hide();
                                        }

                                        successfulToastMaker("درخواست انتقال طرح با موفقیت ثبت شد.", new View(context));
                                        transferDialog.dismiss();
                                        confirmDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    errorToastMaker("خطای سرویس انتقال طرح", new View(context));
                                    e.printStackTrace();
                                    transferDialog.dismiss();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    bottomButtons.setVisibility(View.VISIBLE);
                                }
                            },
                            error -> {
                                errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                                progressBar.setVisibility(View.INVISIBLE);
                                bottomButtons.setVisibility(View.VISIBLE);
                                transferDialog.dismiss();
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
                    requestQueue.add(transferRequest);
                    transferRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DEFAULT_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    confirmDialog.dismiss();
                });

                cancelTransfer.setOnClickListener(v1 -> confirmDialog.dismiss());

                confirmDialog.show();
            }

        });


        cancel.setOnClickListener(v -> transferDialog.dismiss());

        transferDialog.show();
    }

    public void errorToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_error, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_alert, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void successfulToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_successful, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void refreshAccessToken() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", dbHandler.getUser().getUsername());
            jsonBody.put("password", dbHandler.getUser().getPassword());

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
            } catch (JSONException e) {
                errorToastMaker("خطای سرویس ساخت توکن", new View(context));
            }
        }, error -> {
            // show error to user. refresh failed.
            errorToastMaker("ارتباط با سرور برقرار نشد!", new View(context));

        });
        requestQueue.add(refreshTokenRequest);
    }
}
