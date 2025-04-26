package ir.ashilethegreat.payesh.Fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.Activities.DataViewActivity;
import ir.ashilethegreat.payesh.Adapters.EcosystemCounselingsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemCultivationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemEducationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemFinanceRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemIdentificationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemLicenseRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemMarketingRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemNotificationsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemProvidingInfrastructuresRecyclerViewAdapter;
import ir.ashilethegreat.payesh.Adapters.EcosystemProvidingTechnologiesRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.CounselingModal;
import ir.ashilethegreat.payesh.DBhandlers.CultivationModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.EducationModal;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.DBhandlers.IdentificationModal;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.DBhandlers.MarketingModal;
import ir.ashilethegreat.payesh.DBhandlers.NotificationModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingInfrastructureModal;
import ir.ashilethegreat.payesh.DBhandlers.ProvidingTechnologyModal;
import ir.ashilethegreat.payesh.R;

public class ShowEcosystemRequirementFragment extends Fragment {

    Button licenseText, facilityText, counselingText, marketingText, educationText, providingTechnologyText,
            notificationText, providingInfrastructureText, identificationText, cultivationText;
    RecyclerView licensesRecyclerView, facilitiesRecyclerView, counselingsRecyclerView, marketingsRecyclerView, educationsRecyclerView, providingTechnologiesRecyclerView,
            notificationsRecyclerView, providingInfrastructuresRecyclerView, identificationsRecyclerView, cultivationsRecyclerView;
    TextView endOfView;
    NestedScrollView nestedScrollView;

    DBHandler dbHandler;
    String planId;

    public ShowEcosystemRequirementFragment() {
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
        View view = inflater.inflate(R.layout.fragment_show_ecosystem_requirement, container, false);
        licenseText = view.findViewById(R.id.licenseText);
        facilityText = view.findViewById(R.id.facilityText);
        counselingText = view.findViewById(R.id.counselingText);
        marketingText = view.findViewById(R.id.marketingText);
        educationText = view.findViewById(R.id.educationText);
        providingTechnologyText = view.findViewById(R.id.providingTechnologyText);
        notificationText = view.findViewById(R.id.notificationText);
        providingInfrastructureText = view.findViewById(R.id.providingInfrastructureText);
        identificationText = view.findViewById(R.id.identificationText);
        cultivationText = view.findViewById(R.id.cultivationText);
        licensesRecyclerView = view.findViewById(R.id.licenseRecyclerView);
        facilitiesRecyclerView = view.findViewById(R.id.facilitiesRecyclerView);
        counselingsRecyclerView = view.findViewById(R.id.counselingsRecyclerView);
        marketingsRecyclerView = view.findViewById(R.id.marketingRecyclerView);
        educationsRecyclerView = view.findViewById(R.id.educationRecyclerView);
        providingTechnologiesRecyclerView = view.findViewById(R.id.providingTechnologyRecyclerView);
        notificationsRecyclerView = view.findViewById(R.id.notificationRecyclerView);
        providingInfrastructuresRecyclerView = view.findViewById(R.id.providingInfrastructureRecyclerView);
        identificationsRecyclerView = view.findViewById(R.id.identificationRecyclerView);
        cultivationsRecyclerView = view.findViewById(R.id.cultivationRecyclerView);
        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        endOfView = view.findViewById(R.id.endOfView);

        dbHandler = new DBHandler(getContext());
        planId = Objects.requireNonNull(requireActivity().getIntent().getExtras()).getString("planId");

        ArrayList<LicenseModal> licenses = dbHandler.readLicenses(planId);
        if (licenses.size() > 0) {
            licensesRecyclerView.setVisibility(View.VISIBLE);
            licenseText.setVisibility(View.GONE);
            EcosystemLicenseRecyclerViewAdapter adapter = new EcosystemLicenseRecyclerViewAdapter(licenses,licenses, true, requireActivity());
            licensesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            licensesRecyclerView.setAdapter(adapter);
        } else {
            licensesRecyclerView.setVisibility(View.GONE);
            licenseText.setVisibility(View.VISIBLE);
        }

        ArrayList<FacilityModal> facilities = dbHandler.readFacilities(planId);
        if (facilities.size() > 0) {
            facilitiesRecyclerView.setVisibility(View.VISIBLE);
            facilityText.setVisibility(View.GONE);
            EcosystemFinanceRecyclerViewAdapter adapter = new EcosystemFinanceRecyclerViewAdapter(facilities,facilities, true, requireActivity());
            facilitiesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            facilitiesRecyclerView.setAdapter(adapter);
        } else {
            facilitiesRecyclerView.setVisibility(View.GONE);
            facilityText.setVisibility(View.VISIBLE);
        }

        ArrayList<CounselingModal> counselings = dbHandler.readCounselings(planId);
        if (counselings.size() > 0) {
            counselingsRecyclerView.setVisibility(View.VISIBLE);
            counselingText.setVisibility(View.GONE);
            EcosystemCounselingsRecyclerViewAdapter adapter = new EcosystemCounselingsRecyclerViewAdapter(counselings, requireActivity());
            counselingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            counselingsRecyclerView.setAdapter(adapter);
        } else {
            counselingsRecyclerView.setVisibility(View.GONE);
            counselingText.setVisibility(View.VISIBLE);
        }

        ArrayList<CultivationModal> cultivation = dbHandler.readCultivations(planId);
        if (cultivation.size() > 0) {
            cultivationsRecyclerView.setVisibility(View.VISIBLE);
            cultivationText.setVisibility(View.GONE);
            EcosystemCultivationsRecyclerViewAdapter adapter = new EcosystemCultivationsRecyclerViewAdapter(cultivation, requireActivity());
            cultivationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            cultivationsRecyclerView.setAdapter(adapter);
        } else {
            cultivationsRecyclerView.setVisibility(View.GONE);
            cultivationText.setVisibility(View.VISIBLE);
        }

        ArrayList<EducationModal> educations = dbHandler.readEducations(planId);
        if (educations.size() > 0) {
            educationsRecyclerView.setVisibility(View.VISIBLE);
            educationText.setVisibility(View.GONE);
            EcosystemEducationsRecyclerViewAdapter adapter = new EcosystemEducationsRecyclerViewAdapter(educations, requireActivity());
            educationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            educationsRecyclerView.setAdapter(adapter);
        } else {
            educationsRecyclerView.setVisibility(View.GONE);
            educationText.setVisibility(View.VISIBLE);
        }

        ArrayList<IdentificationModal> identifications = dbHandler.readIdentifications(planId);
        if (identifications.size() > 0) {
            identificationsRecyclerView.setVisibility(View.VISIBLE);
            identificationText.setVisibility(View.GONE);
            EcosystemIdentificationsRecyclerViewAdapter adapter = new EcosystemIdentificationsRecyclerViewAdapter(identifications, requireActivity());
            identificationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            identificationsRecyclerView.setAdapter(adapter);
        } else {
            identificationsRecyclerView.setVisibility(View.GONE);
            identificationText.setVisibility(View.VISIBLE);
        }

        ArrayList<MarketingModal> marketing = dbHandler.readMarketings(planId);
        if (marketing.size() > 0) {
            marketingsRecyclerView.setVisibility(View.VISIBLE);
            marketingText.setVisibility(View.GONE);
            EcosystemMarketingRecyclerViewAdapter adapter = new EcosystemMarketingRecyclerViewAdapter(marketing, requireActivity());
            marketingsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            marketingsRecyclerView.setAdapter(adapter);
        } else {
            marketingsRecyclerView.setVisibility(View.GONE);
            marketingText.setVisibility(View.VISIBLE);
        }

        ArrayList<NotificationModal> notifications = dbHandler.readNotifications(planId);
        if (notifications.size() > 0) {
            notificationsRecyclerView.setVisibility(View.VISIBLE);
            notificationText.setVisibility(View.GONE);
            EcosystemNotificationsRecyclerViewAdapter adapter = new EcosystemNotificationsRecyclerViewAdapter(notifications, requireActivity());
            notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            notificationsRecyclerView.setAdapter(adapter);
        } else {
            notificationsRecyclerView.setVisibility(View.GONE);
            notificationText.setVisibility(View.VISIBLE);
        }

        ArrayList<ProvidingInfrastructureModal> providingInfrastructures = dbHandler.readProvidingInfrastructures(planId);
        if (providingInfrastructures.size() > 0) {
            providingInfrastructuresRecyclerView.setVisibility(View.VISIBLE);
            providingInfrastructureText.setVisibility(View.GONE);
            EcosystemProvidingInfrastructuresRecyclerViewAdapter adapter = new EcosystemProvidingInfrastructuresRecyclerViewAdapter(providingInfrastructures, requireActivity());
            providingInfrastructuresRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            providingInfrastructuresRecyclerView.setAdapter(adapter);
        } else {
            providingInfrastructuresRecyclerView.setVisibility(View.GONE);
            providingInfrastructureText.setVisibility(View.VISIBLE);
        }


        ArrayList<ProvidingTechnologyModal> providingTechnologies = dbHandler.readProvidingTechnologies(planId);
        if (providingTechnologies.size() > 0) {
            providingTechnologiesRecyclerView.setVisibility(View.VISIBLE);
            providingTechnologyText.setVisibility(View.GONE);
            EcosystemProvidingTechnologiesRecyclerViewAdapter adapter = new EcosystemProvidingTechnologiesRecyclerViewAdapter(providingTechnologies, requireActivity());
            providingTechnologiesRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            providingTechnologiesRecyclerView.setAdapter(adapter);
        } else {
            providingTechnologiesRecyclerView.setVisibility(View.GONE);
            providingTechnologyText.setVisibility(View.VISIBLE);
        }

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            Rect scrollBounds = new Rect();
            nestedScrollView.getHitRect(scrollBounds);

            if (endOfView.getLocalVisibleRect(scrollBounds)) {
                DataViewActivity.flag3 = true;
                if (DataViewActivity.flag2 && DataViewActivity.flag1) {

                    DataViewActivity.bottomBar.setVisibility(View.VISIBLE);
                    DataViewActivity.bottomBar2.animate().translationY(DataViewActivity.bottomBar2.getHeight()).alpha(0.0f);
                }
            }
        });

        return view;
    }
}