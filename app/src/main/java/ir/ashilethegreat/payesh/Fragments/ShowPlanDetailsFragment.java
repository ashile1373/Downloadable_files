package ir.ashilethegreat.payesh.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import ir.ashilethegreat.payesh.Activities.DataViewActivity;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;


public class ShowPlanDetailsFragment extends Fragment {

    TextView planTitle, planEconomicSection, planSituation, planProvince, planCity, planSection, planAddress, endOfView;
    NestedScrollView nestedScrollView;
    DBHandler dbHandler;
    String planId;

    public ShowPlanDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_plan_details, container, false);

        planTitle = view.findViewById(R.id.planTitle);
        planEconomicSection = view.findViewById(R.id.planEconomicSection);
        planSituation = view.findViewById(R.id.planSituation);
        planProvince = view.findViewById(R.id.planProvince);
        planCity = view.findViewById(R.id.planCity);
        planSection = view.findViewById(R.id.planSection);
        planAddress = view.findViewById(R.id.planAddress);
        endOfView = view.findViewById(R.id.endOfView);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);

        dbHandler = new DBHandler(requireActivity());
        planId = Objects.requireNonNull(requireActivity().getIntent().getExtras()).getString("planId");
        PlanModal planModal = dbHandler.planSelectRow(planId);

        if (planModal.getPlanTitle() != null) {
            if (!planModal.getPlanTitle().equals(""))
                planTitle.setText(dbHandler.readPlanTitleFromID(planModal.getPlanTitle()));
            else planTitle.setText("مشخص نشده");
        } else planTitle.setText("مشخص نشده");

        if (planModal.getPlanEconomicSection() != null) {
            if (!planModal.getPlanEconomicSection().equals(""))
                planEconomicSection.setText(dbHandler.readPlanEconomicSectionFromID(planModal.getPlanEconomicSection()));
            else planEconomicSection.setText("مشخص نشده");
        } else planEconomicSection.setText("مشخص نشده");

        if (planModal.getPlanSituation() != null) {
            if (!planModal.getPlanSituation().equals(""))
                planSituation.setText(dbHandler.readPlanSituationFromID(planModal.getPlanSituation()));
            else planSituation.setText("مشخص نشده");
        } else planSituation.setText("مشخص نشده");

        if (planModal.getProvince() != null) {
            if (!planModal.getProvince().equals(""))
                planProvince.setText(dbHandler.readProvinceNameFromID(planModal.getProvince()));
            else planProvince.setText("مشخص نشده");
        } else planProvince.setText("مشخص نشده");

        if (planModal.getTownship() != null) {
            if (!planModal.getTownship().equals(""))
                planCity.setText(dbHandler.readCityNameFromID(planModal.getTownship()));
            else planCity.setText("مشخص نشده");
        } else planCity.setText("مشخص نشده");

        if (planModal.getSection() != null) {
            if (!planModal.getSection().equals(""))
                planSection.setText(dbHandler.readSectionNameFromID(planModal.getSection()));
            else planSection.setText("مشخص نشده");
        } else planSection.setText("مشخص نشده");

        planAddress.setText(planModal.getMoreAddress());


        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            Rect scrollBounds = new Rect();
            nestedScrollView.getHitRect(scrollBounds);

            if (endOfView.getLocalVisibleRect(scrollBounds)) {
                DataViewActivity.flag2 = true;
                if (DataViewActivity.flag1 && DataViewActivity.flag3) {

                    DataViewActivity.bottomBar.setVisibility(View.VISIBLE);
                    DataViewActivity.bottomBar2.animate().translationY(DataViewActivity.bottomBar2.getHeight()).alpha(0.0f);
                }
            }
        });

        return view;
    }
}