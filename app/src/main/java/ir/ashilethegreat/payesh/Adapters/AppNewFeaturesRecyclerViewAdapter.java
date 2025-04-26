package ir.ashilethegreat.payesh.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.R;

public class AppNewFeaturesRecyclerViewAdapter extends RecyclerView.Adapter<AppNewFeaturesRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<String> appInfo;

    public AppNewFeaturesRecyclerViewAdapter(ArrayList<String> appInfo) {
        this.appInfo = appInfo;

    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_app_new_features_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.feature.setText(appInfo.get(holder.getAbsoluteAdapterPosition()));
        Log.i("test", holder.getAbsoluteAdapterPosition() + " section:" + appInfo.get(holder.getAbsoluteAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return appInfo.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView feature;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            feature = itemView.findViewById(R.id.feature);

        }

    }


}
