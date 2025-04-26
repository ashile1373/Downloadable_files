package ir.ashilethegreat.payesh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.IdentificationModal;
import ir.ashilethegreat.payesh.R;

public class EcosystemIdentificationsRecyclerViewAdapter extends RecyclerView.Adapter<EcosystemIdentificationsRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<IdentificationModal> list;
    DBHandler dbHandler;
    Context context;

    public EcosystemIdentificationsRecyclerViewAdapter(ArrayList<IdentificationModal> list , Context context) {
        this.list = list;
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public EcosystemIdentificationsRecyclerViewAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ecosystem_recyclerview_item, parent, false);
        return new EcosystemIdentificationsRecyclerViewAdapter.ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EcosystemIdentificationsRecyclerViewAdapter.ModelViewHolder holder, int position) {
        if (list.get(holder.getAbsoluteAdapterPosition()).getOrganID() != null)
            holder.organ.setText(dbHandler.readOrganNameFromID(list.get(holder.getAbsoluteAdapterPosition()).getOrganID()));
        else holder.organ.setText("");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView organ;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            organ = itemView.findViewById(R.id.organ);

        }

    }


}