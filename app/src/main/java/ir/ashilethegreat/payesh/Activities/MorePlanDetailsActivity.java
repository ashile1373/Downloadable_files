package ir.ashilethegreat.payesh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.Adapters.MoreDetailsPlanAdapter;
import ir.ashilethegreat.payesh.DBhandlers.CounselingModal;
import ir.ashilethegreat.payesh.DBhandlers.CultivationModal;
import ir.ashilethegreat.payesh.DBhandlers.EducationModal;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.DBhandlers.IdentificationModal;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.DBhandlers.MarketingModal;
import ir.ashilethegreat.payesh.DBhandlers.NotificationModal;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingInfrastructureModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingTechnologyModal;
import ir.ashilethegreat.payesh.R;

public class MorePlanDetailsActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewpager;
    MoreDetailsPlanAdapter adapter;
    LinearLayout back;
    static public PlanModal planModal;
    static public ArrayList<LicenseModal> licenseModalArrayList;
    static public ArrayList<FacilityModal> facilityModalArrayList;
    static public ArrayList<CounselingModal> counselingModalArrayList;
    static public ArrayList<CultivationModal> cultivationModalArrayList;
    static public ArrayList<EducationModal> educationModalArrayList;
    static public ArrayList<IdentificationModal> identificationModalArrayList;
    static public ArrayList<MarketingModal> marketingModalArrayList;
    static public ArrayList<NotificationModal> notificationModalArrayList;
    static public ArrayList<ProvidingInfrastructureModal> providingInfrastructureModalArrayList;
    static public ArrayList<ProvidingTechnologyModal> providingTechnologyModalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_plan_details);

        back = findViewById(R.id.back);
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.viewPager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new MoreDetailsPlanAdapter(fragmentManager, getLifecycle());
        viewpager.setAdapter(adapter);

        tabLayout.addTab(tabLayout.newTab().setText("مشخصات مجری"));
        tabLayout.addTab(tabLayout.newTab().setText("جزئیات طرح"));
        tabLayout.addTab(tabLayout.newTab().setText("زیست بوم"));

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
        overridePendingTransition(0,0);
    }
}