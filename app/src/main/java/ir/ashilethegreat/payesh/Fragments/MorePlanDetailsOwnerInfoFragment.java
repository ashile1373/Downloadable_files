package ir.ashilethegreat.payesh.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.R;

public class MorePlanDetailsOwnerInfoFragment extends Fragment {

    TextView name, parentName, natCode, phone, fixedPhone, birthday, startWorkingDate, education, fieldOfStudy, fieldOfStudyText,
            specialization, specializationText, households, endOfView;
    NestedScrollView nestedScrollView;
    DBHandler dbHandler;
    String planId;

    public MorePlanDetailsOwnerInfoFragment() {
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
        dbHandler = new DBHandler(requireActivity());

        name.setText(String.format("%s %s", MorePlanDetailsActivity.planModal.getName(), MorePlanDetailsActivity.planModal.getFamilyName()));
        parentName.setText(MorePlanDetailsActivity.planModal.getParentName());
        natCode.setText(MorePlanDetailsActivity.planModal.getNatCode());
        if (MorePlanDetailsActivity.planModal.getPhone() != null) {
            if (!MorePlanDetailsActivity.planModal.getPhone().equals(""))
                phone.setText(MorePlanDetailsActivity.planModal.getPhone());
            else phone.setText("مشخص نشده");
        } else phone.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getFixedPhone() != null) {
            if (!MorePlanDetailsActivity.planModal.getFixedPhone().equals(""))
                fixedPhone.setText(MorePlanDetailsActivity.planModal.getFixedPhone());
            else fixedPhone.setText("مشخص نشده");
        } else fixedPhone.setText("مشخص نشده");

        birthday.setText(MorePlanDetailsActivity.planModal.getBirthday().replace("-", "/"));

        if (MorePlanDetailsActivity.planModal.getPlanStartDate() != null) {
            if (!MorePlanDetailsActivity.planModal.getPlanStartDate().equals(""))
                startWorkingDate.setText(MorePlanDetailsActivity.planModal.getPlanStartDate());
            else startWorkingDate.setText("مشخص نشده");
        } else startWorkingDate.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getEducation() != null) {
            if (!MorePlanDetailsActivity.planModal.getEducation().equals("")) {
                education.setText(dbHandler.readEducationFromID(MorePlanDetailsActivity.planModal.getEducation()));
                if (MorePlanDetailsActivity.planModal.getEducation().equals("6") || MorePlanDetailsActivity.planModal.getEducation().equals("7") || MorePlanDetailsActivity.planModal.getEducation().equals("8") ||
                        MorePlanDetailsActivity.planModal.getEducation().equals("9") || MorePlanDetailsActivity.planModal.getEducation().equals("10")) {
                    fieldOfStudy.setVisibility(View.VISIBLE);
                    fieldOfStudyText.setVisibility(View.VISIBLE);
                    specialization.setVisibility(View.VISIBLE);
                    specializationText.setVisibility(View.VISIBLE);
                    if (MorePlanDetailsActivity.planModal.getFieldOfStudy() != null) {
                        if (!MorePlanDetailsActivity.planModal.getFieldOfStudy().equals(""))
                            fieldOfStudy.setText(dbHandler.readFieldOfStudyFromID(MorePlanDetailsActivity.planModal.getFieldOfStudy()));
                        else fieldOfStudy.setText("مشخص نشده");
                    } else fieldOfStudy.setText("مشخص نشده");

                    if (MorePlanDetailsActivity.planModal.getSpecialization() != null) {
                        if (!MorePlanDetailsActivity.planModal.getSpecialization().equals(""))
                            specialization.setText(dbHandler.readSpecializationFromID(MorePlanDetailsActivity.planModal.getSpecialization()));
                        else specialization.setText("مشخص نشده");
                    } else specialization.setText("مشخص نشده");

                }
            } else education.setText("مشخص نشده");
        } else education.setText("مشخص نشده");

        if (MorePlanDetailsActivity.planModal.getHouseholds() != null) {
            if (!MorePlanDetailsActivity.planModal.getHouseholds().equals(""))
                households.setText(dbHandler.readHouseholdsFromID(MorePlanDetailsActivity.planModal.getHouseholds()));
            else households.setText("مشخص نشده");
        } else households.setText("مشخص نشده");


        return view;
    }
}