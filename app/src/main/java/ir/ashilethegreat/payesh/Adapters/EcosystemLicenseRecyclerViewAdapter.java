package ir.ashilethegreat.payesh.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.R;
import saman.zamani.persiandate.PersianDate;

public class EcosystemLicenseRecyclerViewAdapter extends RecyclerView.Adapter<EcosystemLicenseRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<LicenseModal> licenseList;
    final ArrayList<LicenseModal> correctionLicenseList;
    DBHandler dbHandler;
    Context context;
    String planDay = "", planMonth = "", planYear = "";
    boolean isDataView;

    public EcosystemLicenseRecyclerViewAdapter(ArrayList<LicenseModal> licenseList, ArrayList<LicenseModal> correctionLicenseList, boolean isDataView, Context context) {
        this.licenseList = licenseList;
        this.correctionLicenseList = correctionLicenseList;
        this.context = context;
        this.isDataView = isDataView;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public EcosystemLicenseRecyclerViewAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ecosystem_license_recyclerview_item, parent, false);
        return new EcosystemLicenseRecyclerViewAdapter.ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EcosystemLicenseRecyclerViewAdapter.ModelViewHolder holder, int position) {
        if (!correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseNumber().isEmpty())
            holder.licenceNumber.setText(correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseNumber());
        else {
            if (licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseNumber() != null) {
                holder.licenceNumber.setText(licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseNumber());
            } else holder.licenceNumber.setText("");
        }
        if (!correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseType().isEmpty())
            holder.licenceType.setText(dbHandler.readLicenceTypeFromID(correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseType()));
        else {
            if (licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseType() != null)
                holder.licenceType.setText(dbHandler.readLicenceTypeFromID(licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseType()));
            else holder.licenceType.setText("");
        }

        if (!correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseDate().isEmpty())
            holder.dateOfFacilities.setText(correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseDate());
        else {
            if (licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseDate() != null)
                holder.dateOfFacilities.setText(licenseList.get(holder.getAbsoluteAdapterPosition()).getLicenseDate());
            else holder.dateOfFacilities.setText("");
        }
        if (!correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getOrganID().isEmpty())
            holder.organ.setText(dbHandler.readOrganNameFromID(correctionLicenseList.get(holder.getAbsoluteAdapterPosition()).getOrganID()));
        else {
            if (licenseList.get(holder.getAbsoluteAdapterPosition()).getOrganID() != null)
                holder.organ.setText(dbHandler.readOrganNameFromID(licenseList.get(holder.getAbsoluteAdapterPosition()).getOrganID()));
            else holder.organ.setText("");
        }

        if (isDataView)
            holder.editEcosystem.setVisibility(View.GONE);
        holder.editEcosystem.setOnClickListener(v -> {
            createEditDialog(licenseList.get(holder.getAbsoluteAdapterPosition()), correctionLicenseList.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return licenseList.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView licenceNumber, licenceType, dateOfFacilities, organ;
        ExtendedFloatingActionButton editEcosystem;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            licenceNumber = itemView.findViewById(R.id.licenceNumber);
            licenceType = itemView.findViewById(R.id.licenceType);
            dateOfFacilities = itemView.findViewById(R.id.dateOfFacilities);
            organ = itemView.findViewById(R.id.organ);
            editEcosystem = itemView.findViewById(R.id.editEcosystem);

        }

    }

    private void createEditDialog(LicenseModal license, LicenseModal correctionLicense, int position) {
        Dialog editFacility = new Dialog(context);
        editFacility.setContentView(R.layout.custom_echo_system_add_new_license_layout);
        Objects.requireNonNull(editFacility.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText licenceNumber, licenseDate;
        final AutoCompleteTextView licenceType, organ;
        final TextInputLayout licenceNumberLayout, licenseDateLayout, licenceTypeLayout, organLayout;
        final ExtendedFloatingActionButton confirm, cancel, btnChooseDate;
        ArrayAdapter<String> adapterOfOptions;

        licenceNumber = editFacility.findViewById(R.id.licenceNumber);
        licenseDate = editFacility.findViewById(R.id.licenseDate);
        licenceType = editFacility.findViewById(R.id.licenceType);
        organ = editFacility.findViewById(R.id.organ);
        licenceNumberLayout = editFacility.findViewById(R.id.licenceNumberLayout);
        licenseDateLayout = editFacility.findViewById(R.id.licenseDateLayout);
        licenceTypeLayout = editFacility.findViewById(R.id.licenceTypeLayout);
        organLayout = editFacility.findViewById(R.id.organLayout);
        btnChooseDate = editFacility.findViewById(R.id.btnChooseDate);
        confirm = editFacility.findViewById(R.id.confirm);
        cancel = editFacility.findViewById(R.id.cancel);

        if (!correctionLicense.getLicenseNumber().isEmpty())
            licenceNumber.setText(correctionLicense.getLicenseNumber());
        else
            licenceNumber.setText(license.getLicenseNumber());
        if (!correctionLicense.getLicenseDate().isEmpty())
            licenseDate.setText(correctionLicense.getLicenseDate());
        else
            licenseDate.setText(license.getLicenseDate());
        if (!correctionLicense.getLicenseType().isEmpty())
            licenceType.setText(dbHandler.readLicenceTypeFromID(correctionLicense.getLicenseType()));
        else
            licenceType.setText(dbHandler.readLicenceTypeFromID(license.getLicenseType()));
        if (!correctionLicense.getOrganID().isEmpty())
            organ.setText(dbHandler.readOrganNameFromID(correctionLicense.getOrganID()));
        else
            organ.setText(dbHandler.readOrganNameFromID(license.getOrganID()));

        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readLicenceTypeNames());
        licenceType.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readOrgansNames());
        organ.setAdapter(adapterOfOptions);

        licenceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                licenceNumberLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        licenseDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                licenseDateLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        licenceType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                licenceTypeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        organ.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                organLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cancel.setOnClickListener(v -> editFacility.dismiss());

        btnChooseDate.setOnClickListener(v -> {
            Dialog dialog = new Dialog(context);

            dialog.setContentView(R.layout.custom_date_picker_layout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCanceledOnTouchOutside(false);


            Button confirm1 = dialog.findViewById(R.id.confirm);
            Button cancel1 = dialog.findViewById(R.id.cancel);
            NumberPicker day = dialog.findViewById(R.id.dayPicker);
            NumberPicker month = dialog.findViewById(R.id.monthPicker);
            NumberPicker year = dialog.findViewById(R.id.yearPicker);

            day.setMinValue(1);
            day.setMaxValue(31);
            day.setValue(PersianDate.today().getShDay());

            month.setMinValue(1);
            month.setMaxValue(12);
            month.setValue(PersianDate.today().getShMonth());

            year.setMinValue(1300);
            year.setMaxValue(PersianDate.today().getShYear());
            year.setValue(PersianDate.today().getShYear());

            confirm1.setOnClickListener(v1 -> {
                planYear = String.valueOf(year.getValue());
                planMonth = String.valueOf(month.getValue());
                planDay = String.valueOf(day.getValue());
                licenseDate.setText(String.format("%s/%s/%s", planYear, planMonth, planDay));

                licenseDateLayout.setError(null);
                dialog.dismiss();
            });
            cancel1.setOnClickListener(v1 -> dialog.cancel());
            dialog.show();
        });

        confirm.setOnClickListener(v -> {
            if (licenceNumber.getText().toString().trim().isEmpty())
                licenceNumberLayout.setError("شماره مجوز را وارد نمایید!");
            if (!planDay.isEmpty() && !planMonth.isEmpty() && !planYear.isEmpty() && !licenseDate.getText().toString().isEmpty()) {
                if (Integer.parseInt(planMonth) > 6) {
                    if (Integer.parseInt(planDay) > 30) {
                        licenseDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    }
                }
                if (Integer.parseInt(planMonth) == 12) {
                    PersianDate date1 = new PersianDate();
                    date1.setShYear(Integer.parseInt(planYear));
                    if (Integer.parseInt(planDay) == 30 && !date1.isLeap()) {
                        licenseDateLayout.setError("تاریخ را به درستی وارد کنید!");
                    }
                }
                if (Integer.parseInt(planYear) == PersianDate.today().getShYear()) {
                    if (Integer.parseInt(planMonth) == PersianDate.today().getShMonth()) {
                        if (Integer.parseInt(planDay) > PersianDate.today().getShDay()) {
                            licenseDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                        }
                    } else if (Integer.parseInt(planMonth) > PersianDate.today().getShMonth()) {
                        licenseDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                    }
                } else if (Integer.parseInt(planYear) > PersianDate.today().getShYear()) {
                    licenseDateLayout.setError("تاریخ وارد شده نمی تواند از تاریخ امروز جلوتر باشد!");
                }

                if (Integer.parseInt(planYear) < 1300 || Integer.parseInt(planYear) > 1500) {
                    licenseDateLayout.setError("سال را به درستی وارد کنید!");
                    if (Integer.parseInt(planMonth) > 12) {
                        licenseDateLayout.setError("ماه را به درستی وارد کنید!");
                        if (Integer.parseInt(planDay) > 31) {
                            licenseDateLayout.setError("روز را به درستی وارد کنید!");
                        }
                    }
                }

            }
            if (licenceType.getText().toString().trim().isEmpty())
                licenceTypeLayout.setError("نوع مجوز را انتخاب نمایید!");
            if (organ.getText().toString().trim().isEmpty())
                organLayout.setError("نام دستگاه را انتخاب نمایید!");

            if (licenceNumberLayout.getError() == null && licenseDateLayout.getError() == null &&
                    licenceTypeLayout.getError() == null && organLayout.getError() == null) {

                if (!license.getLicenseNumber().equals(licenceNumber.getText().toString()))
                    correctionLicense.setLicenseNumber(licenceNumber.getText().toString());
                else
                    correctionLicense.setLicenseNumber("");
                if (!license.getLicenseDate().equals(licenseDate.getText().toString()))
                    correctionLicense.setLicenseDate(licenseDate.getText().toString());
                else
                    correctionLicense.setLicenseDate("");
                if (!license.getLicenseType().equals(dbHandler.readLicenceTypeID(licenceType.getText().toString())))
                    correctionLicense.setLicenseType(dbHandler.readLicenceTypeID(licenceType.getText().toString()));
                else
                    correctionLicense.setLicenseType("");
                if (!license.getOrganID().equals(dbHandler.readOrganID(organ.getText().toString())))
                    correctionLicense.setOrganID(dbHandler.readOrganID(organ.getText().toString()));
                else
                    correctionLicense.setOrganID("");

                dbHandler.updateEcosystemLicense(correctionLicense);
                notifyItemChanged(position);
                editFacility.dismiss();

            }
        });

        editFacility.show();
    }


}