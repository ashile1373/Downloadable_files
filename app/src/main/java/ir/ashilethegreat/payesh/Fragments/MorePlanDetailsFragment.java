package ir.ashilethegreat.payesh.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;


public class MorePlanDetailsFragment extends Fragment {

    TextView planTitle, planEconomicSection, planSituation, planProvince, planCity, planSection, planAddress, endOfView;
    DBHandler dbHandler;
    String planId;

    public MorePlanDetailsFragment() {
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

        dbHandler = new DBHandler(requireActivity());

        if (MorePlanDetailsActivity.planModal.getPlanTitle() != null) {
            if (!MorePlanDetailsActivity.planModal.getPlanTitle().equals(""))
                planTitle.setText(dbHandler.readPlanTitleFromID(MorePlanDetailsActivity.planModal.getPlanTitle()));
            else planTitle.setText("مشخص نشده");
        } else planTitle.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getPlanEconomicSection() != null) {
            if (!MorePlanDetailsActivity.planModal.getPlanEconomicSection().equals(""))
                planEconomicSection.setText(dbHandler.readPlanEconomicSectionFromID(MorePlanDetailsActivity.planModal.getPlanEconomicSection()));
            else planEconomicSection.setText("مشخص نشده");
        } else planEconomicSection.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getPlanSituation() != null) {
            if (!MorePlanDetailsActivity.planModal.getPlanSituation().equals(""))
                planSituation.setText(dbHandler.readPlanSituationFromID(MorePlanDetailsActivity.planModal.getPlanSituation()));
            else planSituation.setText("مشخص نشده");
        } else planSituation.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getProvince() != null) {
            if (!MorePlanDetailsActivity.planModal.getProvince().equals(""))
                planProvince.setText(dbHandler.readProvinceNameFromID(MorePlanDetailsActivity.planModal.getProvince()));
            else planProvince.setText("مشخص نشده");
        } else planProvince.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getTownship() != null) {
            if (!MorePlanDetailsActivity.planModal.getTownship().equals(""))
                planCity.setText(dbHandler.readCityNameFromID(MorePlanDetailsActivity.planModal.getTownship()));
            else planCity.setText("مشخص نشده");
        } else planCity.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getSection() != null) {
            if (!MorePlanDetailsActivity.planModal.getSection().equals(""))
                planSection.setText(dbHandler.readSectionNameFromID(MorePlanDetailsActivity.planModal.getSection()));
            else planSection.setText("مشخص نشده");
        } else planSection.setText("مشخص نشده");

        planAddress.setText(MorePlanDetailsActivity.planModal.getMoreAddress());

        return view;
    }
}