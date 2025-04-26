package ir.ashilethegreat.payesh.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
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

import ir.ashilethegreat.payesh.Activities.InquiryActivity;
import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.Activities.ReloadManagementActivity;
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

public class ReloadManagementRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int DEFAULT_TIMEOUT = 20000;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ArrayList<PlanModal> planList;
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
    ArrayList<UserModal> userList;
    ArrayList<String> userNames;
    int selectedPosition;
    final DBHandler dbHandler;
    final Context context;
    final String loginToken;
    boolean holdTheClick = false;
    final RequestQueue requestQueue;
    Dialog transferDialog;
    JSONObject jsonBody;

    public ReloadManagementRecyclerViewAdapter(ArrayList<PlanModal> planList, ArrayList<CounselingModal> counselingArrayList, ArrayList<CultivationModal> cultivationArrayList,
                                               ArrayList<EducationModal> educationArrayList, ArrayList<FacilityModal> facilityArrayList, ArrayList<IdentificationModal> identificationArrayList,
                                               ArrayList<LicenseModal> licenseArrayList, ArrayList<MarketingModal> marketingArrayList,
                                               ArrayList<NotificationModal> notificationArrayList, ArrayList<ProvidingInfrastructureModal> providingInfrastructureArrayList,
                                               ArrayList<ProvidingTechnologyModal> providingTechnologyArrayList, Context context) {
        this.planList = planList;
        this.counselingList = counselingArrayList;
        this.cultivationList = cultivationArrayList;
        this.educationList = educationArrayList;
        this.facilityList = facilityArrayList;
        this.identificationList = identificationArrayList;
        this.licenseList = licenseArrayList;
        this.marketingList = marketingArrayList;
        this.notificationList = notificationArrayList;
        this.providingInfrastructureList = providingInfrastructureArrayList;
        this.providingTechnologyList = providingTechnologyArrayList;
        this.userList = new ArrayList<>();
        this.userNames = new ArrayList<>();
        this.context = context;
        dbHandler = new DBHandler(context);
        this.loginToken = dbHandler.getUser().getToken();
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_reload_management_recyclerview_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_reload_management_shimmer_layout, parent, false);
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
        final TextView phoneNumber;
        final TextView address;
        final LinearLayout reloadPlanLayout;
        final RelativeLayout bottomBody;
        final ProgressBar loading;
        final ImageView more;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            reloadPlanLayout = itemView.findViewById(R.id.reloadPlanLayout);
            bottomBody = itemView.findViewById(R.id.bottomBody);
            address = itemView.findViewById(R.id.address);
            loading = itemView.findViewById(R.id.loading);
            more = itemView.findViewById(R.id.more);
        }

        void bind(PlanModal plan) {
            PlanPosition.setText(String.valueOf(plan.getPlanID()));
            name.setText(String.format("%s %s", plan.getName(), plan.getFamilyName()));
            loading.setVisibility(View.INVISIBLE);
            bottomBody.setVisibility(View.VISIBLE);

            if (!plan.getPhone().equalsIgnoreCase("null") && !plan.getPhone().equalsIgnoreCase(""))
                phoneNumber.setText(plan.getPhone());
            else phoneNumber.setText("مشخص نشده");

            address.setText(String.format("%s،%s", dbHandler.readProvinceNameFromID(plan.getProvince()), dbHandler.readCityNameFromID(plan.getTownship())));

            reloadPlanLayout.setOnClickListener(v -> {
                if (!holdTheClick) {
                    holdTheClick = true;
                    createDeleteDialog(plan, getBindingAdapterPosition(), loading, bottomBody);
                }
            });

            more.setOnClickListener(v -> {
                final Dialog bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.custom_manage_input_plans_layout);

                LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
                LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
                LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);


                Objects.requireNonNull(moreDetails).setOnClickListener(view1 -> {
                    createDetailsDialog(planList.get(getAbsoluteAdapterPosition()));
                    bottomSheetDialog.cancel();
                });

                Objects.requireNonNull(transferPlansManagementLayout).setOnClickListener(view1 -> {
                    createTransferDialog(planList.get(getAbsoluteAdapterPosition()), getAbsoluteAdapterPosition());
                    bottomSheetDialog.cancel();
                });

                Objects.requireNonNull(inquiryLayout).setOnClickListener(view1 -> {
                    Intent intent = new Intent(context, InquiryActivity.class);
                    intent.putExtra("natCode", planList.get(getAbsoluteAdapterPosition()).getNatCode());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
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

    private void reloadSelectedPlan(PlanModal plan, int position, View view, View loading, View bottomBody) {

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_inspection", plan.getPlanID());
            jsonBody.put("national_code", plan.getNatCode());
        } catch (JSONException e) {
            errorToastMaker("خطای ساخت اطلاعات ارسالی", view);
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLDelete), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired"))
                            refreshAccessToken();
                        if (response.getString("status").equals("1")) {
                            reloadPlan(plan, position);
                            dbHandler.close();
                        } else if (response.getString("status").equals("0")) {
                            alertToastMaker("در حال حاضر طرح قابل بارگذاری مجدد نیست!", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomBody.setVisibility(View.VISIBLE);
                        } else {
                            alertToastMaker("در ارتباط با سرور مشکلی رخ داد. \n دوباره تلاش کنید.", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomBody.setVisibility(View.VISIBLE);
                        }
                        holdTheClick = false;
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس بارگذاری مجدد طرح", view);
                        holdTheClick = false;
                    }
                },
                error ->

                {
//                    Log.i("test",error.networkResponse.toString());
                    error.printStackTrace();
                    errorToastMaker("ارتباط اینترنتی خود را بررسی کرده و دوباره تلاش کنید.", view);
                    loading.setVisibility(View.INVISIBLE);
                    bottomBody.setVisibility(View.VISIBLE);
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
        jsonRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void createDeleteDialog(PlanModal plan, int position, View loading, View bottomBody) {
        Dialog delete = new Dialog(context);
        delete.setContentView(R.layout.custom_input_requests_confirmation_delete_dialog_layout);
        Objects.requireNonNull(delete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete.setCanceledOnTouchOutside(false);

        ExtendedFloatingActionButton confirm, cancel;

        confirm = delete.findViewById(R.id.confirm);
        cancel = delete.findViewById(R.id.cancel);

        cancel.setOnClickListener(v -> {
            loading.setVisibility(View.INVISIBLE);
            bottomBody.setVisibility(View.VISIBLE);
            holdTheClick = false;
            delete.dismiss();
        });
        confirm.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            bottomBody.setVisibility(View.INVISIBLE);
            reloadSelectedPlan(plan, position, new View(context), loading, bottomBody);
            delete.dismiss();
        });

        delete.show();
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
            selectedPosition = position;
        });

        transfer.setOnClickListener(v -> {
            if (userChoose.getText().toString().isEmpty())
                userChooseLayout.setError("یک مورد را انتخاب کنید!");
            else {
                Dialog confirmDialog = new Dialog(context);
                confirmDialog.setContentView(R.layout.custom_ready_to_send_transfer_confirmation_dialog_layout);
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
                                        planList.remove(planPosition);
                                        notifyItemRemoved(planPosition);
//                                        notifyDataSetChanged();
                                        if (planList.size() == 0 && ReloadManagementActivity.searchedContents.getVisibility() != View.VISIBLE) {
                                            ReloadManagementActivity.reloadPlansRecyclerView.setVisibility(View.INVISIBLE);
                                            ReloadManagementActivity.offlineLayout.setVisibility(View.VISIBLE);
                                            ReloadManagementActivity.search.hide();
                                            ReloadManagementActivity.toEnd.hide();
                                        } else if (planList.size() == 0 && ReloadManagementActivity.searchedContents.getVisibility() == View.VISIBLE) {
                                            ReloadManagementActivity.reloadPlansRecyclerView.setVisibility(View.INVISIBLE);
                                            ReloadManagementActivity.notFound.setVisibility(View.VISIBLE);
                                            ReloadManagementActivity.search.hide();
                                            ReloadManagementActivity.toEnd.hide();
                                        }

                                        successfulToastMaker("درخواست انتقال طرح با موفقیت ثبت شد.", new View(context));
                                        transferDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    errorToastMaker("خطای سرویس انتقال طرح", new View(context));
                                    e.printStackTrace();
                                    transferDialog.dismiss();
                                    confirmDialog.dismiss();
                                }
                            },
                            error -> {
                                errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                                transferDialog.dismiss();
                                error.printStackTrace();
                                progressBar.setVisibility(View.INVISIBLE);
                                bottomButtons.setVisibility(View.VISIBLE);
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

    private void reloadPlan(PlanModal plan, int position) {
        dbHandler.deletePlan(plan.getPlanID(), plan.getUserID());
        dbHandler.close();
        planList.remove(plan);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, planList.size());
        successfulToastMaker("بارگذاری مجدد طرح با موفقیت انجام شد.", new View(context));
        if (planList.size() > 0)
            ReloadManagementActivity.search.show();
        if (planList.size() == 0 && ReloadManagementActivity.searchedContents.getVisibility() != View.VISIBLE) {
            ReloadManagementActivity.nothingFoundLayout.setVisibility(View.VISIBLE);
            ReloadManagementActivity.search.hide();
        } else if (planList.size() == 0 && ReloadManagementActivity.searchedContents.getVisibility() == View.VISIBLE) {
            ReloadManagementActivity.notFound.setVisibility(View.VISIBLE);

            ReloadManagementActivity.search.hide();
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
