package ir.ashilethegreat.payesh.Adapters;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
import ir.ashilethegreat.payesh.R;


public class TransferDonePlansRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    Context context;
    final DBHandler dbHandler;
    final RequestQueue requestQueue;
    Dialog moreDetails;

    public TransferDonePlansRecyclerViewAdapter(ArrayList<PlanModal> planList, ArrayList<UserModal> sendersList, ArrayList<UserModal> receiversList, ArrayList<CounselingModal> counselingArrayList,
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
        this.sendersList = sendersList;
        this.receiversList = receiversList;
        this.context = context;
        this.dbHandler = new DBHandler(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_transfer_done_plans_recyclerview_item, parent, false);
            return new TransferDonePlansRecyclerViewAdapter.ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_transfer_done_plans_shimmer_layout, parent, false);
            return new TransferDonePlansRecyclerViewAdapter.LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PlanModal plan = planList.get(viewHolder.getAbsoluteAdapterPosition());
        UserModal sender = sendersList.get(viewHolder.getAbsoluteAdapterPosition());
        UserModal receiver = receiversList.get(viewHolder.getAbsoluteAdapterPosition());
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_ITEM ->
                    ((TransferDonePlansRecyclerViewAdapter.ItemViewHolder) viewHolder).bind(plan, sender, receiver);
            case VIEW_TYPE_LOADING ->
                    ((TransferDonePlansRecyclerViewAdapter.LoadingViewHolder) viewHolder).bind();
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
        final TextView transferTo;
        final TextView transferFrom;
        ImageView more;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            transferTo = itemView.findViewById(R.id.transferTo);
            transferFrom = itemView.findViewById(R.id.transferFrom);
            address = itemView.findViewById(R.id.address);
            more = itemView.findViewById(R.id.more);
        }

        void bind(PlanModal plan, UserModal sender, UserModal receiver) {
            PlanPosition.setText(String.valueOf(plan.getPlanID()));
            name.setText(String.format("%s %s", plan.getName(), plan.getFamilyName()));
            planTitle.setText(dbHandler.readPlanTitleFromID(plan.getPlanTitle()));
            transferTo.setText(String.format("%s %s", receiver.getFirstName(), receiver.getLastName()));
            transferFrom.setText(String.format("%s %s", sender.getFirstName(), sender.getLastName()));
            if (!plan.getPhone().equalsIgnoreCase("null") && !plan.getPhone().equalsIgnoreCase(""))
                phoneNumber.setText(plan.getPhone());
            else phoneNumber.setText("مشخص نشده");
            address.setText(String.format("%s،%s", dbHandler.readProvinceNameFromID(plan.getProvince()), dbHandler.readCityNameFromID(plan.getTownship())));

            more.setOnClickListener(v -> {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                bottomSheetDialog.setContentView(R.layout.custom_manage_input_plans_layout);

                LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
                LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
                LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);

                Objects.requireNonNull(transferPlansManagementLayout).setVisibility(View.GONE);

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