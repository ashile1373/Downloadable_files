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

public class ShowOwnerInfoFragment extends Fragment {

    TextView name, parentName, natCode, phone, fixedPhone, birthday, startWorkingDate, education, fieldOfStudy, fieldOfStudyText,
            specialization, specializationText, households, endOfView;
    NestedScrollView nestedScrollView;
    DBHandler dbHandler;
    String planId;

    public ShowOwnerInfoFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_owner_info, container, false);

        nestedScrollView = view.findViewById(R.id.nestedScrollView);
        name = view.findViewById(R.id.name);
        parentName = view.findViewById(R.id.parentName);
        natCode = view.findViewById(R.id.natCode);
        phone = view.findViewById(R.id.phone);
        fixedPhone = view.findViewById(R.id.fixedPhone);
        birthday = view.findViewById(R.id.birthday);
        startWorkingDate = view.findViewById(R.id.startWorkingDate);
        education = view.findViewById(R.id.educationOne);
        fieldOfStudy = view.findViewById(R.id.fieldOfStudy);
        fieldOfStudyText = view.findViewById(R.id.fieldOfStudyText);
        specialization = view.findViewById(R.id.specialization);
        specializationText = view.findViewById(R.id.specializationText);
        households = view.findViewById(R.id.households);
        endOfView = view.findViewById(R.id.endOfView);

        dbHandler = new DBHandler(getActivity());
        planId = Objects.requireNonNull(requireActivity().getIntent().getExtras()).getString("planId");
        PlanModal planModal = dbHandler.planSelectRow(planId);

        name.setText(String.format("%s %s", planModal.getName(), planModal.getFamilyName()));
        parentName.setText(planModal.getParentName());
        natCode.setText(planModal.getNatCode());

        if (!planModal.getPhone().equals(""))
            phone.setText(planModal.getPhone());
        else phone.setText("مشخص نشده");

        if (!planModal.getFixedPhone().equals(""))
            fixedPhone.setText(planModal.getFixedPhone());
        else fixedPhone.setText("مشخص نشده");

        birthday.setText(planModal.getBirthday().replace("-", "/"));

        if (!planModal.getPlanStartDate().equals(""))
            startWorkingDate.setText(planModal.getPlanStartDate());
        else startWorkingDate.setText("مشخص نشده");

        if (planModal.getEducation() != null) {
            if (!planModal.getEducation().equals("")) {
                education.setText(dbHandler.readEducationFromID(planModal.getEducation()));
                if (planModal.getEducation().equals("6") || planModal.getEducation().equals("7") || planModal.getEducation().equals("8") ||
                        planModal.getEducation().equals("9") || planModal.getEducation().equals("10")) {
                    fieldOfStudy.setVisibility(View.VISIBLE);
                    fieldOfStudyText.setVisibility(View.VISIBLE);
                    specialization.setVisibility(View.VISIBLE);
                    specializationText.setVisibility(View.VISIBLE);
                    if (planModal.getFieldOfStudy() != null) {
                        if (!planModal.getFieldOfStudy().equals(""))
                            fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(planModal.getFieldOfStudy()));
                        else fieldOfStudy.setText("مشخص نشده");
                    } else fieldOfStudy.setText("مشخص نشده");
                    if (planModal.getSpecialization() != null) {
                        if (!planModal.getSpecialization().equals(""))
                            specialization.setText(dbHandler.readSpecializationFromID(planModal.getSpecialization()));
                        else specialization.setText("مشخص نشده");
                    } else specialization.setText("مشخص نشده");
                }
            }
            else education.setText("مشخص نشده");
        } else education.setText("مشخص نشده");

        if (planModal.getHouseholds() != null) {
            if (!planModal.getHouseholds().equals(""))
                households.setText(dbHandler.readHouseholdsFromID(planModal.getHouseholds()));
            else households.setText("مشخص نشده");
        } else households.setText("مشخص نشده");


        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            Rect scrollBounds = new Rect();
            nestedScrollView.getHitRect(scrollBounds);

            if (endOfView.getLocalVisibleRect(scrollBounds)) {
                DataViewActivity.flag1 = true;

                if (DataViewActivity.flag2 && DataViewActivity.flag3) {

                    DataViewActivity.bottomBar.setVisibility(View.VISIBLE);
                    DataViewActivity.bottomBar2.animate().translationY(DataViewActivity.bottomBar2.getHeight()).alpha(0.0f);
                }
            }
        });

        return view;
    }
}