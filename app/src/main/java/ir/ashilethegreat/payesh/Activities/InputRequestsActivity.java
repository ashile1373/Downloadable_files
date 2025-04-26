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

import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.InputRequestsAdapter;
import ir.ashilethegreat.payesh.Fragments.InputNewPlansFragment;
import ir.ashilethegreat.payesh.R;

public class InputRequestsActivity extends AppCompatActivity {

    ImageView back;
    public static TabLayout tabLayout;
    ViewPager2 viewpager;
    InputRequestsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_requests);

        back = findViewById(R.id.back);
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new InputRequestsAdapter(fragmentManager, getLifecycle());
        viewpager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("طرح های جدید"));
        tabLayout.addTab(tabLayout.newTab().setText("طرح های نیاز به اصلاح"));

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
//        if (searchLayout.getVisibility() == View.VISIBLE)
//            searchClose.performClick();
//        else if (searchedContents.getVisibility() == View.VISIBLE)
//            searchedContentsClose.performClick();
//        else {
            super.onBackPressed();
            Intent intent = new Intent(InputRequestsActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
//        }
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(InputRequestsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(InputRequestsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}