package ir.ashilethegreat.payesh.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.R;

public class ProfileDetailsRecyclerViewAdapter extends RecyclerView.Adapter<ProfileDetailsRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<String> profileDetailsNames;
    final ArrayList<String> profileDetailsTitles;
    Context context;

    public ProfileDetailsRecyclerViewAdapter(ArrayList<String> profileDetailsNames ,ArrayList<String> profileDetailsTitles ) {
        this.profileDetailsNames = profileDetailsNames;
        this.profileDetailsTitles = profileDetailsTitles;
    }


    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_profile_details_recyclerview_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.profileTitle.setText(profileDetailsTitles.get(holder.getAbsoluteAdapterPosition()));
        holder.profileText.setText(profileDetailsNames.get(holder.getAbsoluteAdapterPosition()));

    }

    @Override
    public int getItemCount() {
        return profileDetailsNames.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView profileTitle;
        final TextView profileText;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            profileTitle = itemView.findViewById(R.id.profileTitle);
            profileText = itemView.findViewById(R.id.profileText);
        }

    }

}
