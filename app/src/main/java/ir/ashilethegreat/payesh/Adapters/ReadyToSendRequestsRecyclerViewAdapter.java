package ir.ashilethegreat.payesh.Adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
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

import ir.ashilethegreat.payesh.Activities.InquiryActivity;
import ir.ashilethegreat.payesh.Activities.MorePlanDetailsActivity;
import ir.ashilethegreat.payesh.Activities.ReadyToSendRequestsActivity;
import ir.ashilethegreat.payesh.BuildConfig;
import ir.ashilethegreat.payesh.DBhandlers.BailModal;
import ir.ashilethegreat.payesh.DBhandlers.ConfirmPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.FacilityModal;
import ir.ashilethegreat.payesh.DBhandlers.LicenseModal;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.DBhandlers.RejectedPlanModal;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.DBhandlers.ViolationPlanModal;
import ir.ashilethegreat.payesh.R;

public class ReadyToSendRequestsRecyclerViewAdapter extends RecyclerView.Adapter<ReadyToSendRequestsRecyclerViewAdapter.ModelViewHolder> {

    private final int DEFAULT_TIMEOUT = 30000;
    final ArrayList<PlanModal> planList;
    final ArrayList<UserModal> userList;
    final ArrayList<String> userNames;
    UserModal userModal;
    Context context;
    final DBHandler dbHandler;
    Dialog inProgress, done, transferDialog;
    String userID;
    final RequestQueue requestQueue;
    JSONObject jsonBody;
    int selectedPosition;

    public ReadyToSendRequestsRecyclerViewAdapter(ArrayList<PlanModal> planList, String userID, Context context) {
        this.planList = planList;
        this.context = context;
        this.dbHandler = new DBHandler(context);
        this.inProgress = new Dialog(context);
        this.userList = new ArrayList<>();
        this.userNames = new ArrayList<>();
        this.userModal = new UserModal();
        this.done = new Dialog(context);
        this.requestQueue = Volley.newRequestQueue(context);
        this.userID = userID;
    }


    @NonNull
    @Override
    public ModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_ready_to_send_recyclerview_item, parent, false);
        return new ModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModelViewHolder holder, int position) {
        holder.PlanPosition.setText(planList.get(holder.getAbsoluteAdapterPosition()).getPlanID());
        holder.name.setText(String.format("%s %s", planList.get(holder.getAbsoluteAdapterPosition()).getName(), planList.get(holder.getAbsoluteAdapterPosition()).getFamilyName()));
        holder.planTitle.setText(dbHandler.readPlanTitleFromID(planList.get(holder.getAbsoluteAdapterPosition()).getPlanTitle()));
        holder.phoneNumber.setText(planList.get(holder.getAbsoluteAdapterPosition()).getPhone());
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

        holder.reviewFromStartLayout.setOnClickListener(v -> createSendBackToReadyToPendingDialog(holder.getAbsoluteAdapterPosition()));

        holder.uploadLayout.setOnClickListener(v -> {
            createUploadDialog();
            uploadData(holder.getAbsoluteAdapterPosition(), planList.get(holder.getAbsoluteAdapterPosition()).getPlanFinalSuperVisionSituation());
        });

        holder.more.setOnClickListener(v -> {
            final Dialog bottomSheetDialog = new BottomSheetDialog(context);
            bottomSheetDialog.setContentView(R.layout.custom_manage_input_plans_layout);

            LinearLayout moreDetails = bottomSheetDialog.findViewById(R.id.moreDetails);
            LinearLayout transferPlansManagementLayout = bottomSheetDialog.findViewById(R.id.transferPlansManagementLayout);
            LinearLayout inquiryLayout = bottomSheetDialog.findViewById(R.id.inquiryLayout);


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

            bottomSheetDialog.show();
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
        final TextView phoneNumber;
        final LinearLayout reviewFromStartLayout;
        final LinearLayout uploadLayout;
        final TextView planSituation;
        final ImageView more;

        public ModelViewHolder(@NonNull View itemView) {
            super(itemView);
            PlanPosition = itemView.findViewById(R.id.planPosition);
            name = itemView.findViewById(R.id.name);
            planTitle = itemView.findViewById(R.id.planTitle);
            phoneNumber = itemView.findViewById(R.id.phoneNumber);
            reviewFromStartLayout = itemView.findViewById(R.id.reviewFromStartLayout);
            uploadLayout = itemView.findViewById(R.id.uploadLayout);
            planSituation = itemView.findViewById(R.id.planSituation);
            more = itemView.findViewById(R.id.more);
        }

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
                confirmDialog.setContentView(R.layout.custom_ready_to_send_transfer_confirmation_dialog_layout);
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
//                                        notifyDataSetChanged();
                                        if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() != View.VISIBLE) {
                                            ReadyToSendRequestsActivity.readyToSendRecyclerView.setVisibility(View.INVISIBLE);
                                            ReadyToSendRequestsActivity.offlineLayout.setVisibility(View.VISIBLE);
                                            ReadyToSendRequestsActivity.search.hide();
                                            ReadyToSendRequestsActivity.toEnd.hide();
                                        } else if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() == View.VISIBLE) {
                                            ReadyToSendRequestsActivity.readyToSendRecyclerView.setVisibility(View.INVISIBLE);
                                            ReadyToSendRequestsActivity.notFound.setVisibility(View.VISIBLE);
                                            ReadyToSendRequestsActivity.search.hide();
                                            ReadyToSendRequestsActivity.toEnd.hide();
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

    private void createSendBackToReadyToPendingDialog(int position) {
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.custom_ready_to_send_confirmation_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button cancel = dialog.findViewById(R.id.cancel);
        Button confirm = dialog.findViewById(R.id.confirm);

        confirm.setOnClickListener(v -> {
            DBHandler dbHandler = new DBHandler(context);
            planList.get(position).setIsCompleted(0);
            dbHandler.updatePlanInfo(planList.get(position));
            planList.remove(position);
            notifyItemRemoved(position);

            dbHandler.close();
            successfulToastMaker("با موفقیت به بخش «بارگیری شده» برگشت داده شد.", v);
            if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() != View.VISIBLE) {
                ReadyToSendRequestsActivity.readyToSendRecyclerView.setVisibility(View.INVISIBLE);
                ReadyToSendRequestsActivity.offlineLayout.setVisibility(View.VISIBLE);
                ReadyToSendRequestsActivity.search.hide();
            } else if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() == View.VISIBLE) {
                ReadyToSendRequestsActivity.readyToSendRecyclerView.setVisibility(View.INVISIBLE);
                ReadyToSendRequestsActivity.notFound.setVisibility(View.VISIBLE);
                ReadyToSendRequestsActivity.search.hide();
            }
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> dialog.dismiss());


        dialog.show();

    }

    private void uploadData(int position, int situation) {
        PlanModal planModal = planList.get(position);
        JSONObject postData = new JSONObject();
        try {
            postData.put("id_registration_status", planModal.getStatusRegistration());
            postData.put("version", BuildConfig.VERSION_CODE);
            if (situation == 1) {
                postData.put("status", 1);
                final ConfirmPlanModal confirmPlan = dbHandler.confirmPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                ArrayList<BailModal> bailModalArrayList = dbHandler.readPlanBails(planList.get(position).getPlanID(), planList.get(position).getUserID());

                postData.put("id_type_position", confirmPlan.getConfirmPlanRegionLocation());
                postData.put("id_bordering_position", confirmPlan.getConfirmPlanBorderLocation());
                postData.put("id_part", confirmPlan.getConfirmSection());
                postData.put("id_part", confirmPlan.getConfirmSection());
                postData.put("city", confirmPlan.getConfirmCity());
                postData.put("rural_district", confirmPlan.getConfirmRegion());
                postData.put("village", confirmPlan.getConfirmMainStreet());
                postData.put("street", confirmPlan.getConfirmSubStreet());
                postData.put("alley", confirmPlan.getConfirmAlley());
                postData.put("plaque", confirmPlan.getConfirmCode());
                postData.put("post_code", confirmPlan.getConfirmPostalCode());
                postData.put("address", confirmPlan.getConfirmMoreAddress());
                postData.put("has_license", confirmPlan.getConfirmPlanLicenceExist());
                if (!confirmPlan.getConfirmPlanLicenceImage().isEmpty())
                    postData.put("image_license_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanLicenceImage());
                if (!confirmPlan.getConfirmPlanOwnershipImage().isEmpty())
                    postData.put("image_ownership_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanOwnershipImage());
                postData.put("image_tarh_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanImage());
                postData.put("new_employment", confirmPlan.getConfirmPlanEmployeeCount());
                postData.put("id_tarh_status_type_employment", confirmPlan.getConfirmPlanType());
                postData.put("id_payment_status", confirmPlan.getConfirmPlanReceivedFacilitySituation());
                postData.put("financing_payment", confirmPlan.getConfirmPlanReceivedFacility());
                postData.put("reasons_non_payment", confirmPlan.getConfirmPlanNotReceivedFacilityReason());
                postData.put("bank_bail", confirmPlan.getConfirmPlanProvidedBail());
                postData.put("id_bank", confirmPlan.getConfirmPlanBankTitle());
                postData.put("bank_branch", confirmPlan.getConfirmPlanBankBranch());
                postData.put("financing_payment_date", confirmPlan.getConfirmPlanPaymentDate());
                postData.put("financing_payment_take_long", confirmPlan.getConfirmPlanDateMargin());
                postData.put("financing_deviation", confirmPlan.getConfirmPlanFacilityDeviationExist());
                postData.put("monthly_income", confirmPlan.getConfirmPlanMonthlyIncome());
                postData.put("gps_l", confirmPlan.getConfirmPlanLongitude());
                postData.put("gps_w", confirmPlan.getConfirmPlanLatitude());
                postData.put("id_marketing_level", confirmPlan.getConfirmPlanMarketingLevel());
                postData.put("inspection_assessment", confirmPlan.getConfirmPlanFinalEvaluation());
                postData.put("job_offers_1", confirmPlan.getConfirmPlanOwnerSuggestionOne());
                postData.put("job_offers_2", confirmPlan.getConfirmPlanOwnerSuggestionTwo());
                postData.put("education_offers_1", confirmPlan.getConfirmPlanEducationOne());
                postData.put("education_offers_2", confirmPlan.getConfirmPlanEducationTwo());
                postData.put("strengths_plan", confirmPlan.getConfirmPlanStrength());
                postData.put("weaknesses_plan", confirmPlan.getConfirmPlanWeakness());

                JSONArray challengeList = new JSONArray();
                if (confirmPlan.getConfirmNotUpToDateDataChallenge() == 1)
                    challengeList.put("1");
                if (confirmPlan.getConfirmHumanResourcesChallenge() == 1)
                    challengeList.put("2");
                if (confirmPlan.getConfirmCulturalProblemsChallenge() == 1)
                    challengeList.put("3");
                if (confirmPlan.getConfirmLegalIssuesChallenge() == 1)
                    challengeList.put("4");
                if (confirmPlan.getConfirmRuleChangingChallenge() == 1)
                    challengeList.put("5");
                if (confirmPlan.getConfirmMarketingChallenge() == 1)
                    challengeList.put("6");
                if (confirmPlan.getConfirmTariffChallenge() == 1)
                    challengeList.put("7");
                if (confirmPlan.getConfirmImportSimilarProductChallenge() == 1)
                    challengeList.put("8");
                if (confirmPlan.getConfirmFinancialChallenge() == 1)
                    challengeList.put("9");
                if (confirmPlan.getConfirmRAndDChallenge() == 1)
                    challengeList.put("10");
                if (confirmPlan.getConfirmSupplyRawMaterialsChallenge() == 1)
                    challengeList.put("11");
                if (confirmPlan.getConfirmInfrastructureChallenge() == 1)
                    challengeList.put("12");
                if (confirmPlan.getConfirmOtherChallenge() == 1)
                    challengeList.put("13");

                postData.put("id_problem_location_employed", challengeList);

                JSONArray valueChainList = new JSONArray();
                if (confirmPlan.getConfirmPlanSupplyValueChain() == 1)
                    valueChainList.put("1");
                if (confirmPlan.getConfirmPlanDesignValueChain() == 1)
                    valueChainList.put("2");
                if (confirmPlan.getConfirmPlanProductionValueChain() == 1)
                    valueChainList.put("3");
                if (confirmPlan.getConfirmPlanProcessingValueChain() == 1)
                    valueChainList.put("4");
                if (confirmPlan.getConfirmPlanMarketingValueChain() == 1)
                    valueChainList.put("5");
                if (confirmPlan.getConfirmPlanOthersValueChain() == 1)
                    valueChainList.put("6");

                postData.put("id_value_chain", valueChainList);

                JSONArray bailsList = new JSONArray();
                for (int i = 0; i < bailModalArrayList.size(); i++) {
                    bailsList.put(bailModalArrayList.get(i).getConfirmBailName());
                }
                postData.put("id_bail", bailsList);

                //
                final PlanModal correctionPlan = dbHandler.correctionPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                JSONObject editedData = new JSONObject();

                JSONObject info = new JSONObject();
                if (!correctionPlan.getPhone().isEmpty())
                    info.put("phone", correctionPlan.getPhone());
                if (!correctionPlan.getFixedPhone().isEmpty())
                    info.put("tel", correctionPlan.getFixedPhone());
                if (!correctionPlan.getEducation().isEmpty())
                    info.put("id_degrees", correctionPlan.getEducation());
                info.put("id_fields", correctionPlan.getFieldOfStudy());
                info.put("id_proficiency", correctionPlan.getSpecialization());
                if (!correctionPlan.getHouseholds().isEmpty())
                    info.put("id_supervisor", correctionPlan.getHouseholds());

                editedData.put("info", info);

                JSONObject plan = new JSONObject();
                if (!correctionPlan.getPlanTitle().isEmpty())
                    plan.put("id_title_plan", correctionPlan.getPlanTitle());
                if (!correctionPlan.getPlanEconomicSection().isEmpty())
                    plan.put("id_type_activity", correctionPlan.getPlanEconomicSection());
                if (!correctionPlan.getPlanSituation().isEmpty())
                    plan.put("id_activity_status", correctionPlan.getPlanSituation());
                if (!correctionPlan.getPlanStartDate().isEmpty())
                    plan.put("date_activity", correctionPlan.getPlanStartDate());

                editedData.put("tarh", plan);

//                JSONObject location = new JSONObject();
//                if (!correctionPlan.getPlanRegionLocation().isEmpty())
//                    location.put("id_job_position", correctionPlan.getPlanRegionLocation());
//
//                editedData.put("location", location);

                JSONArray license = new JSONArray();
                ArrayList<LicenseModal> licenseModalArrayList = dbHandler.readCorrectionLicenses(planList.get(position).getPlanID());
                for (int i = 0; i < licenseModalArrayList.size(); i++) {
                    JSONObject licenseData = new JSONObject();
                    if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty() || !licenseModalArrayList.get(i).getLicenseType().isEmpty() ||
                            !licenseModalArrayList.get(i).getLicenseDate().isEmpty() || !licenseModalArrayList.get(i).getOrganID().isEmpty()) {
                        licenseData.put("id", licenseModalArrayList.get(i).getLicenseID());
                        if (!licenseModalArrayList.get(i).getOrganID().isEmpty())
                            licenseData.put("id_executive_device", licenseModalArrayList.get(i).getOrganID());
                        if (!licenseModalArrayList.get(i).getLicenseType().isEmpty())
                            licenseData.put("id_type_license", licenseModalArrayList.get(i).getLicenseType());
                        if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty())
                            licenseData.put("license_number", licenseModalArrayList.get(i).getLicenseNumber());
                        if (!licenseModalArrayList.get(i).getLicenseDate().isEmpty())
                            licenseData.put("date_license_number", licenseModalArrayList.get(i).getLicenseDate());

                        license.put(licenseData);
                    }
                }

                editedData.put("license", license);

                JSONArray financing = new JSONArray();
                ArrayList<FacilityModal> facilityModalArrayList = dbHandler.readCorrectionFacilities(planList.get(position).getPlanID());
                for (int i = 0; i < facilityModalArrayList.size(); i++) {
                    JSONObject facilityData = new JSONObject();
                    if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getFixedBankID().isEmpty() || !facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getWorkingBankID().isEmpty() || !facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty() ||
                            !facilityModalArrayList.get(i).getDateFacilities().isEmpty() || !facilityModalArrayList.get(i).getOrganID().isEmpty()) {
                        facilityData.put("id", facilityModalArrayList.get(i).getFinanceID());
                        if (!facilityModalArrayList.get(i).getOrganID().isEmpty())
                            facilityData.put("id_executive_device", facilityModalArrayList.get(i).getOrganID());
                        if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty())
                            facilityData.put("money", facilityModalArrayList.get(i).getTotalFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty())
                            facilityData.put("brought_person", facilityModalArrayList.get(i).getContributedFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty())
                            facilityData.put("id_location_of_credit", facilityModalArrayList.get(i).getSupplyFacilitiesID());
                        if (!facilityModalArrayList.get(i).getDateFacilities().isEmpty())
                            facilityData.put("id_location_of_credit_date", facilityModalArrayList.get(i).getDateFacilities());
                        if (!facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty())
                            facilityData.put("loan", facilityModalArrayList.get(i).getShareFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty())
                            facilityData.put("fixed_capital", facilityModalArrayList.get(i).getFixedFacilityAmount());
                        if (!facilityModalArrayList.get(i).getFixedBankID().isEmpty())
                            facilityData.put("id_bank_fixed_capital", facilityModalArrayList.get(i).getFixedBankID());
                        if (!facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getWorkingBankID().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingBankID());

                        financing.put(facilityData);
                    }
                }

                editedData.put("financing", financing);

                postData.put("data_edit", editedData);

            } else if (situation == 2) {
                postData.put("status", 2);
                final ViolationPlanModal violationPlan = dbHandler.violationPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                postData.put("gps_l", violationPlan.getViolationPlanLongitude());
                postData.put("gps_w", violationPlan.getViolationPlanLatitude());
                postData.put("id_type_position", violationPlan.getViolationPlanRegionLocation());
                postData.put("id_bordering_position", violationPlan.getViolationPlanBorderLocation());
                postData.put("id_part", violationPlan.getViolationSection());
                postData.put("city", violationPlan.getViolationCity());
                postData.put("rural_district", violationPlan.getViolationRegion());
                postData.put("village", violationPlan.getViolationMainStreet());
                postData.put("street", violationPlan.getViolationSubStreet());
                postData.put("alley", violationPlan.getViolationAlley());
                postData.put("plaque", violationPlan.getViolationCode());
                postData.put("post_code", violationPlan.getViolationPostalCode());
                postData.put("address", violationPlan.getViolationMoreAddress());
                postData.put("image_tarh_base64", "data:image/jpeg;base64," + violationPlan.getViolationPlanImage());

                JSONObject violationList = new JSONObject();

                if (violationPlan.getViolationNonCooperation() == 1) {

                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 1);
                    violationItem.put("reason_warning", violationPlan.getViolationNonCooperationDescription());
                    violationList.put("1", violationItem);
                }
                if (violationPlan.getViolationNonPlanImplement() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 2);
                    violationItem.put("reason_warning", violationPlan.getViolationNonPlanImplementDescription());
                    violationList.put("2", violationItem);
                }
                if (violationPlan.getViolationPersonalInfo() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 3);
                    violationItem.put("reason_warning", violationPlan.getViolationPersonalInfoDescription());
                    violationList.put("3", violationItem);
                }
                if (violationPlan.getViolationStartDate() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 4);
                    violationItem.put("reason_warning", violationPlan.getViolationStartDateDescription());
                    violationItem.put("correct_loan_date", violationPlan.getViolationPlanAdditionalCorrectStartDate());
                    violationList.put("4", violationItem);
                }
                if (violationPlan.getViolationEcosystem() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 5);
                    violationItem.put("reason_warning", violationPlan.getViolationEcosystemDescription());
                    violationList.put("5", violationItem);
                }
                if (violationPlan.getViolationPlanTitle() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 6);
                    violationItem.put("reason_warning", violationPlan.getViolationPlanTitleDescription());
                    violationItem.put("correct_id_title_plan", violationPlan.getViolationPlanAdditionalCorrectTitle());
                    violationList.put("6", violationItem);
                }
                if (violationPlan.getViolationConflictInPlanPlace() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 7);
                    violationItem.put("reason_warning", violationPlan.getViolationConflictInPlanPlaceDescription());
                    violationList.put("7", violationItem);
                }
                if (violationPlan.getViolationTypeContradictionOfFacilities() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 8);
                    violationItem.put("reason_warning", violationPlan.getViolationTypeContradictionOfFacilitiesDescription());
                    violationItem.put("correct_loan", violationPlan.getViolationPlanAdditionalCorrectFacility());
                    violationList.put("8", violationItem);
                }
                if (violationPlan.getViolationTypeNonCompletionOfPlan() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 9);
                    violationItem.put("reason_warning", violationPlan.getViolationTypeNonCompletionOfPlanDescription());
                    violationItem.put("start_in_month", violationPlan.getViolationPlanAdditionalStartInMonth());
                    violationList.put("9", violationItem);
                }
                if (violationPlan.getViolationPlanClosure() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 10);
                    violationItem.put("reason_warning", violationPlan.getViolationPlanClosureDescription());
                    violationItem.put("id_closure_reason", violationPlan.getViolationPlanAdditionalClosureReason());
                    violationList.put("10", violationItem);
                }
                if (violationPlan.getViolationPlanIncompatibility() == 1) {
                    JSONObject violationItem = new JSONObject();
                    violationItem.put("id_reason_warning", 11);
                    violationItem.put("reason_warning", violationPlan.getViolationPlanIncompatibilityDescription());
                    violationList.put("11", violationItem);
                }

                postData.put("id_reason_warning", violationList);

                final PlanModal correctionPlan = dbHandler.correctionPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                JSONObject editedData = new JSONObject();

                JSONObject info = new JSONObject();
                if (!correctionPlan.getPhone().isEmpty())
                    info.put("phone", correctionPlan.getPhone());
                if (!correctionPlan.getFixedPhone().isEmpty())
                    info.put("tel", correctionPlan.getFixedPhone());
                if (!correctionPlan.getEducation().isEmpty())
                    info.put("id_degrees", correctionPlan.getEducation());
                info.put("id_fields", correctionPlan.getFieldOfStudy());
                info.put("id_proficiency", correctionPlan.getSpecialization());
                if (!correctionPlan.getHouseholds().isEmpty())
                    info.put("id_supervisor", correctionPlan.getHouseholds());

                editedData.put("info", info);

                JSONObject plan = new JSONObject();
                if (!correctionPlan.getPlanTitle().isEmpty())
                    plan.put("id_title_plan", correctionPlan.getPlanTitle());
                if (!correctionPlan.getPlanEconomicSection().isEmpty())
                    plan.put("id_type_activity", correctionPlan.getPlanEconomicSection());
                if (!correctionPlan.getPlanSituation().isEmpty())
                    plan.put("id_activity_status", correctionPlan.getPlanSituation());
                if (!correctionPlan.getPlanStartDate().isEmpty())
                    plan.put("date_activity", correctionPlan.getPlanStartDate());

                editedData.put("tarh", plan);

//                JSONObject location = new JSONObject();
//                if (!correctionPlan.getPlanRegionLocation().isEmpty())
//                    location.put("id_job_position", correctionPlan.getPlanRegionLocation());
//
//                editedData.put("location", location);

                JSONArray license = new JSONArray();
                ArrayList<LicenseModal> licenseModalArrayList = dbHandler.readCorrectionLicenses(planList.get(position).getPlanID());
                for (int i = 0; i < licenseModalArrayList.size(); i++) {
                    JSONObject licenseData = new JSONObject();
                    if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty() || !licenseModalArrayList.get(i).getLicenseType().isEmpty() ||
                            !licenseModalArrayList.get(i).getLicenseDate().isEmpty() || !licenseModalArrayList.get(i).getOrganID().isEmpty()) {
                        licenseData.put("id", licenseModalArrayList.get(i).getLicenseID());
                        if (!licenseModalArrayList.get(i).getOrganID().isEmpty())
                            licenseData.put("id_executive_device", licenseModalArrayList.get(i).getOrganID());
                        if (!licenseModalArrayList.get(i).getLicenseType().isEmpty())
                            licenseData.put("id_type_license", licenseModalArrayList.get(i).getLicenseType());
                        if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty())
                            licenseData.put("license_number", licenseModalArrayList.get(i).getLicenseNumber());
                        if (!licenseModalArrayList.get(i).getLicenseDate().isEmpty())
                            licenseData.put("date_license_number", licenseModalArrayList.get(i).getLicenseDate());

                        license.put(licenseData);
                    }
                }

                editedData.put("license", license);

                JSONArray financing = new JSONArray();
                ArrayList<FacilityModal> facilityModalArrayList = dbHandler.readCorrectionFacilities(planList.get(position).getPlanID());
                for (int i = 0; i < facilityModalArrayList.size(); i++) {
                    JSONObject facilityData = new JSONObject();
                    if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getFixedBankID().isEmpty() || !facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getWorkingBankID().isEmpty() || !facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty() ||
                            !facilityModalArrayList.get(i).getDateFacilities().isEmpty() || !facilityModalArrayList.get(i).getOrganID().isEmpty()) {
                        facilityData.put("id", facilityModalArrayList.get(i).getFinanceID());
                        if (!facilityModalArrayList.get(i).getOrganID().isEmpty())
                            facilityData.put("id_executive_device", facilityModalArrayList.get(i).getOrganID());
                        if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty())
                            facilityData.put("money", facilityModalArrayList.get(i).getTotalFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty())
                            facilityData.put("brought_person", facilityModalArrayList.get(i).getContributedFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty())
                            facilityData.put("id_location_of_credit", facilityModalArrayList.get(i).getSupplyFacilitiesID());
                        if (!facilityModalArrayList.get(i).getDateFacilities().isEmpty())
                            facilityData.put("id_location_of_credit_date", facilityModalArrayList.get(i).getDateFacilities());
                        if (!facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty())
                            facilityData.put("loan", facilityModalArrayList.get(i).getShareFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty())
                            facilityData.put("fixed_capital", facilityModalArrayList.get(i).getFixedFacilityAmount());
                        if (!facilityModalArrayList.get(i).getFixedBankID().isEmpty())
                            facilityData.put("id_bank_fixed_capital", facilityModalArrayList.get(i).getFixedBankID());
                        if (!facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getWorkingBankID().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingBankID());

                        financing.put(facilityData);
                    }
                }

                editedData.put("financing", financing);

                postData.put("data_edit", editedData);

            } else if (situation == 3) {
                postData.put("status", 3);
                final RejectedPlanModal rejectedPlanModal = dbHandler.rejectedPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                postData.put("id_reason_archive", rejectedPlanModal.getRejectionReason());

                JSONObject rejectionList = new JSONObject();

                if (rejectedPlanModal.getRejectedTypeNonCooperation().equals("1")) {
                    JSONObject rejectionItem = new JSONObject();
                    rejectionItem.put("id_reason_warning", 1);
                    rejectionItem.put("message", rejectedPlanModal.getRejectedTypeNonCooperationDescription());
                    rejectionList.put("1", rejectionItem);
                }
                if (rejectedPlanModal.getRejectedTypeFailureToImplement().equals("1")) {
                    JSONObject rejectionItem = new JSONObject();
                    rejectionItem.put("id_reason_warning", 2);
                    rejectionItem.put("message", rejectedPlanModal.getRejectedTypeFailureToImplementDescription());
                    rejectionList.put("2", rejectionItem);
                }
                if (rejectedPlanModal.getRejectedTypePersonalInfo().equals("1")) {
                    JSONObject rejectionItem = new JSONObject();
                    rejectionItem.put("id_reason_warning", 3);
                    rejectionItem.put("message", rejectedPlanModal.getRejectedTypePersonalInfoDescription());
                    rejectionList.put("3", rejectionItem);
                }
                if (rejectedPlanModal.getRejectedTypeNoFacilityExist().equals("1")) {
                    JSONObject rejectionItem = new JSONObject();
                    rejectionItem.put("id_reason_warning", 5);
                    rejectionItem.put("message", rejectedPlanModal.getRejectedTypeNoFacilityExistDescription());
                    rejectionList.put("5", rejectionItem);
                }
                if (rejectedPlanModal.getRejectedTypeNonCompletionOfPlan().equals("1")) {
                    JSONObject rejectionItem = new JSONObject();
                    rejectionItem.put("id_reason_warning", 9);
                    rejectionItem.put("message", rejectedPlanModal.getRejectedTypeNonCompletionOfPlanDescription());
                    rejectionItem.put("start_in_month", rejectedPlanModal.getRejectionTypePlanAdditionalStartInMonth());
                    rejectionList.put("9", rejectionItem);
                }

                postData.put("id_reason_warning", rejectionList);

            }
            if (situation == 4) {
                postData.put("status", 1);
                final ConfirmPlanModal confirmPlan = dbHandler.confirmPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                postData.put("id_type_position", confirmPlan.getConfirmPlanRegionLocation());
                postData.put("id_bordering_position", confirmPlan.getConfirmPlanBorderLocation());
                postData.put("id_part", confirmPlan.getConfirmSection());
                postData.put("city", confirmPlan.getConfirmCity());
                postData.put("rural_district", confirmPlan.getConfirmRegion());
                postData.put("village", confirmPlan.getConfirmMainStreet());
                postData.put("street", confirmPlan.getConfirmSubStreet());
                postData.put("alley", confirmPlan.getConfirmAlley());
                postData.put("plaque", confirmPlan.getConfirmCode());
                postData.put("post_code", confirmPlan.getConfirmPostalCode());
                postData.put("address", confirmPlan.getConfirmMoreAddress());
                postData.put("has_license", confirmPlan.getConfirmPlanLicenceExist());
                if (!confirmPlan.getConfirmPlanLicenceImage().isEmpty())
                    postData.put("image_license_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanLicenceImage());
                if (!confirmPlan.getConfirmPlanOwnershipImage().isEmpty())
                    postData.put("image_ownership_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanOwnershipImage());
                postData.put("image_tarh_base64", "data:image/jpeg;base64," + confirmPlan.getConfirmPlanImage());
                postData.put("new_employment", confirmPlan.getConfirmPlanEmployeeCount());
                postData.put("id_payment_status", confirmPlan.getConfirmPlanReceivedFacilitySituation());
                postData.put("financing_payment", confirmPlan.getConfirmPlanReceivedFacility());
                postData.put("reasons_non_payment", confirmPlan.getConfirmPlanNotReceivedFacilityReason());
                postData.put("bank_bail", confirmPlan.getConfirmPlanProvidedBail());
                postData.put("id_bank", confirmPlan.getConfirmPlanBankTitle());
                postData.put("bank_branch", confirmPlan.getConfirmPlanBankBranch());
                postData.put("financing_payment_date", confirmPlan.getConfirmPlanPaymentDate());
                postData.put("financing_payment_take_long", confirmPlan.getConfirmPlanDateMargin());
                postData.put("financing_deviation", confirmPlan.getConfirmPlanFacilityDeviationExist());
                postData.put("monthly_income", confirmPlan.getConfirmPlanMonthlyIncome());
                postData.put("gps_l", confirmPlan.getConfirmPlanLongitude());
                postData.put("gps_w", confirmPlan.getConfirmPlanLatitude());
                postData.put("id_marketing_level", confirmPlan.getConfirmPlanMarketingLevel());
                postData.put("inspection_assessment", confirmPlan.getConfirmPlanFinalEvaluation());
                postData.put("job_offers_1", confirmPlan.getConfirmPlanOwnerSuggestionOne());
                postData.put("job_offers_2", confirmPlan.getConfirmPlanOwnerSuggestionTwo());
                postData.put("education_offers_1", confirmPlan.getConfirmPlanEducationOne());
                postData.put("education_offers_2", confirmPlan.getConfirmPlanEducationTwo());
                postData.put("strengths_plan", confirmPlan.getConfirmPlanStrength());
                postData.put("weaknesses_plan", confirmPlan.getConfirmPlanWeakness());

                JSONArray challengeList = new JSONArray();
                if (confirmPlan.getConfirmNotUpToDateDataChallenge() == 1)
                    challengeList.put("1");
                if (confirmPlan.getConfirmHumanResourcesChallenge() == 1)
                    challengeList.put("2");
                if (confirmPlan.getConfirmCulturalProblemsChallenge() == 1)
                    challengeList.put("3");
                if (confirmPlan.getConfirmLegalIssuesChallenge() == 1)
                    challengeList.put("4");
                if (confirmPlan.getConfirmRuleChangingChallenge() == 1)
                    challengeList.put("5");
                if (confirmPlan.getConfirmMarketingChallenge() == 1)
                    challengeList.put("6");
                if (confirmPlan.getConfirmTariffChallenge() == 1)
                    challengeList.put("7");
                if (confirmPlan.getConfirmImportSimilarProductChallenge() == 1)
                    challengeList.put("8");
                if (confirmPlan.getConfirmFinancialChallenge() == 1)
                    challengeList.put("9");
                if (confirmPlan.getConfirmRAndDChallenge() == 1)
                    challengeList.put("10");
                if (confirmPlan.getConfirmSupplyRawMaterialsChallenge() == 1)
                    challengeList.put("11");
                if (confirmPlan.getConfirmInfrastructureChallenge() == 1)
                    challengeList.put("12");
                if (confirmPlan.getConfirmOtherChallenge() == 1)
                    challengeList.put("13");

                postData.put("id_problem_location_employed", challengeList);

                JSONArray valueChainList = new JSONArray();
                if (confirmPlan.getConfirmPlanSupplyValueChain() == 1)
                    valueChainList.put("1");
                if (confirmPlan.getConfirmPlanDesignValueChain() == 1)
                    valueChainList.put("2");
                if (confirmPlan.getConfirmPlanProductionValueChain() == 1)
                    valueChainList.put("3");
                if (confirmPlan.getConfirmPlanProcessingValueChain() == 1)
                    valueChainList.put("4");
                if (confirmPlan.getConfirmPlanMarketingValueChain() == 1)
                    valueChainList.put("5");
                if (confirmPlan.getConfirmPlanOthersValueChain() == 1)
                    valueChainList.put("6");

                postData.put("id_value_chain", valueChainList);

                final PlanModal correctionPlan = dbHandler.correctionPlanSelectRow(planList.get(position).getPlanID(), planList.get(position).getUserID());
                JSONObject editedData = new JSONObject();

                JSONObject info = new JSONObject();
                if (!correctionPlan.getPhone().isEmpty())
                    info.put("phone", correctionPlan.getPhone());
                if (!correctionPlan.getFixedPhone().isEmpty())
                    info.put("tel", correctionPlan.getFixedPhone());
                if (!correctionPlan.getEducation().isEmpty())
                    info.put("id_degrees", correctionPlan.getEducation());
                info.put("id_fields", correctionPlan.getFieldOfStudy());
                info.put("id_proficiency", correctionPlan.getSpecialization());
                if (!correctionPlan.getHouseholds().isEmpty())
                    info.put("id_supervisor", correctionPlan.getHouseholds());

                editedData.put("info", info);

                JSONObject plan = new JSONObject();
                if (!correctionPlan.getPlanTitle().isEmpty())
                    plan.put("id_title_plan", correctionPlan.getPlanTitle());
                if (!correctionPlan.getPlanEconomicSection().isEmpty())
                    plan.put("id_type_activity", correctionPlan.getPlanEconomicSection());
                if (!correctionPlan.getPlanSituation().isEmpty())
                    plan.put("id_activity_status", correctionPlan.getPlanSituation());
                if (!correctionPlan.getPlanStartDate().isEmpty())
                    plan.put("date_activity", correctionPlan.getPlanStartDate());

                editedData.put("tarh", plan);

//                JSONObject location = new JSONObject();
//                if (!correctionPlan.getPlanRegionLocation().isEmpty())
//                    location.put("id_job_position", correctionPlan.getPlanRegionLocation());
//
//                editedData.put("location", location);

                JSONArray license = new JSONArray();
                ArrayList<LicenseModal> licenseModalArrayList = dbHandler.readCorrectionLicenses(planList.get(position).getPlanID());
                for (int i = 0; i < licenseModalArrayList.size(); i++) {
                    JSONObject licenseData = new JSONObject();
                    if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty() || !licenseModalArrayList.get(i).getLicenseType().isEmpty() ||
                            !licenseModalArrayList.get(i).getLicenseDate().isEmpty() || !licenseModalArrayList.get(i).getOrganID().isEmpty()) {
                        licenseData.put("id", licenseModalArrayList.get(i).getLicenseID());
                        if (!licenseModalArrayList.get(i).getOrganID().isEmpty())
                            licenseData.put("id_executive_device", licenseModalArrayList.get(i).getOrganID());
                        if (!licenseModalArrayList.get(i).getLicenseType().isEmpty())
                            licenseData.put("id_type_license", licenseModalArrayList.get(i).getLicenseType());
                        if (!licenseModalArrayList.get(i).getLicenseNumber().isEmpty())
                            licenseData.put("license_number", licenseModalArrayList.get(i).getLicenseNumber());
                        if (!licenseModalArrayList.get(i).getLicenseDate().isEmpty())
                            licenseData.put("date_license_number", licenseModalArrayList.get(i).getLicenseDate());

                        license.put(licenseData);
                    }
                }

                editedData.put("license", license);

                JSONArray financing = new JSONArray();
                ArrayList<FacilityModal> facilityModalArrayList = dbHandler.readCorrectionFacilities(planList.get(position).getPlanID());
                for (int i = 0; i < facilityModalArrayList.size(); i++) {
                    JSONObject facilityData = new JSONObject();
                    if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty() || !facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getFixedBankID().isEmpty() || !facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty() ||
                            !facilityModalArrayList.get(i).getWorkingBankID().isEmpty() || !facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty() ||
                            !facilityModalArrayList.get(i).getDateFacilities().isEmpty() || !facilityModalArrayList.get(i).getOrganID().isEmpty()) {
                        facilityData.put("id", facilityModalArrayList.get(i).getFinanceID());
                        if (!facilityModalArrayList.get(i).getOrganID().isEmpty())
                            facilityData.put("id_executive_device", facilityModalArrayList.get(i).getOrganID());
                        if (!facilityModalArrayList.get(i).getTotalFacilitiesAmount().isEmpty())
                            facilityData.put("money", facilityModalArrayList.get(i).getTotalFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getContributedFacilitiesAmount().isEmpty())
                            facilityData.put("brought_person", facilityModalArrayList.get(i).getContributedFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getSupplyFacilitiesID().isEmpty())
                            facilityData.put("id_location_of_credit", facilityModalArrayList.get(i).getSupplyFacilitiesID());
                        if (!facilityModalArrayList.get(i).getDateFacilities().isEmpty())
                            facilityData.put("id_location_of_credit_date", facilityModalArrayList.get(i).getDateFacilities());
                        if (!facilityModalArrayList.get(i).getShareFacilitiesAmount().isEmpty())
                            facilityData.put("loan", facilityModalArrayList.get(i).getShareFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getFixedFacilityAmount().isEmpty())
                            facilityData.put("fixed_capital", facilityModalArrayList.get(i).getFixedFacilityAmount());
                        if (!facilityModalArrayList.get(i).getFixedBankID().isEmpty())
                            facilityData.put("id_bank_fixed_capital", facilityModalArrayList.get(i).getFixedBankID());
                        if (!facilityModalArrayList.get(i).getWorkingFacilitiesAmount().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingFacilitiesAmount());
                        if (!facilityModalArrayList.get(i).getWorkingBankID().isEmpty())
                            facilityData.put("working_capital", facilityModalArrayList.get(i).getWorkingBankID());

                        financing.put(facilityData);
                    }
                }

                editedData.put("financing", financing);

                postData.put("data_edit", editedData);
            }

        } catch (Exception e) {
            errorToastMaker("خطای بسته بندی اطلاعات ارسالی", new View(context));
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, context.getString(R.string.URLUpload) + planModal.getPlanID(), postData,
                response -> {
                    Log.i("test", response.toString());
                    try {
                        Log.i("test", response.toString());
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                            alertToastMaker("دوباره تلاش کنید.", new View(context));
                            inProgress.dismiss();
                        }
                        // error in Sending data
                        else if (response.getString("status").equals("0")) {
                            errorToastMaker("خطا در بارگذاری اطلاعات!\nاطلاعات پایش شده را مجدداً بررسی کنید.", new View(context));
                            ArrayList<String> errors = new ArrayList<>();
                            JSONArray jsonArray = new JSONArray();
                            try {
                                jsonArray = new JSONArray(response.getString("message"));
                            } catch (JSONException ignored) {

                            }

                            for (int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++) {
                                try {
                                    errors.add(i + 1 + "- " + jsonArray.get(i).toString());
                                } catch (JSONException ignored) {
                                }
                            }
                            if (jsonArray.length() == 0) {
                                errors.add(response.getString("message"));
                            }

                            Dialog dialog = new Dialog(context);

                            dialog.setContentView(R.layout.custom_ready_to_send_upload_error);
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.setCanceledOnTouchOutside(false);

                            Button confirm = dialog.findViewById(R.id.confirm);
                            RecyclerView appNewFeaturesRecyclerView = dialog.findViewById(R.id.errorsRecyclerView);

                            AppNewFeaturesRecyclerViewAdapter adapter = new AppNewFeaturesRecyclerViewAdapter(errors);

                            appNewFeaturesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                            appNewFeaturesRecyclerView.setAdapter(adapter);

                            confirm.setOnClickListener(v -> dialog.dismiss());

                            dialog.show();

                            inProgress.dismiss();
                        }
                        // upload successfully done
                        else if (response.getString("status").equals("1")) {
                            planModal.setIsCompleted(2);
                            dbHandler.updatePlanInfo(planModal);
                            planList.remove(position);
                            notifyItemRemoved(position);
                            createDoneDialog();
                            inProgress.dismiss();
                            if (planList.size() > 0)
                                ReadyToSendRequestsActivity.search.show();
                            if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() != View.VISIBLE) {
                                ReadyToSendRequestsActivity.offlineLayout.setVisibility(View.VISIBLE);
                                ReadyToSendRequestsActivity.search.hide();
                            } else if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() == View.VISIBLE) {
                                ReadyToSendRequestsActivity.notFound.setVisibility(View.VISIBLE);
                                ReadyToSendRequestsActivity.search.hide();
                            }
                            //to be removed
//                            successfulToastMaker("status 1 successful",new View(context));
//                            successfulToastMaker("بارگذاری طرح با موفقیت انجام شد.", new View(context));
                            //

                        }
                        //  upload unsuccessful cause user has no access to this task
                        else if (response.getString("status").equals("2")) {
                            errorToastMaker("شما هم اکنون به این مورد دسترسی ندارید!", new View(context));
                            inProgress.dismiss();
                        }
//    upload won't do cause the managers already accepted the plan
//     plan just removed from list and send to completed plans
                        else if (response.getString("status").equals("3")) {
                            planModal.setIsCompleted(2);
                            dbHandler.updatePlanInfo(planModal);
                            planList.remove(position);
                            notifyItemRemoved(position);
                            inProgress.dismiss();
                            errorToastMaker("این طرح قبلاً بارگذاری شده و توسط مدیران به تأیید رسیده است.", new View(context));
//                            //to be removed
//                            errorToastMaker("status 3", new View(context));
                            //
                            if (planList.size() > 0)
                                ReadyToSendRequestsActivity.search.show();
                            if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() != View.VISIBLE) {
                                ReadyToSendRequestsActivity.offlineLayout.setVisibility(View.VISIBLE);
                                ReadyToSendRequestsActivity.search.hide();
                            } else if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() == View.VISIBLE) {
                                ReadyToSendRequestsActivity.notFound.setVisibility(View.VISIBLE);
                                ReadyToSendRequestsActivity.search.hide();
                            }
                        }

                    } catch (JSONException e) {
                        inProgress.dismiss();
                        errorToastMaker("خطای سرویس اطلاعات ارسالی", new View(context));
                        e.printStackTrace();
                    }
                },
                error -> {
                    inProgress.dismiss();
                    errorToastMaker("ارتباط با سرور برقرار نشد!\nارتباط اینترنتی خود را بررسی کنید!", new View(context));
                    error.printStackTrace();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + dbHandler.getUser().getToken());
                return headers;
            }
        };

        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
            } catch (JSONException e) {
                // this will never happen but if so, show error to user.
            }
        }, error -> {
            // show error to user. refresh failed.
            Log.i("test", new String(error.networkResponse.data));

        });
        requestQueue.add(refreshTokenRequest);
        refreshTokenRequest.setRetryPolicy(new DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    private void createDoneDialog() {
        done = new Dialog(context);

        done.setContentView(R.layout.custom_ready_to_send_upload_done_layout);
        Objects.requireNonNull(done.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        done.setCanceledOnTouchOutside(false);

        Button confirm = done.findViewById(R.id.confirm);
        successfulToastMaker("بارگذاری طرح با موفقیت انجام شد.", new View(context));
        notifyDataSetChanged();
        confirm.setOnClickListener(v -> done.dismiss());
        done.show();

        if (planList.size() > 0)
            ReadyToSendRequestsActivity.search.show();
        if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() != View.VISIBLE) {
            ReadyToSendRequestsActivity.offlineLayout.setVisibility(View.VISIBLE);
            ReadyToSendRequestsActivity.readyToSendRecyclerView.setVisibility(View.INVISIBLE);
            ReadyToSendRequestsActivity.search.hide();
        } else if (planList.size() == 0 && ReadyToSendRequestsActivity.searchedContents.getVisibility() == View.VISIBLE) {
            ReadyToSendRequestsActivity.notFound.setVisibility(View.VISIBLE);
            ReadyToSendRequestsActivity.search.hide();
        }
    }

    private void createUploadDialog() {
        inProgress = new Dialog(context);

        inProgress.setContentView(R.layout.custom_ready_to_send_upload_in_progress_layout);
        Objects.requireNonNull(inProgress.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        inProgress.setCanceledOnTouchOutside(false);

        ProgressBar progressBar = inProgress.findViewById(R.id.sendingProgressBar);
        LottieAnimationView animationView = inProgress.findViewById(R.id.animationView);

        progressBar.setProgress(0);
        progressBar.setMax(1);

        animationView.setMaxHeight(animationView.getWidth());

        inProgress.show();

    }

}
