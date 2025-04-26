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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.Activities.InquiryActivity;
import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;


public class SentRecyclerViewAdapter extends RecyclerView.Adapter<SentRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<PlanModal> planList;
    Context context;
    final DBHandler dbHandler;

    public SentRecyclerViewAdapter(ArrayList<PlanModal> planList, Context context) {
        this.planList = planList;
        this.context = context;
        this.dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sent_recyclerview_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.PlanPosition.setText(planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
        holder.name.setText(String.format("%s %s", planList.get(holder.getAbsoluteAdapterPosition()).getName(), planList.get(holder.getAbsoluteAdapterPosition()).getFamilyName()));
        holder.planTitle.setText(dbHandler.readPlanTitleFromID(planList.get(holder.getAbsoluteAdapterPosition()).getPlanTitle()));
        holder.phoneNumber.setText(planList.get(holder.getAbsoluteAdapterPosition()).getPhone());
        holder.more.setOnClickListener(v -> {
            final Dialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.custom_manage_input_plans_layout);

            LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
            LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
            LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);

            Objects.requireNonNull(transferPlansManagementLayout).setVisibility(View.GONE);

            Objects.requireNonNull(moreDetails).setOnClickListener(view1 -> {
                createDetailsDialog(planList.get(holder.getAbsoluteAdapterPosition()));
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(inquiryLayout).setOnClickListener(view1 -> {
                Intent intent = new Intent(context, InquiryActivity.class);
                intent.putExtra("natCode", planList.get(holder.getAbsoluteAdapterPosition()).getNatCode());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                bottomSheetDialog.cancel();
            });

            bottomSheetDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView PlanPosition;
        final TextView name;
        final TextView planTitle;
        final TextView phoneNumber;
        final ImageView more;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            more = itemView.findViewById(R.id.more);
        }

    }

    private void createDetailsDialog(PlanModal plan) {
        Intent intent = new Intent(context, MorePlanDetailsActivity.class);
        MorePlanDetailsActivity.planModal = plan;
        MorePlanDetailsActivity.providingTechnologyModalArrayList = dbHandler.readProvidingTechnologies(plan.getPlanID());
        MorePlanDetailsActivity.facilityModalArrayList = dbHandler.readFacilities(plan.getPlanID());
        MorePlanDetailsActivity.licenseModalArrayList = dbHandler.readLicenses(plan.getPlanID());
        MorePlanDetailsActivity.counselingModalArrayList = dbHandler.readCounselings(plan.getPlanID());
        MorePlanDetailsActivity.cultivationModalArrayList = dbHandler.readCultivations(plan.getPlanID());
        MorePlanDetailsActivity.educationModalArrayList = dbHandler.readEducations(plan.getPlanID());
        MorePlanDetailsActivity.marketingModalArrayList = dbHandler.readMarketings(plan.getPlanID());
        MorePlanDetailsActivity.identificationModalArrayList = dbHandler.readIdentifications(plan.getPlanID());
        MorePlanDetailsActivity.providingInfrastructureModalArrayList = dbHandler.readProvidingInfrastructures(plan.getPlanID());
        MorePlanDetailsActivity.notificationModalArrayList = dbHandler.readNotifications(plan.getPlanID());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

}
