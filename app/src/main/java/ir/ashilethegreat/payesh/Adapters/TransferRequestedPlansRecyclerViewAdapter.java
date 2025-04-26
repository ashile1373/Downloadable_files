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
import ir.ashilethegreat.payesh.Fragments.TransferRequestedPlansFragment;
import ir.ashilethegreat.payesh.R;


public class TransferRequestedPlansRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int DEFAULT_TIMEOUT = 20000;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    final ArrayList<PlanModal> planList;
    final ArrayList<UserModal> sendersList;
    final ArrayList<UserModal> receiversList;
    final ArrayList<LicenseModal> licenseList;
    final ArrayList<FacilityModal> facilityList;
    final ArrayList<CounselingModal> counselingList;
    final ArrayList<CultivationModal> cultivationList;
    final ArrayList<EducationModal> educationList;
    final ArrayList<IdentificationModal> identificationList;
    final ArrayList<MarketingModal> marketingList;
    final ArrayList<NotificationModal> notificationList;
    final ArrayList<ProvidingInfrastructureModal> providingInfrastructureList;
    final ArrayList<ProvidingTechnologyModal> providingTechnologyList;

    ArrayList<UserModal> userList;
    ArrayList<String> userNames;
    Context context;
    final DBHandler dbHandler;
    boolean holdTheClick = false;
    final RequestQueue requestQueue;
    Dialog moreDetails, transferDialog;
    JSONObject jsonBody;
    int selectedPosition;


    public TransferRequestedPlansRecyclerViewAdapter(ArrayList<PlanModal> planList, ArrayList<UserModal> sendersList, ArrayList<UserModal> receiversList, ArrayList<CounselingModal> counselingArrayList,
                                                     ArrayList<CultivationModal> cultivationArrayList, ArrayList<EducationModal> educationArrayList, ArrayList<FacilityModal> facilityArrayList,
                                                     ArrayList<IdentificationModal> identificationArrayList, ArrayList<LicenseModal> licenseArrayList, ArrayList<MarketingModal> marketingArrayList,
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
        this.context = context;
        this.dbHandler = new DBHandler(context);
        this.sendersList = sendersList;
        this.receiversList = receiversList;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_transfer_requested_plans_recyclerview_item, parent, false);
            return new TransferRequestedPlansRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_transfer_requested_plans_shimmer_layout, parent, false);
            return new TransferRequestedPlansRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PlanModal plan = planList.get(viewHolder.getAbsoluteAdapterPosition());
        UserModal receiver = receiversList.get(viewHolder.getAbsoluteAdapterPosition());
        UserModal sender = sendersList.get(viewHolder.getAbsoluteAdapterPosition());
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_ITEM ->
                    ((TransferRequestedPlansRecyclerViewAdapter.ItemViewHolder) viewHolder).bind(plan, sender, receiver);
            case VIEW_TYPE_LOADING ->
                    ((TransferRequestedPlansRecyclerViewAdapter.LoadingViewHolder) viewHolder).bind();
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
        final TextView transferFrom;
        final TextView transferTo;
        final LinearLayout cancelTransferLayout;
        final RelativeLayout bottomBody;
        final ProgressBar loading;
        ImageView more;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            cancelTransferLayout = itemView.findViewById(R.id.cancelTransferLayout);
            address = itemView.findViewById(R.id.address);
            transferFrom = itemView.findViewById(R.id.transferFrom);
            transferTo = itemView.findViewById(R.id.transferTo);
            loading = itemView.findViewById(R.id.loading);
            more = itemView.findViewById(R.id.more);
            bottomBody = itemView.findViewById(R.id.bottomBody);
        }

        void bind(PlanModal plan, UserModal sender, UserModal receiver) {
            PlanPosition.setText(String.valueOf(plan.getPlanID()));
            name.setText(String.format("%s %s", plan.getName(), plan.getFamilyName()));
            transferTo.setText(String.format("%s %s", receiver.getFirstName(), receiver.getLastName()));
            planTitle.setText(dbHandler.readPlanTitleFromID(plan.getPlanTitle()));
            transferTo.setText(String.format("%s %s", receiver.getFirstName(), receiver.getLastName()));
            transferFrom.setText(String.format("%s %s", sender.getFirstName(), sender.getLastName()));

            if (!plan.getPhone().equalsIgnoreCase("null") && !plan.getPhone().equalsIgnoreCase(""))
                phoneNumber.setText(plan.getPhone());
            else phoneNumber.setText("مشخص نشده");

            address.setText(String.format("%s،%s", dbHandler.readProvinceNameFromID(plan.getProvince()), dbHandler.readCityNameFromID(plan.getTownship())));

            cancelTransferLayout.setOnClickListener(v -> {
                createDeleteDialog(plan, getBindingAdapterPosition(), new View(context), loading, bottomBody);
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

                Objects.requireNonNull(transferPlansManagementLayout).setOnClickListener(view1 -> {
                    createTransferDialog(plan, getAbsoluteAdapterPosition());
                    bottomSheetDialog.cancel();
                });

                Objects.requireNonNull(inquiryLayout).setOnClickListener(view1 -> {
                    Intent intent = new Intent(context, InquiryActivity.class);
                    intent.putExtra("natCode", plan.getNatCode());
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

    private void removeSelectedPlan(PlanModal plan, int position, View view, View loading, View bottomBody) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("id_inspection", plan.getPlanID());
            jsonBody.put("national_code", plan.getNatCode());
        } catch (JSONException e) {
            errorToastMaker("خطای ساخت اطلاعات ارسالی", view);
        }

        //USER ID IS ACTUALLY TRANSFER ID
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLCancelPlanTransferRequest) + plan.getUserID(), jsonBody,
                response -> {

//                    Log.i("test", plan.getInspectionID());
                    try {
                        if (response.getString("status").equals("Token is Expired"))
                            refreshAccessToken();
                        if (response.getString("status").equals("1")) {
                            removeFromTransferList(plan, position);
                            dbHandler.close();
                        } else if (response.getString("status").equals("0")) {
                            alertToastMaker("در حال حاضر طرح قابل حذف کردن از لیست نیست!", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomBody.setVisibility(View.VISIBLE);
                        } else {
                            alertToastMaker("در ارتباط با سرور مشکلی رخ داد. \n دوباره تلاش کنید.", view);
                            loading.setVisibility(View.INVISIBLE);
                            bottomBody.setVisibility(View.VISIBLE);
                        }
                        holdTheClick = false;
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس گرفتن طرح", view);
                    }
                },
                error -> {
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
                headers.put("Authorization", "Bearer " + dbHandler.getUser().getToken());
                return headers;
            }
        };
        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void createDeleteDialog(PlanModal plan, int position, View view, View loading, View bottomBody) {
        Dialog delete = new Dialog(context);
        delete.setContentView(R.layout.custom_transfer_cancel_confirmation_dialog_layout);
        Objects.requireNonNull(delete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ExtendedFloatingActionButton confirm, cancel;

        confirm = delete.findViewById(R.id.confirm);
        cancel = delete.findViewById(R.id.cancel);

        cancel.setOnClickListener(v -> delete.dismiss());
        confirm.setOnClickListener(v -> {
            loading.setVisibility(View.VISIBLE);
            bottomBody.setVisibility(View.INVISIBLE);
            removeSelectedPlan(plan, position, view, loading, bottomBody);
            delete.dismiss();
        });

        delete.show();
    }

    private void removeFromTransferList(PlanModal plan, int position) {

        planList.remove(plan);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, planList.size());
        successfulToastMaker("طرح از لیست در انتظار انتقال حذف شد!", new View(context));
        if (planList.size() > 0)
            TransferRequestedPlansFragment.search.show();
        if (planList.size() == 0 && TransferRequestedPlansFragment.searchedContents.getVisibility() != View.VISIBLE) {
            TransferRequestedPlansFragment.allDone.setVisibility(View.VISIBLE);
            TransferRequestedPlansFragment.search.hide();
        } else if (planList.size() == 0 && TransferRequestedPlansFragment.searchedContents.getVisibility() == View.VISIBLE) {
            TransferRequestedPlansFragment.notFound.setVisibility(View.VISIBLE);
            TransferRequestedPlansFragment.search.hide();
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
        userList = new ArrayList<>();
        userNames = new ArrayList<>();
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
                confirmDialog.setContentView(R.layout.custom_transfer_to_another_user_confirmation_dialog_layout);
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
                                        UserModal changedReceiver = receiversList.get(planPosition);
                                        changedReceiver.setFirstName(userNames.get(selectedPosition).substring(3));
                                        changedReceiver.setLastName("");
                                        receiversList.add(planPosition, changedReceiver);
                                        notifyItemChanged(planPosition);
                                        successfulToastMaker("درخواست تغییر انتقال طرح با موفقیت ثبت شد.", new View(context));
                                        transferDialog.dismiss();
                                        confirmDialog.dismiss();
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
}