package ir.ashilethegreat.payesh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.Activities.ConfirmPlanFacilityActivity;
import ir.ashilethegreat.payesh.DBhandlers.BailModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;


public class BailRecyclerViewAdapter extends RecyclerView.Adapter<BailRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<BailModal> bailList;
    Context context;
    final DBHandler dbHandler;

    public BailRecyclerViewAdapter(ArrayList<BailModal> bailList, Context context) {
        this.bailList = bailList;
        this.context = context;
        this.dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_confirm_plan_facility_bail_recyclerview_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.bail.setText(dbHandler.readPlanBailsNameFromID(bailList.get(holder.getAbsoluteAdapterPosition()).getConfirmBailName()));

        holder.deleteBailLayout.setOnClickListener(view -> {
            dbHandler.removePlanBail(bailList.get(holder.getBindingAdapterPosition()).getConfirmBailID());
            bailList.remove(holder.getBindingAdapterPosition());
            notifyItemRemoved(holder.getBindingAdapterPosition());

            if (bailList.size() == 0){
                ConfirmPlanFacilityActivity.bailsRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bailList.size();
    }

    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView bail;
        final LinearLayout deleteBailLayout;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            bail = itemView.findViewById(R.id.bail);
            deleteBailLayout = itemView.findViewById(R.id.deleteBailLayout);
        }

    }

}
