package ir.ashilethegreat.payesh.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.ashilethegreat.payesh.Fragments.ShowEcosystemRequirementFragment;
import ir.ashilethegreat.payesh.Fragments.ShowOwnerInfoFragment;
import ir.ashilethegreat.payesh.Fragments.ShowPlanDetailsFragment;

public class DataViewAdapter extends FragmentStateAdapter {
    public DataViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new ShowOwnerInfoFragment();
        else if (position == 1) return new ShowPlanDetailsFragment();
        else return new ShowEcosystemRequirementFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
