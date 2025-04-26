package ir.ashilethegreat.payesh.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import ir.ashilethegreat.payesh.Fragments.TransferDonePlansFragment;
import ir.ashilethegreat.payesh.Fragments.TransferRequestedPlansFragment;

public class TransferViewAdapter extends FragmentStateAdapter {
    public TransferViewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new TransferRequestedPlansFragment();
        else return new TransferDonePlansFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
