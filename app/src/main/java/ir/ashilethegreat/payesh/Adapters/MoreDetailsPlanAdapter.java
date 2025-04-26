package ir.ashilethegreat.payesh.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.ashilethegreat.payesh.Fragments.MorePlanDetailEcosystemRequirementFragment;
import ir.ashilethegreat.payesh.Fragments.MorePlanDetailsFragment;
import ir.ashilethegreat.payesh.Fragments.MorePlanDetailsOwnerInfoFragment;

public class MoreDetailsPlanAdapter extends FragmentStateAdapter {
    public MoreDetailsPlanAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new MorePlanDetailsOwnerInfoFragment();
        else if (position == 1) return new MorePlanDetailsFragment();
        else return new MorePlanDetailEcosystemRequirementFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
