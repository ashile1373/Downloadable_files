package ir.ashilethegreat.payesh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.InquiryModal;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class InquiryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    ArrayList<InquiryModal> planList;
    final DBHandler dbHandler;
    final Context context;
    final RequestQueue requestQueue;

    public InquiryRecyclerViewAdapter(ArrayList<InquiryModal> planList, Context context) {
        this.context = context;
        this.planList = planList;
        dbHandler = new DBHandler(context);
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_inquiry_recyclerview_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.custom_inquiry_spacer_recyclerview_item, parent, false);
            return new SpacerViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        InquiryModal plan = planList.get(viewHolder.getAbsoluteAdapterPosition());
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_ITEM -> ((ItemViewHolder) viewHolder).bind(plan);
            case VIEW_TYPE_LOADING ->
                    ((SpacerViewHolder) viewHolder).bind(plan.getSupervisionName());
        }
    }

    @Override
    public int getItemCount() {
        if (planList == null) return 0;
        return planList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (planList.get(position).getSupervisionName() != null) return VIEW_TYPE_LOADING;
        return VIEW_TYPE_ITEM;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        final TextView planNumber;
        final TextView owner;
        final TextView planTitle;
        final TextView natCode;
        final TextView phone;
        final TextView supervisor;
        final TextView planSituation;
        final TextView planStage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            planNumber = itemView.findViewById(R.id.planNumber);
            owner = itemView.findViewById(R.id.owner);
            planTitle = itemView.findViewById(R.id.planTitle);
            natCode = itemView.findViewById(R.id.natCode);
            phone = itemView.findViewById(R.id.phone);
            supervisor = itemView.findViewById(R.id.supervisor);
            planSituation = itemView.findViewById(R.id.planSituation);
            planStage = itemView.findViewById(R.id.planStage);
        }

        void bind(InquiryModal plan) {
            planNumber.setText(String.valueOf(plan.getPlanID()));
            owner.setText(String.format("%s %s", plan.getName(), plan.getFamilyName()));
            planTitle.setText(dbHandler.readPlanTitleFromID(plan.getPlanTitle()));
            natCode.setText(plan.getNatCode());
            if (!plan.getPhone().equalsIgnoreCase("null") && !plan.getPhone().equalsIgnoreCase(""))
                phone.setText(plan.getPhone());
            else phone.setText("مشخص نشده");
            if (!plan.getInspectorName().equalsIgnoreCase("null") && !plan.getInspectorName().equalsIgnoreCase(""))
                supervisor.setText(plan.getInspectorName());
            else supervisor.setText("");
            if (!plan.getInspectorFamilyName().equalsIgnoreCase("null") && !plan.getInspectorFamilyName().equalsIgnoreCase(""))
                supervisor.setText(String.format("%s %s", supervisor.getText().toString(), plan.getInspectorFamilyName()));

            if (plan.getPlanSituation().equalsIgnoreCase("null") || plan.getPlanSituation().equalsIgnoreCase("")) {
                PlanModal planModal = dbHandler.planSelectRow(plan.getPlanID());
                if (planModal.getPlanFinalSuperVisionSituation() != 0)
                    plan.setPlanSituation(String.valueOf(planModal.getPlanFinalSuperVisionSituation()));
            }
            switch (plan.getPlanSituation()) {
                case "1" -> {
                    planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_green));
                    planSituation.setText("تأیید");
                }
                case "2" -> {
                    planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_red));
                    planSituation.setText("تخلف");
                }
                case "3" -> {
                    planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_blue));
                    planSituation.setText("برگشت طرح");
                }
                case "4" -> {
                    planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_yellow));
                    planSituation.setText("تصحیح");
                }
                default -> {
                    planSituation.setText("مشخص نشده");
                    planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray));
                }
            }

            if (plan.getPlanStage().equalsIgnoreCase("null") || plan.getPlanStage().equalsIgnoreCase("")) {
                planStage.setText("آغاز نشده");
                planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray));
            } else {
                switch (plan.getPlanStage()) {
                    case "1" -> {
                        planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_blue));
                        planStage.setText("آماده نظارت");
                    }
                    case "2" -> {
                        planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_green));
                        planStage.setText("تأیید نهایی");
                    }
                    case "3" -> {
                        planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_yellow));
                        planStage.setText("در حال بررسی");
                    }
                    case "4" -> {
                        planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_red));
                        planStage.setText("نیاز به اصلاح");
                    }
                    default -> {
                        planStage.setText("آغاز نشده");
                        planStage.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray));
                    }
                }
            }


        }
    }

    private static class SpacerViewHolder extends RecyclerView.ViewHolder {

        final TextView title;

        public SpacerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }

        void bind(String s) {
            title.setText(s);
        }

    }

}
