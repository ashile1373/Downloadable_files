package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import ir.ashilethegreat.payesh.Adapters.TransferViewAdapter;
import ir.ashilethegreat.payesh.R;

public class TransferPlansManagementActivity extends AppCompatActivity {

    ImageView back;
    TabLayout tabLayout;
    ViewPager2 viewpager;
    TransferViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_plans_management);

        back = findViewById(R.id.back);

        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new TransferViewAdapter(fragmentManager, getLifecycle());
        viewpager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("طرح های در انتظار انتقال"));
        tabLayout.addTab(tabLayout.newTab().setText("طرح های منتقل شده"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

        back.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(TransferPlansManagementActivity.this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(TransferPlansManagementActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(TransferPlansManagementActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}