package ir.ashilethegreat.payesh.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.R;

public class EcosystemFinanceRecyclerViewAdapter extends RecyclerView.Adapter<EcosystemFinanceRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<FacilityModal> facilityList;
    final ArrayList<FacilityModal> correctionFacilityList;
    DBHandler dbHandler;
    boolean isDataView;
    Context context;

    public EcosystemFinanceRecyclerViewAdapter(ArrayList<FacilityModal> facilityList, ArrayList<FacilityModal> correctionFacilityList, boolean isDataView, Context context) {
        this.facilityList = facilityList;
        this.correctionFacilityList = correctionFacilityList;
        this.context = context;
        this.isDataView = isDataView;
        dbHandler = new DBHandler(context);
    }

    @NonNull
    @Override
    public EcosystemFinanceRecyclerViewAdapter.ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ecosystem_facilities_recyclerview_item, parent, false);
        return new EcosystemFinanceRecyclerViewAdapter.ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EcosystemFinanceRecyclerViewAdapter.ModelViewHolder holder, int position) {
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                    holder.facilitiesTotalAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.facilitiesTotalAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                        holder.facilitiesTotalAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.facilitiesTotalAmount.setText(String.format("%s ریال", formattedString));
                } else holder.facilitiesTotalAmount.setText("0 ریال");
            } else {
                holder.facilitiesTotalAmount.setText("0 ریال");
            }
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getContributedFacilitiesAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getContributedFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم آورده متقاضی (ریال)", new View(context));
                    holder.contributedFacilitiesAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.contributedFacilitiesAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getContributedFacilitiesAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getContributedFacilitiesAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش سهم آورده متقاضی (ریال)", new View(context));
                        holder.contributedFacilitiesAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.contributedFacilitiesAmount.setText(String.format("%s ریال", formattedString));
                }
            } else {
                holder.contributedFacilitiesAmount.setText("0 ریال");
            }
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getShareFacilitiesAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getShareFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم تسهیلات (ریال)", new View(context));
                    holder.facilitiesShareAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.facilitiesShareAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getShareFacilitiesAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getShareFacilitiesAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش سهم تسهیلات (ریال)", new View(context));
                        holder.facilitiesShareAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.facilitiesShareAmount.setText(String.format("%s ریال", formattedString));
                }
            } else {
                holder.facilitiesShareAmount.setText("0 ریال");
            }
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getFixedFacilityAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getFixedFacilityAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش تسهیلات ثابت (ریال)", new View(context));
                    holder.fixedFacilitiesAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.fixedFacilitiesAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getFixedFacilityAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getFixedFacilityAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش سرمایه ثابت (ریال)", new View(context));
                        holder.fixedFacilitiesAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.fixedFacilitiesAmount.setText(String.format("%s ریال", formattedString));
                }
            } else {
                holder.fixedFacilitiesAmount.setText("0 ریال");
            }
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingFacilitiesAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سرمایه در گردش (ریال)", new View(context));
                    holder.workingFacilitiesAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.workingFacilitiesAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingFacilitiesAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingFacilitiesAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش تسهیلات در گردش (ریال)", new View(context));
                        holder.workingFacilitiesAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.workingFacilitiesAmount.setText(String.format("%s ریال", formattedString));
                }
            } else {
                holder.workingFacilitiesAmount.setText("0 ریال");
            }
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getFixedBankID().isEmpty())
            holder.bankNameFixed.setText(dbHandler.readBankNameFromID(correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getFixedBankID()));
        else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getFixedBankID() != null)
                holder.bankNameFixed.setText(dbHandler.readBankNameFromID(facilityList.get(holder.getAbsoluteAdapterPosition()).getFixedBankID()));
            else holder.bankNameFixed.setText("");
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingBankID().isEmpty())
            holder.bankNameWorking.setText(dbHandler.readBankNameFromID(correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingBankID()));
        else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingBankID() != null)
                holder.bankNameWorking.setText(dbHandler.readBankNameFromID(facilityList.get(holder.getAbsoluteAdapterPosition()).getWorkingBankID()));
            else holder.bankNameWorking.setText("");
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getSupplyFacilitiesID().isEmpty())
            holder.supplyFacilities.setText(dbHandler.readSupplyFacilityFromID(correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getSupplyFacilitiesID()));
        else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getSupplyFacilitiesID() != null)
                holder.supplyFacilities.setText(dbHandler.readSupplyFacilityFromID(facilityList.get(holder.getAbsoluteAdapterPosition()).getSupplyFacilitiesID()));
            else holder.supplyFacilities.setText("");
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getDateFacilities().isEmpty())
            holder.dateFacilities.setText(dbHandler.readDateFacilityFromID(correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getDateFacilities()));
        else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getDateFacilities() != null)
                holder.dateFacilities.setText(dbHandler.readDateFacilityFromID(facilityList.get(holder.getAbsoluteAdapterPosition()).getDateFacilities()));
            else holder.dateFacilities.setText("");
        }
        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getOrganID().isEmpty())
            holder.organ.setText(dbHandler.readOrganNameFromID(correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getOrganID()));
        else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getOrganID() != null)
                holder.organ.setText(dbHandler.readOrganNameFromID(facilityList.get(holder.getAbsoluteAdapterPosition()).getOrganID()));
            else holder.organ.setText("");
        }

        if (isDataView)
            holder.editEcosystem.setVisibility(View.GONE);

        holder.editEcosystem.setOnClickListener(v -> {

            //.................................
            //.................................
            //NEED TO CHECK FOR NULL EXCEPTION
            //................................
            //................................

//            createEditDialog(facilityList.get(holder.getAbsoluteAdapterPosition()), correctionFacilityList.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return facilityList.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView facilitiesTotalAmount, contributedFacilitiesAmount, facilitiesShareAmount,
                fixedFacilitiesAmount, bankNameFixed, workingFacilitiesAmount, bankNameWorking,
                supplyFacilities, dateFacilities, organ;
        ExtendedFloatingActionButton editEcosystem;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            facilitiesTotalAmount = itemView.findViewById(R.id.totalFacilitiesAmount);
            contributedFacilitiesAmount = itemView.findViewById(R.id.contributedFacilitiesAmount);
            facilitiesShareAmount = itemView.findViewById(R.id.shareFacilitiesAmount);
            fixedFacilitiesAmount = itemView.findViewById(R.id.fixedFacilitiesAmount);
            bankNameFixed = itemView.findViewById(R.id.bankNameFixed);
            workingFacilitiesAmount = itemView.findViewById(R.id.workingFacilitiesAmount);
            bankNameWorking = itemView.findViewById(R.id.bankNameWorking);
            supplyFacilities = itemView.findViewById(R.id.supplyFacilities);
            dateFacilities = itemView.findViewById(R.id.dateFacilities);
            organ = itemView.findViewById(R.id.organ);
            editEcosystem = itemView.findViewById(R.id.editEcosystem);

        }

    }
    //.................................
    //.................................
    //NEED TO CHECK FOR NULL EXCEPTION
    //................................
    //................................

   /* private void createEditDialog(FacilityModal facility, FacilityModal correctionFacility, int position) {
        Dialog editFacility = new Dialog(context);
        editFacility.setContentView(R.layout.custom_echo_system_add_new_facility_layout);
        Objects.requireNonNull(editFacility.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText totalFacilitiesAmount, contributedFacilitiesAmount, shareFacilitiesAmount,
                fixedFacilitiesAmount, workingFacilitiesAmount;
        final AutoCompleteTextView organ, dateFacilities, supplyFacilities, bankNameWorking, bankNameFixed;
        final TextInputLayout totalFacilitiesAmountLayout, contributedFacilitiesAmountLayout,
                shareFacilitiesAmountLayout, fixedFacilitiesAmountLayout, workingFacilitiesAmountLayout,
                organLayout, dateFacilitiesLayout, supplyFacilitiesLayout, bankNameWorkingLayout, bankNameFixedLayout;
        final ExtendedFloatingActionButton confirm, cancel;
        final TextView totalFacilityToText, contributedFacilityToText, shareFacilityToText, fixedFacilityToText, workingFacilityToText;
        ArrayAdapter<String> adapterOfOptions;

        totalFacilitiesAmount = editFacility.findViewById(R.id.totalFacilitiesAmount);
        contributedFacilitiesAmount = editFacility.findViewById(R.id.contributedFacilitiesAmount);
        shareFacilitiesAmount = editFacility.findViewById(R.id.shareFacilitiesAmount);
        fixedFacilitiesAmount = editFacility.findViewById(R.id.fixedFacilitiesAmount);
        workingFacilitiesAmount = editFacility.findViewById(R.id.workingFacilitiesAmount);
        organ = editFacility.findViewById(R.id.organ);
        dateFacilities = editFacility.findViewById(R.id.dateFacilities);
        supplyFacilities = editFacility.findViewById(R.id.supplyFacilities);
        bankNameWorking = editFacility.findViewById(R.id.bankNameWorking);
        bankNameFixed = editFacility.findViewById(R.id.bankNameFixed);
        totalFacilitiesAmountLayout = editFacility.findViewById(R.id.totalFacilitiesAmountLayout);
        contributedFacilitiesAmountLayout = editFacility.findViewById(R.id.contributedFacilitiesAmountLayout);
        shareFacilitiesAmountLayout = editFacility.findViewById(R.id.shareFacilitiesAmountLayout);
        fixedFacilitiesAmountLayout = editFacility.findViewById(R.id.fixedFacilitiesAmountLayout);
        workingFacilitiesAmountLayout = editFacility.findViewById(R.id.workingFacilitiesAmountLayout);
        organLayout = editFacility.findViewById(R.id.organLayout);
        dateFacilitiesLayout = editFacility.findViewById(R.id.dateFacilitiesLayout);
        supplyFacilitiesLayout = editFacility.findViewById(R.id.supplyFacilitiesLayout);
        bankNameWorkingLayout = editFacility.findViewById(R.id.bankNameWorkingLayout);
        bankNameFixedLayout = editFacility.findViewById(R.id.bankNameFixedLayout);
        totalFacilityToText = editFacility.findViewById(R.id.totalFacilityToText);
        contributedFacilityToText = editFacility.findViewById(R.id.contributedFacilityToText);
        shareFacilityToText = editFacility.findViewById(R.id.shareFacilityToText);
        fixedFacilityToText = editFacility.findViewById(R.id.fixedFacilityToText);
        workingFacilityToText = editFacility.findViewById(R.id.workingFacilityToText);
        confirm = editFacility.findViewById(R.id.confirm);
        cancel = editFacility.findViewById(R.id.cancel);

        if (!correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount().isEmpty()) {
            String s = correctionFacilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                    holder.facilitiesTotalAmount.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                holder.facilitiesTotalAmount.setText(String.format("%s ریال", formattedString));
            }
        } else {
            if (facilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount() != null) {
                String s = facilityList.get(holder.getAbsoluteAdapterPosition()).getTotalFacilitiesAmount();
                Long longval = 0L;
                if (!s.isEmpty()) {
                    if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                        s = s.replaceAll(",", "");
                        s = s.replaceAll("/", "");
                        s = s.replaceAll("\\.", "");
                        s = s.replaceAll(" ", "");
                        s = s.replaceAll("\"", "");
                    }
                    try {
                        longval = Long.parseLong(s);
                    } catch (Exception e) {
                        alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                        holder.facilitiesTotalAmount.setText("");
                    }
                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);
                    holder.facilitiesTotalAmount.setText(String.format("%s ریال", formattedString));
                }
            } else {
                holder.facilitiesTotalAmount.setText("0 ریال");
            }
        }

        if (!correctionFacility.getTotalFacilitiesAmount().isEmpty()) {
            totalFacilitiesAmount.setText(correctionFacility.getTotalFacilitiesAmount());
            String s = correctionFacility.getTotalFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                    totalFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                totalFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        } else {
            totalFacilitiesAmount.setText(facility.getTotalFacilitiesAmount());
            String s = facility.getTotalFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش کل سرمایه گذاری (ریال)", new View(context));
                    totalFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                totalFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (!correctionFacility.getContributedFacilitiesAmount().isEmpty()) {
            contributedFacilitiesAmount.setText(correctionFacility.getContributedFacilitiesAmount());
            String s = correctionFacility.getContributedFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم آورده متقاضی (ریال)", new View(context));
                    contributedFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                contributedFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        } else {
            contributedFacilitiesAmount.setText(facility.getContributedFacilitiesAmount());
            String s = facility.getContributedFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم آورده متقاضی (ریال)", new View(context));
                    contributedFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                contributedFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (!correctionFacility.getShareFacilitiesAmount().isEmpty()) {
            shareFacilitiesAmount.setText(correctionFacility.getShareFacilitiesAmount());
            String s = correctionFacility.getShareFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم تسهیلات (ریال)", new View(context));
                    shareFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                shareFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        } else {
            shareFacilitiesAmount.setText(facility.getShareFacilitiesAmount());
            String s = facility.getShareFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش سهم تسهیلات (ریال)", new View(context));
                    shareFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                shareFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (!correctionFacility.getFixedFacilityAmount().isEmpty()) {
            fixedFacilitiesAmount.setText(correctionFacility.getFixedFacilityAmount());
            String s = correctionFacility.getFixedFacilityAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش تهسیلات ثابت (ریال)", new View(context));
                    fixedFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                fixedFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        } else {
            fixedFacilitiesAmount.setText(facility.getFixedFacilityAmount());
            String s = facility.getFixedFacilityAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش تهسیلات ثابت (ریال)", new View(context));
                    fixedFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                fixedFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (!correctionFacility.getWorkingFacilitiesAmount().isEmpty()) {
            workingFacilitiesAmount.setText(correctionFacility.getWorkingFacilitiesAmount());
            String s = correctionFacility.getWorkingFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش تهسیلات گردش (ریال)", new View(context));
                    workingFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                workingFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        } else {
            workingFacilitiesAmount.setText(facility.getWorkingFacilitiesAmount());
            String s = facility.getWorkingFacilitiesAmount();
            Long longval = 0L;
            if (!s.isEmpty()) {
                if (s.contains(",") || s.contains("/") || s.contains(".") || s.contains(" ") || s.contains("\"")) {
                    s = s.replaceAll(",", "");
                    s = s.replaceAll("/", "");
                    s = s.replaceAll("\\.", "");
                    s = s.replaceAll(" ", "");
                    s = s.replaceAll("\"", "");
                }
                try {
                    longval = Long.parseLong(s);
                } catch (Exception e) {
                    alertToastMaker("خطا در نمایش تهسیلات گردش (ریال)", new View(context));
                    workingFacilityToText.setText("");
                }
                DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                formatter.applyPattern("#,###,###,###");
                String formattedString = formatter.format(longval);
                workingFacilityToText.setText(String.format("%s ریال", formattedString));
            }
        }
        if (!correctionFacility.getFixedBankID().isEmpty())
            bankNameFixed.setText(dbHandler.readBankNameFromID(correctionFacility.getFixedBankID()));
        else
            bankNameFixed.setText(dbHandler.readBankNameFromID(facility.getFixedBankID()));
        if (!correctionFacility.getWorkingBankID().isEmpty())
            bankNameWorking.setText(dbHandler.readBankNameFromID(correctionFacility.getWorkingBankID()));
        else
            bankNameWorking.setText(dbHandler.readBankNameFromID(facility.getWorkingBankID()));
        if (!correctionFacility.getSupplyFacilitiesID().isEmpty())
            supplyFacilities.setText(dbHandler.readSupplyFacilityFromID(correctionFacility.getSupplyFacilitiesID()));
        else
            supplyFacilities.setText(dbHandler.readSupplyFacilityFromID(facility.getSupplyFacilitiesID()));
        if (!correctionFacility.getDateFacilities().isEmpty())
            dateFacilities.setText(dbHandler.readDateFacilityFromID(correctionFacility.getDateFacilities()));
        else
            dateFacilities.setText(dbHandler.readDateFacilityFromID(facility.getDateFacilities()));
        if (!correctionFacility.getOrganID().isEmpty())
            organ.setText(dbHandler.readOrganNameFromID(correctionFacility.getOrganID()));
        else
            organ.setText(dbHandler.readOrganNameFromID(facility.getOrganID()));

        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readBanksNames());
        bankNameFixed.setAdapter(adapterOfOptions);
        bankNameWorking.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readSupplyFacilityNames());
        supplyFacilities.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readDateFacilityNames());
        dateFacilities.setAdapter(adapterOfOptions);
        adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, dbHandler.readOrgansNames());
        organ.setAdapter(adapterOfOptions);

        totalFacilitiesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                totalFacilitiesAmountLayout.setError(null);
                totalFacilitiesAmount.removeTextChangedListener(this);

                try {
                    if (!totalFacilitiesAmount.getText().toString().isEmpty()) {
                        String originalString = s.toString();
                        Long longval = 0L;
                        if (originalString.contains(",") || originalString.contains("/") || originalString.contains(".") || originalString.contains(" ") || originalString.contains("\"")) {
                            originalString = originalString.replaceAll(",", "");
                            originalString = originalString.replaceAll("/", "");
                            originalString = originalString.replaceAll("\\.", "");
                            originalString = originalString.replaceAll(" ", "");
                            originalString = originalString.replaceAll("\"", "");
                        }
                        try {
                            longval = Long.parseLong(originalString);
                        } catch (Exception e) {
                            alertToastMaker("عدد را به شکل درست بنویسید!", new View(context));
                            totalFacilityToText.setText("");
                        }

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        totalFacilityToText.setText(String.format("%s ریال", formattedString));
                        totalFacilitiesAmount.setSelection(totalFacilitiesAmount.getText().length());
                    } else totalFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                totalFacilitiesAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contributedFacilitiesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                contributedFacilitiesAmountLayout.setError(null);
                contributedFacilitiesAmount.removeTextChangedListener(this);

                try {
                    if (!contributedFacilitiesAmount.getText().toString().isEmpty()) {
                        String originalString = s.toString();
                        Long longval = 0L;
                        if (originalString.contains(",") || originalString.contains("/") || originalString.contains(".") || originalString.contains(" ") || originalString.contains("\"")) {
                            originalString = originalString.replaceAll(",", "");
                            originalString = originalString.replaceAll("/", "");
                            originalString = originalString.replaceAll("\\.", "");
                            originalString = originalString.replaceAll(" ", "");
                            originalString = originalString.replaceAll("\"", "");
                        }
                        try {
                            longval = Long.parseLong(originalString);
                        } catch (Exception e) {
                            alertToastMaker("عدد را به شکل درست بنویسید!", new View(context));
                            contributedFacilitiesAmount.setText("");
                        }

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        contributedFacilityToText.setText(String.format("%s ریال", formattedString));
                        contributedFacilitiesAmount.setSelection(contributedFacilitiesAmount.getText().length());
                    } else contributedFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                contributedFacilitiesAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        shareFacilitiesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                shareFacilitiesAmountLayout.setError(null);
                shareFacilitiesAmount.removeTextChangedListener(this);

                try {
                    if (!shareFacilitiesAmount.getText().toString().isEmpty()) {
                        String originalString = s.toString();
                        Long longval = 0L;
                        if (originalString.contains(",") || originalString.contains("/") || originalString.contains(".") || originalString.contains(" ") || originalString.contains("\"")) {
                            originalString = originalString.replaceAll(",", "");
                            originalString = originalString.replaceAll("/", "");
                            originalString = originalString.replaceAll("\\.", "");
                            originalString = originalString.replaceAll(" ", "");
                            originalString = originalString.replaceAll("\"", "");
                        }
                        try {
                            longval = Long.parseLong(originalString);
                        } catch (Exception e) {
                            alertToastMaker("عدد را به شکل درست بنویسید!", new View(context));
                            shareFacilitiesAmount.setText("");
                        }

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        shareFacilityToText.setText(String.format("%s ریال", formattedString));
                        shareFacilitiesAmount.setSelection(shareFacilitiesAmount.getText().length());
                    } else shareFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                shareFacilitiesAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        fixedFacilitiesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                fixedFacilitiesAmountLayout.setError(null);
                fixedFacilitiesAmount.removeTextChangedListener(this);

                try {
                    if (!fixedFacilitiesAmount.getText().toString().isEmpty()) {
                        String originalString = s.toString();
                        Long longval = 0L;
                        if (originalString.contains(",") || originalString.contains("/") || originalString.contains(".") || originalString.contains(" ") || originalString.contains("\"")) {
                            originalString = originalString.replaceAll(",", "");
                            originalString = originalString.replaceAll("/", "");
                            originalString = originalString.replaceAll("\\.", "");
                            originalString = originalString.replaceAll(" ", "");
                            originalString = originalString.replaceAll("\"", "");
                        }
                        try {
                            longval = Long.parseLong(originalString);
                        } catch (Exception e) {
                            alertToastMaker("عدد را به شکل درست بنویسید!", new View(context));
                            fixedFacilitiesAmount.setText("");
                        }

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        fixedFacilityToText.setText(String.format("%s ریال", formattedString));
                        fixedFacilitiesAmount.setSelection(fixedFacilitiesAmount.getText().length());
                    } else fixedFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                fixedFacilitiesAmount.addTextChangedListener(this);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        workingFacilitiesAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                workingFacilitiesAmountLayout.setError(null);
                workingFacilitiesAmount.removeTextChangedListener(this);

                try {
                    if (!workingFacilitiesAmount.getText().toString().isEmpty()) {
                        String originalString = s.toString();
                        Long longval = 0L;
                        if (originalString.contains(",") || originalString.contains("/") || originalString.contains(".") || originalString.contains(" ") || originalString.contains("\"")) {
                            originalString = originalString.replaceAll(",", "");
                            originalString = originalString.replaceAll("/", "");
                            originalString = originalString.replaceAll("\\.", "");
                            originalString = originalString.replaceAll(" ", "");
                            originalString = originalString.replaceAll("\"", "");
                        }
                        try {
                            longval = Long.parseLong(originalString);
                        } catch (Exception e) {
                            alertToastMaker("عدد را به شکل درست بنویسید!", new View(context));
                            workingFacilitiesAmount.setText("");
                        }

                        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                        formatter.applyPattern("#,###,###,###");
                        String formattedString = formatter.format(longval);

                        //setting text after format to TextView
                        workingFacilityToText.setText(String.format("%s ریال", formattedString));
                        workingFacilitiesAmount.setSelection(workingFacilitiesAmount.getText().length());
                    } else workingFacilityToText.setText(String.format("%s ریال", 0));
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                workingFacilitiesAmount.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankNameFixed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bankNameFixedLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        bankNameWorking.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bankNameWorkingLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        supplyFacilities.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                supplyFacilitiesLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateFacilities.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dateFacilitiesLayout.setError(null);
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

        confirm.setOnClickListener(v -> {
            if (totalFacilitiesAmount.getText().toString().trim().isEmpty())
                totalFacilitiesAmountLayout.setError("مبلغ کل سرمایه گذاری نمی تواند خالی باشد!");
            if (contributedFacilitiesAmount.getText().toString().trim().isEmpty())
                contributedFacilitiesAmountLayout.setError("سهم آورده متقاضی نمی تواند خالی باشد!");
            if (shareFacilitiesAmount.getText().toString().trim().isEmpty())
                shareFacilitiesAmountLayout.setError("سهم تسهیلات نمی تواند خالی باشد!");
            if (fixedFacilitiesAmount.getText().toString().trim().isEmpty())
                fixedFacilitiesAmountLayout.setError("تسهیلات ثابت نمی تواند خالی باشد!");
            if (bankNameFixed.getText().toString().trim().isEmpty())
                bankNameFixedLayout.setError("تأمین کننده تسهیلات ثابت را انتخاب نمایید!");
            if (workingFacilitiesAmount.getText().toString().trim().isEmpty())
                workingFacilitiesAmountLayout.setError("تسهیلات در گردش نمی تواند خالی باشد!");
            if (bankNameWorking.getText().toString().trim().isEmpty())
                bankNameWorkingLayout.setError("تأمین کننده تسهیلات در گردش را انتخاب نمایید!");
            if (supplyFacilities.getText().toString().trim().isEmpty())
                supplyFacilitiesLayout.setError("محل تأمین منابع را انتخاب نمایید!");
            if (dateFacilities.getText().toString().trim().isEmpty())
                dateFacilitiesLayout.setError("تاریخ تأمین را انتخاب نمایید!");
            if (organ.getText().toString().trim().isEmpty())
                organLayout.setError("نام دستگاه را انتخاب نمایید!");
            try {
                if (Double.parseDouble(fixedFacilitiesAmount.getText().toString()) + Double.parseDouble(workingFacilitiesAmount.getText().toString()) !=
                        Double.parseDouble(shareFacilitiesAmount.getText().toString())) {
                    shareFacilitiesAmountLayout.setError("مبلغ سهم تسهیلات باید با مجموع تسهیلات ثابت و درگردش برابر باشد!");
                }
                if (Double.parseDouble(shareFacilitiesAmount.getText().toString()) + Double.parseDouble(contributedFacilitiesAmount.getText().toString()) !=
                        Double.parseDouble(totalFacilitiesAmount.getText().toString())) {
                    totalFacilitiesAmountLayout.setError("مبلغ کل سرمایه گذاری باید با مجموع سهم آورده متقاضی و سهم تسهیلات برابر باشد!");
                }
            } catch (Exception e) {
                alertToastMaker("شکل نوشتاری اعداد صحیح نیست!\nلطفاً مقادیر عددی را بدون هرگونه علامت اضافه بنویسید و دوباره تلاش نمایید!", new View(context));
            }
            if (totalFacilitiesAmountLayout.getError() == null && contributedFacilitiesAmountLayout.getError() == null &&
                    shareFacilitiesAmountLayout.getError() == null && fixedFacilitiesAmountLayout.getError() == null &&
                    workingFacilitiesAmountLayout.getError() == null && bankNameFixedLayout.getError() == null &&
                    bankNameWorkingLayout.getError() == null && supplyFacilitiesLayout.getError() == null &&
                    dateFacilitiesLayout.getError() == null && organLayout.getError() == null) {

                if (!facility.getTotalFacilitiesAmount().equals(totalFacilitiesAmount.getText().toString()))
                    correctionFacility.setTotalFacilitiesAmount(totalFacilitiesAmount.getText().toString());
                else
                    correctionFacility.setTotalFacilitiesAmount("");
                if (!facility.getContributedFacilitiesAmount().equals(contributedFacilitiesAmount.getText().toString()))
                    correctionFacility.setContributedFacilitiesAmount(contributedFacilitiesAmount.getText().toString());
                else
                    correctionFacility.setContributedFacilitiesAmount("");
                if (!facility.getShareFacilitiesAmount().equals(shareFacilitiesAmount.getText().toString()))
                    correctionFacility.setShareFacilitiesAmount(shareFacilitiesAmount.getText().toString());
                else
                    correctionFacility.setShareFacilitiesAmount("");
                if (!facility.getFixedFacilityAmount().equals(fixedFacilitiesAmount.getText().toString()))
                    correctionFacility.setFixedFacilityAmount(fixedFacilitiesAmount.getText().toString());
                else
                    correctionFacility.setFixedFacilityAmount("");
                if (!facility.getWorkingFacilitiesAmount().equals(workingFacilitiesAmount.getText().toString()))
                    correctionFacility.setWorkingFacilitiesAmount(workingFacilitiesAmount.getText().toString());
                else
                    correctionFacility.setWorkingFacilitiesAmount("");
                if (!facility.getFixedBankID().equals(dbHandler.readBankID(bankNameFixed.getText().toString())))
                    correctionFacility.setFixedBankID(dbHandler.readBankID(bankNameFixed.getText().toString()));
                else
                    correctionFacility.setFixedBankID("");
                if (!facility.getWorkingBankID().equals(dbHandler.readBankID(bankNameWorking.getText().toString())))
                    correctionFacility.setWorkingBankID(dbHandler.readBankID(bankNameWorking.getText().toString()));
                else
                    correctionFacility.setWorkingBankID("");
                if (!facility.getSupplyFacilitiesID().equals(dbHandler.readSupplyFacilityID(supplyFacilities.getText().toString())))
                    correctionFacility.setSupplyFacilitiesID(dbHandler.readSupplyFacilityID(supplyFacilities.getText().toString()));
                else
                    correctionFacility.setSupplyFacilitiesID("");
                if (!facility.getDateFacilities().equals(dbHandler.readDateFacilityID(dateFacilities.getText().toString())))
                    correctionFacility.setDateFacilities(dbHandler.readDateFacilityID(dateFacilities.getText().toString()));
                else
                    correctionFacility.setDateFacilities("");
                if (!facility.getOrganID().equals(dbHandler.readOrganID(organ.getText().toString())))
                    correctionFacility.setOrganID(dbHandler.readOrganID(organ.getText().toString()));
                else
                    correctionFacility.setOrganID("");

                dbHandler.updateEcosystemFacility(correctionFacility);
                notifyItemChanged(position);
                editFacility.dismiss();

            }
        });

        editFacility.show();
    }*/

    private void createDeleteDialog(int position) {
        Dialog delete = new Dialog(context);

        delete.setContentView(R.layout.custom_pending_to_supper_vision_confirmation_delete_dialog_layout);
        Objects.requireNonNull(delete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete.setCanceledOnTouchOutside(false);

        Button confirm = delete.findViewById(R.id.confirm);
        Button cancel = delete.findViewById(R.id.cancel);

        confirm.setOnClickListener(v -> {
//            dbHandler.deletePendingToSupperVisionPlans(facilityList.get(position).getPlanID());
//            facilityList.remove(position);
//            notifyItemRemoved(position);
//            notifyDataSetChanged();
//
//            if (facilityList.size() == 0) {
//                PendingToSuperVisionActivity.offlineLayout.setVisibility(View.VISIBLE);
//                PendingToSuperVisionActivity.pendingToSupperVisionRecyclerView.setVisibility(View.INVISIBLE);
//                PendingToSuperVisionActivity.search.setVisibility(View.INVISIBLE);
//                PendingToSuperVisionActivity.toEnd.setVisibility(View.INVISIBLE);
//            }
//            successfulToastMaker("طرح مورد نظر از لیست حذف شد.", new View(context));
            delete.dismiss();
        });

        cancel.setOnClickListener(v -> delete.dismiss());

        delete.show();
    }

    public void alertToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_alert, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}