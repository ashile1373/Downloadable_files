package ir.ashilethegreat.payesh.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.ashilethegreat.payesh.Fragments.InputNewPlansFragment;
import ir.ashilethegreat.payesh.Fragments.InputReturnedPlansFragment;

public class InputRequestsAdapter extends FragmentStateAdapter {
    public InputRequestsAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new InputNewPlansFragment();
        else return new InputReturnedPlansFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
