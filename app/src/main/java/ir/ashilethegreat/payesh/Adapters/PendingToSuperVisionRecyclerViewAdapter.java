package ir.ashilethegreat.payesh.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.Activities.ConfirmPlanFacilityActivity;
import ir.ashilethegreat.payesh.Activities.ConfirmPlanLicenseActivity;
import ir.ashilethegreat.payesh.Activities.ConfirmPlanRequirementsActivity;
import ir.ashilethegreat.payesh.Activities.CorrectionPlanEcosystemRequirementActivity;
import ir.ashilethegreat.payesh.Activities.CorrectionPlanFacilityActivity;
import ir.ashilethegreat.payesh.Activities.CorrectionPlanLicenseActivity;
import ir.ashilethegreat.payesh.Activities.CorrectionPlanDetailsActivity;
import ir.ashilethegreat.payesh.Activities.CorrectionPlanRequirementsActivity;
import ir.ashilethegreat.payesh.Activities.DataViewActivity;
import ir.ashilethegreat.payesh.Activities.InquiryActivity;
import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.Activities.PendingToSuperVisionActivity;
import ir.ashilethegreat.payesh.Activities.RejectedPlanActivity;
import ir.ashilethegreat.payesh.Activities.ViolationPlanLocationActivity;
import ir.ashilethegreat.payesh.Activities.ViolationPlanRequirementsActivity;
import ir.ashilethegreat.payesh.DBhandlers.ConfirmPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class PendingToSuperVisionRecyclerViewAdapter extends RecyclerView.Adapter<PendingToSuperVisionRecyclerViewAdapter.ModelViewHolder> {

    final ArrayList<PlanModal> planList;
    ArrayList<UserModal> userList;
    ArrayList<String> userNames;
    final Context context;
    final DBHandler dbHandler;
    Dialog delete, transferDialog, descriptionDialog;
    String loginToken;
    int selectedPosition;

    final int DEFAULT_TIMEOUT = 20000;
    RequestQueue requestQueue;
    JSONObject jsonBody;

    public PendingToSuperVisionRecyclerViewAdapter(ArrayList<PlanModal> planList, Context context) {
        this.dbHandler = new DBHandler(context);
        this.context = context;
        this.planList = planList;
        this.userList = new ArrayList<>();
        this.userNames = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(context);
        this.loginToken = dbHandler.getUser().getToken();
    }

    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_pending_to_super_vision_plans_recyclerview_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {

        if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 0) {
            holder.continueTitle.setText("شروع پایش");
        }
        holder.PlanPosition.setText(String.valueOf(planList.get(holder.getAbsoluteAdapterPosition()).getPlanID()));
        holder.name.setText(String.format("%s %s", planList.get(holder.getAbsoluteAdapterPosition()).getName(), planList.get(holder.getAbsoluteAdapterPosition()).getFamilyName()));
        holder.planTitle.setText(dbHandler.readPlanTitleFromID(planList.get(holder.getAbsoluteAdapterPosition()).getPlanTitle()));
        if (!planList.get(holder.getAbsoluteAdapterPosition()).getPhone().equalsIgnoreCase("null") &&
                !planList.get(holder.getAbsoluteAdapterPosition()).getPhone().isEmpty())
            holder.phoneNumber.setText(planList.get(holder.getAbsoluteAdapterPosition()).getPhone());
        holder.address.setText(String.format("%s،%s", dbHandler.readProvinceNameFromID(planList.get(holder.getAbsoluteAdapterPosition()).getProvince()),
                dbHandler.readCityNameFromID(planList.get(holder.getAbsoluteAdapterPosition()).getTownship())));

        if (!planList.get(holder.getAbsoluteAdapterPosition()).getUserDescription().isEmpty())
            holder.planDescription.setColorFilter(ContextCompat.getColor(context, R.color.blue_500), PorterDuff.Mode.SRC_IN);
        else
            holder.planDescription.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_IN);

        if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanPinned() == 1)
            holder.planPin.setColorFilter(ContextCompat.getColor(context, R.color.blue_500), PorterDuff.Mode.SRC_IN);
        else
            holder.planPin.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_IN);

        if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 0) {
            holder.planSituation.setText("مشخص نشده");
            holder.planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.gray));
        } else {
            holder.planSituation.setText(dbHandler.readSuperVisionTypeNameFromID(planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation()));
            switch (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation()) {
                case 1 ->
                        holder.planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_green));
                case 2 ->
                        holder.planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_red));
                case 3 ->
                        holder.planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_blue));
                case 4 ->
                        holder.planSituation.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.toast_yellow));
            }
        }

        holder.more.setOnClickListener(v -> {
            final Dialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.custom_manage_pending_to_supper_vision_plans_layout);

            LinearLayout deletePlan = bottomSheetDialog.findViewById(R.id.deletePlan);
            LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
            LinearLayout changeSituationPlan = bottomSheetDialog.findViewById(R.id.changeSituationPlan);
            LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
            LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);

            Objects.requireNonNull(deletePlan).setOnClickListener(view1 -> {
                createDeleteDialog(holder.getAbsoluteAdapterPosition());
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(moreDetails).setOnClickListener(view1 -> {
                createDetailsDialog(planList.get(holder.getAbsoluteAdapterPosition()));
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(transferPlansManagementLayout).setOnClickListener(view1 -> {
                createTransferDialog(planList.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition());
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(inquiryLayout).setOnClickListener(view1 -> {
                Intent intent = new Intent(context, InquiryActivity.class);
                intent.putExtra("natCode", planList.get(holder.getAbsoluteAdapterPosition()).getNatCode());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(changeSituationPlan).setOnClickListener(view1 -> {
                Intent intent = new Intent(context, DataViewActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();
                bottomSheetDialog.cancel();
            });

            bottomSheetDialog.show();
        });

        holder.planDescription.setOnClickListener(v -> createDescriptionDialog(planList.get(holder.getAbsoluteAdapterPosition()), holder.getAbsoluteAdapterPosition()));

        holder.planPin.setOnClickListener(v -> {
            if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanPinned() == 0) {
                PlanModal removedPlanModal = planList.remove(holder.getAbsoluteAdapterPosition());
                removedPlanModal.setPlanPinnedPosition("0");
                removedPlanModal.setPlanPinned(1);
                planList.add(0, removedPlanModal);
                dbHandler.updatePlanInfo(removedPlanModal);
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
                notifyItemMoved(holder.getAbsoluteAdapterPosition(), 0);
                PendingToSuperVisionActivity.pendingToSupperVisionRecyclerView.smoothScrollToPosition(0);
                successfulToastMaker("طرح مورد نظر با موفقیت در ابتدای لیست سنجاق شد.", new View(context));
            } else {
                PlanModal removedPlanModal = planList.remove(holder.getAbsoluteAdapterPosition());
                removedPlanModal.setPlanPinnedPosition(String.valueOf(Integer.MAX_VALUE));
                removedPlanModal.setPlanPinned(0);
                planList.add(removedPlanModal);
                dbHandler.updatePlanInfo(removedPlanModal);
                notifyItemChanged(holder.getAbsoluteAdapterPosition());
                notifyItemMoved(holder.getAbsoluteAdapterPosition(), planList.size() - 1);
                PendingToSuperVisionActivity.pendingToSupperVisionRecyclerView.smoothScrollToPosition(planList.size() - 1);
                successfulToastMaker("طرح مورد نظر از حالت سنجاق شده خارج شد.", new View(context));
            }

        });

        holder.continueLayout.setOnClickListener(v -> {

            if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 0) {
                Intent intent = new Intent(context, DataViewActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();
            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 1) {
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 0) {
                    Intent intent = new Intent(context, ConfirmPlanLicenseActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 1) {
                    Intent intent = new Intent(context, ConfirmPlanFacilityActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 2) {
                    Intent intent = new Intent(context, ConfirmPlanRequirementsActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }

            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 2) {
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 0) {

                    Intent intent = new Intent(context, ViolationPlanLocationActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 1) {
                    Intent intent = new Intent(context, ViolationPlanRequirementsActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();
                }
            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 3) {
                Intent intent = new Intent(context, RejectedPlanActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();

            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 4) {
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 0) {
                    Intent intent = new Intent(context, CorrectionPlanDetailsActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    intent.putExtra("isCorrectionNeeded", 1);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 1) {
                    Intent intent = new Intent(context, CorrectionPlanEcosystemRequirementActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 2) {
                    Intent intent = new Intent(context, CorrectionPlanLicenseActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 3) {
                    Intent intent = new Intent(context, CorrectionPlanFacilityActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
                if (planList.get(holder.getAbsoluteAdapterPosition()).getWhichStep() == 4) {
                    Intent intent = new Intent(context, CorrectionPlanRequirementsActivity.class);
                    intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(0, 0);
                    ((Activity) context).finish();

                }
            }

            //add more
        });

        holder.callLayout.setOnClickListener(v ->

        {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            if (planList.get(holder.getAbsoluteAdapterPosition()).getPhone() != null) {
                if (!planList.get(holder.getAbsoluteAdapterPosition()).getPhone().isEmpty() && planList.get(holder.getAbsoluteAdapterPosition()).getPhone().startsWith("0"))
                    intent.setData(Uri.parse("tel:" + planList.get(holder.getAbsoluteAdapterPosition()).getPhone()));
                else if (!planList.get(holder.getAbsoluteAdapterPosition()).getPhone().isEmpty() && !planList.get(holder.getAbsoluteAdapterPosition()).getPhone().startsWith("0"))
                    intent.setData(Uri.parse("tel:0" + planList.get(holder.getAbsoluteAdapterPosition()).getPhone()));
            } else {
                errorToastMaker("شماره ای برای تماس وجود ندارد!", new View(context));
            }
            context.startActivity(intent);

        });

        holder.itemView.setOnClickListener(v ->

        {
            if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 0) {
                Intent intent = new Intent(context, DataViewActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();


            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 1) {
                Intent intent = new Intent(context, ConfirmPlanLicenseActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();

            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 2) {
                Intent intent = new Intent(context, ViolationPlanLocationActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();


            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 3) {
                Intent intent = new Intent(context, RejectedPlanActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();


            } else if (planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation() == 4) {
                Intent intent = new Intent(context, CorrectionPlanDetailsActivity.class);
                intent.putExtra("planId", planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(0, 0);
                ((Activity) context).finish();

            }
            //add more

        });
    }

    @Override
    public int getItemCount() {
        return planList.size();
    }


    public static class ModelViewHolder extends RecyclerView.ViewHolder {
        final TextView PlanPosition;
        final TextView name;
        final TextView planTitle;
        final TextView continueTitle;
        final TextView phoneNumber;
        final TextView address;
        final LinearLayout continueLayout;
        final LinearLayout callLayout;
        final TextView planSituation;
        final ImageView more;
        final ImageView planDescription;
        final ImageView planPin;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            continueLayout = itemView.findViewById(R.id.continueLayout);
            callLayout = itemView.findViewById(R.id.callLayout);
            address = itemView.findViewById(R.id.address);
            planSituation = itemView.findViewById(R.id.planSituation);
            continueTitle = itemView.findViewById(R.id.continueTitle);
            more = itemView.findViewById(R.id.more);
            planDescription = itemView.findViewById(R.id.planDescription);
            planPin = itemView.findViewById(R.id.planPin);
        }

    }

    @Override
    public void onViewRecycled(@NonNull ModelViewHolder holder) {
        super.onViewRecycled(holder);
        for (int i = 0; i < planList.size(); i++) {
            if (!planList.get(i).getPlanPinnedPosition().equals(String.valueOf(Integer.MAX_VALUE))) {
                planList.get(i).setPlanPinnedPosition(i + "");
                dbHandler.updatePlanInfo(planList.get(i));
            } else {
                planList.get(i).setPlanPinnedPosition(String.valueOf(Integer.MAX_VALUE));
                dbHandler.updatePlanInfo(planList.get(i));
            }
        }
    }

    private void createDeleteDialog(int position) {
        delete = new Dialog(context);

        delete.setContentView(R.layout.custom_pending_to_supper_vision_confirmation_delete_dialog_layout);
        Objects.requireNonNull(delete.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        delete.setCanceledOnTouchOutside(false);

        Button confirm = delete.findViewById(R.id.confirm);
        Button cancel = delete.findViewById(R.id.cancel);
        ProgressBar deleteProgressBar = delete.findViewById(R.id.deleteProgressBar);

        confirm.setOnClickListener(v -> {
            confirm.setVisibility(View.INVISIBLE);
            cancel.setVisibility(View.INVISIBLE);
            deleteProgressBar.setVisibility(View.VISIBLE);
            jsonBody = new JSONObject();
            try {
                jsonBody.put("id_inspection", planList.get(position).getPlanID());
                jsonBody.put("national_code", planList.get(position).getNatCode());
            } catch (JSONException e) {
                errorToastMaker("خطای ساخت اطلاعات ارسالی", new View(context));
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLDelete), jsonBody,
                    response -> {
                        Log.i("test", "response:" + response.toString());

//                    Log.i("test", plan.getInspectionID());
                        try {
                            if (response.getString("status").equals("Token is Expired")) {
                                refreshAccessToken();
                                confirm.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.VISIBLE);
                                deleteProgressBar.setVisibility(View.INVISIBLE);
                            }
                            if (response.getString("status").equals("1")) {
                                dbHandler.deletePlan(planList.get(position).getPlanID(), planList.get(position).getUserID());
                                planList.remove(position);
                                notifyItemRemoved(position);

                                if (planList.size() == 0) {
                                    PendingToSuperVisionActivity.offlineLayout.setVisibility(View.VISIBLE);
                                    PendingToSuperVisionActivity.pendingToSupperVisionRecyclerView.setVisibility(View.INVISIBLE);
                                    PendingToSuperVisionActivity.search.setVisibility(View.INVISIBLE);
                                    PendingToSuperVisionActivity.toEnd.setVisibility(View.INVISIBLE);
                                }
                                successfulToastMaker("طرح مورد نظر از لیست در حال نظارت حذف شد.", new View(context));
                                delete.dismiss();

                            } else if (response.getString("status").equals("0")) {
                                alertToastMaker("در حال حاضر طرح قابل حذف کردن از لیست نیست!", new View(context));
                                confirm.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.VISIBLE);
                                deleteProgressBar.setVisibility(View.GONE);
                                delete.dismiss();
                            } else {
                                errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n دوباره تلاش کنید.", new View(context));
                                confirm.setVisibility(View.VISIBLE);
                                cancel.setVisibility(View.VISIBLE);
                                deleteProgressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            errorToastMaker("خطای سرویس حذف طرح", new View(context));
                            delete.dismiss();
                        }
                    },
                    error -> {
                        error.printStackTrace();
                        errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                        confirm.setVisibility(View.VISIBLE);
                        cancel.setVisibility(View.VISIBLE);
                        deleteProgressBar.setVisibility(View.GONE);
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + loginToken);
                    return headers;
                }
            };
            requestQueue.add(jsonRequest);
            jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                    DEFAULT_TIMEOUT,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        });

        cancel.setOnClickListener(v -> delete.dismiss());

        delete.show();
    }

    private void createDescriptionDialog(PlanModal plan, int position) {
        descriptionDialog = new Dialog(context);

        descriptionDialog.setContentView(R.layout.custom_pending_to_super_vision_description_layout);
        Objects.requireNonNull(descriptionDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        descriptionDialog.setCanceledOnTouchOutside(false);

        Button confirm = descriptionDialog.findViewById(R.id.confirm);
        Button cancel = descriptionDialog.findViewById(R.id.cancel);
        EditText description = descriptionDialog.findViewById(R.id.description);

        description.setText(plan.getUserDescription());

        confirm.setOnClickListener(v -> {
            plan.setUserDescription(description.getText().toString());
            dbHandler.updatePlanInfo(plan);
            successfulToastMaker("تغییرات توضیحات شخصی طرح با موفقیت ثبت شد.", new View(context));
            notifyItemChanged(position);
            descriptionDialog.dismiss();
        });

        cancel.setOnClickListener(v -> descriptionDialog.dismiss());

        descriptionDialog.show();
    }

    private void createDetailsDialog(PlanModal plan) {
        Intent intent = new Intent(context, MorePlanDetailsActivity.class);
        MorePlanDetailsActivity.planModal = plan;
        MorePlanDetailsActivity.providingTechnologyModalArrayList = dbHandler.readProvidingTechnologies(plan.getPlanID());
        MorePlanDetailsActivity.facilityModalArrayList = dbHandler.readFacilities(plan.getPlanID());
        MorePlanDetailsActivity.licenseModalArrayList = dbHandler.readLicenses(plan.getPlanID());
        MorePlanDetailsActivity.counselingModalArrayList = dbHandler.readCounselings(plan.getPlanID());
        MorePlanDetailsActivity.cultivationModalArrayList = dbHandler.readCultivations(plan.getPlanID());
        MorePlanDetailsActivity.educationModalArrayList = dbHandler.readEducations(plan.getPlanID());
        MorePlanDetailsActivity.marketingModalArrayList = dbHandler.readMarketings(plan.getPlanID());
        MorePlanDetailsActivity.identificationModalArrayList = dbHandler.readIdentifications(plan.getPlanID());
        MorePlanDetailsActivity.providingInfrastructureModalArrayList = dbHandler.readProvidingInfrastructures(plan.getPlanID());
        MorePlanDetailsActivity.notificationModalArrayList = dbHandler.readNotifications(plan.getPlanID());
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(0, 0);
    }

    private void createTransferDialog(PlanModal plan, int planPosition) {

        transferDialog = new Dialog(context);
        transferDialog.setContentView(R.layout.custom_transfer_plans_management_users_list_layout);
        Objects.requireNonNull(transferDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextInputLayout userChooseLayout;
        AutoCompleteTextView userChoose;
        ExtendedFloatingActionButton transfer, cancel;
        ProgressBar userLoadingProgressbar, transferProgressbar;
        LinearLayout bottomBar;

        userChooseLayout = transferDialog.findViewById(R.id.userChooseLayout);
        userChoose = transferDialog.findViewById(R.id.userChoose);
        transfer = transferDialog.findViewById(R.id.transfer);
        cancel = transferDialog.findViewById(R.id.cancel);
        userLoadingProgressbar = transferDialog.findViewById(R.id.userLoadingProgressbar);
        transferProgressbar = transferDialog.findViewById(R.id.transferProgressbar);
        bottomBar = transferDialog.findViewById(R.id.bottomBar);

        userLoadingProgressbar.setVisibility(View.VISIBLE);
        userChooseLayout.setVisibility(View.INVISIBLE);

        jsonBody = new JSONObject();
        JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLTransferUserList), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                            transferDialog.dismiss();
                        } else if (response.getString("status").equals("1")) {

                            JSONArray jsonArray = response.getJSONArray("data");
                            if (jsonArray.length() == 0) {
                                alertToastMaker("شخصی برای انتقال طرح یافت نشد!", new View(context));
                                userChooseLayout.setVisibility(View.VISIBLE);
                                userLoadingProgressbar.setVisibility(View.INVISIBLE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject data = jsonArray.getJSONObject(i);

                                    UserModal userModal = new UserModal();
                                    userModal.setId(data.getString("id"));
                                    userModal.setFirstName(data.getString("name"));
                                    userModal.setLastName(data.getString("family"));

                                    userList.add(userModal);

                                    userNames.add(i + 1 + "- " + data.getString("name") + " " + data.getString("family"));

                                }
                            }
                            ArrayAdapter<String> adapterOfOptions = new ArrayAdapter<>(context, R.layout.spinner_layout, userNames);
                            userChoose.setAdapter(adapterOfOptions);
                            userChooseLayout.setVisibility(View.VISIBLE);
                            userLoadingProgressbar.setVisibility(View.INVISIBLE);

                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس دریافت اطلاعات اشخاص", new View(context));
                        e.printStackTrace();
                        transferDialog.dismiss();
                    }
                },
                error -> {
                    errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                    transferDialog.dismiss();
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                UserModal user = dbHandler.getUser();
                headers.put("Authorization", "Bearer " + user.getToken());
                return headers;
            }
        };
        requestQueue.add(jsonRequest1);
        jsonRequest1.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        userChoose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userChooseLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        userChoose.setOnItemClickListener((parent, view, position, id) -> {

            selectedPosition = position;
        });

        transfer.setOnClickListener(v -> {
            if (userChoose.getText().toString().isEmpty())
                userChooseLayout.setError("یک مورد را انتخاب کنید!");
            else {
                Dialog confirmDialog = new Dialog(context);
                confirmDialog.setContentView(R.layout.custom_pending_to_supervision_transfer_confirmation_dialog_layout);
                Objects.requireNonNull(confirmDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ExtendedFloatingActionButton confirmTransfer, cancelTransfer;
                ProgressBar progressBar;
                LinearLayout bottomButtons;


                confirmTransfer = confirmDialog.findViewById(R.id.confirm);
                cancelTransfer = confirmDialog.findViewById(R.id.cancel);
                progressBar = confirmDialog.findViewById(R.id.progressBar);
                bottomButtons = confirmDialog.findViewById(R.id.bottomButtons);

                confirmTransfer.setOnClickListener(v12 -> {

                    bottomBar.setVisibility(View.INVISIBLE);
                    transferProgressbar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    bottomButtons.setVisibility(View.INVISIBLE);

                    jsonBody = new JSONObject();
                    try {
                        jsonBody.put("id_user", userList.get(selectedPosition).getId());
                    } catch (JSONException ignored) {

                    }
                    JsonObjectRequest transferRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLPlanTransferRequest) + plan.getPlanID(), jsonBody,
                            response -> {
                                try {
                                    if (response.getString("status").equals("Token is Expired")) {
                                        refreshAccessToken();
                                        bottomBar.setVisibility(View.VISIBLE);
                                        transferProgressbar.setVisibility(View.INVISIBLE);
                                        progressBar.setVisibility(View.INVISIBLE);
                                        bottomButtons.setVisibility(View.VISIBLE);
                                    } else if (response.getString("status").equals("1")) {

                                        dbHandler.deletePlan(planList.get(planPosition).getPlanID(), planList.get(planPosition).getUserID());
                                        planList.remove(planPosition);
                                        notifyItemRemoved(planPosition);
                                        notifyDataSetChanged();

                                        if (planList.size() == 0) {
                                            PendingToSuperVisionActivity.offlineLayout.setVisibility(View.VISIBLE);
                                            PendingToSuperVisionActivity.pendingToSupperVisionRecyclerView.setVisibility(View.INVISIBLE);
                                            PendingToSuperVisionActivity.search.setVisibility(View.INVISIBLE);
                                            PendingToSuperVisionActivity.toEnd.setVisibility(View.INVISIBLE);
                                        }

                                        successfulToastMaker("درخواست انتقال طرح با موفقیت ثبت شد.", new View(context));
                                        transferDialog.dismiss();
                                    }
                                } catch (JSONException e) {
                                    errorToastMaker("خطای سرویس انتقال طرح", new View(context));
                                    e.printStackTrace();
                                    transferDialog.dismiss();
                                    confirmDialog.dismiss();
                                }
                            },
                            error -> {
                                errorToastMaker("در ارتباط با سرور مشکلی رخ داد. \n اینترنت خود را بررسی نموده و دوباره تلاش کنید.", new View(context));
                                transferDialog.dismiss();
                                error.printStackTrace();
                                progressBar.setVisibility(View.INVISIBLE);
                                bottomButtons.setVisibility(View.VISIBLE);
                            }
                    ) {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String, String> headers = new HashMap<>();
                            UserModal user = dbHandler.getUser();
                            headers.put("Authorization", "Bearer " + user.getToken());
                            return headers;
                        }
                    };
                    requestQueue.add(transferRequest);
                    transferRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DEFAULT_TIMEOUT,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    confirmDialog.dismiss();
                });

                cancelTransfer.setOnClickListener(v1 -> confirmDialog.dismiss());

                confirmDialog.show();
            }

        });


        cancel.setOnClickListener(v -> transferDialog.dismiss());

        transferDialog.show();
    }

    public void errorToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_error, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
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

    public void successfulToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_successful, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void infoToastMaker(String message, View view) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.toast_message_info, view.findViewById(R.id.toast_layout_root));

        TextView text = layout.findViewById(R.id.toastMessage);
        text.setText(message);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void refreshAccessToken() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", dbHandler.getUser().getUsername());
            jsonBody.put("password", dbHandler.getUser().getPassword());

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
                alertToastMaker("دوباره تلاش کنید!", new View(context));
            } catch (JSONException e) {
                // this will never happen but if so, show error to user.
            }
        }, error -> {
            // show error to user. refresh failed.
            Log.e("test", new String(error.networkResponse.data));

        });
        requestQueue.add(refreshTokenRequest);
    }
}
