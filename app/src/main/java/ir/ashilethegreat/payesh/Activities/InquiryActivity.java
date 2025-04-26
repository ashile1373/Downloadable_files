package ir.ashilethegreat.payesh.Activities;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.InquiryRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.InquiryModal;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class InquiryActivity extends AppCompatActivity {

    ImageView back;
    CardView inquiryResultLayout, inquirySearchLayout, notFound;
    LinearLayout bottomBar;
    Button startInquiry, inquiryAgain, searchAgain;
    RecyclerView inquiryRecyclerView;
    ArrayList<InquiryModal> planList;
    ArrayList<String> planSpacerName;
    TextInputLayout inquiryLayout;
    InquiryRecyclerViewAdapter adapter;
    EditText inquiry;
    ProgressBar inquiryProgressBar;

    DBHandler dbHandler;
    final int DEFAULT_TIMEOUT = 20000;
    String URLNextPage = "";
    String URLastPage = "";
    RequestQueue requestQueue;
    String natCode = "";
    JSONObject jsonBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        back = findViewById(R.id.back);
        inquiry = findViewById(R.id.inquiry);
        inquiryLayout = findViewById(R.id.inquiryLayout);
        inquiryResultLayout = findViewById(R.id.inquiryResultLayout);
        inquirySearchLayout = findViewById(R.id.inquirySearchLayout);
        inquiryRecyclerView = findViewById(R.id.inquiryRecyclerView);
        notFound = findViewById(R.id.notFound);
        bottomBar = findViewById(R.id.bottomBar);
        startInquiry = findViewById(R.id.startInquiry);
        inquiryAgain = findViewById(R.id.inquiryAgain);
        searchAgain = findViewById(R.id.searchAgain);
        inquiryProgressBar = findViewById(R.id.inquiryProgressBar);

        dbHandler = new DBHandler(this);
        requestQueue = Volley.newRequestQueue(this);
        natCode = Objects.requireNonNull(getIntent().getExtras()).getString("natCode");

        inquiry.setText(natCode);

        inquiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inquiryLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        startInquiry.setOnClickListener(v -> {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            if (inquiry.getText().toString().length() < 10)
                inquiryLayout.setError("کد ملی را به شکل درست بنویسید!");
            else {
                startInquiry.setVisibility(View.INVISIBLE);
                inquiryProgressBar.setVisibility(View.VISIBLE);
                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("national_code", inquiry.getText().toString());
                } catch (JSONException ignored) {
                    // never thrown in this case
                }
                inquiryResult(jsonBody);
            }
        });

        inquiryAgain.setOnClickListener(v -> {
            notFound.setVisibility(View.GONE);
            inquiryResultLayout.setVisibility(View.GONE);
            bottomBar.setVisibility(View.GONE);
            inquirySearchLayout.setVisibility(View.VISIBLE);
            startInquiry.setVisibility(View.VISIBLE);
            inquiryProgressBar.setVisibility(View.INVISIBLE);
        });

        searchAgain.setOnClickListener(v -> inquiryAgain.performClick());

        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0,0);
    }

    private void inquiryResult(JSONObject jsonBody) {

        inquiryResultLayout.setVisibility(View.INVISIBLE);
        planList = new ArrayList<>();
        planSpacerName = new ArrayList<>();

        JsonObjectRequest jsonRequest1 = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLInquiry), jsonBody,
                response -> {
                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response.getString("status").equals("1")) {
                            JSONObject body = new JSONObject(response.getString("data"));
                            URLNextPage = body.getString("next_page_url");
                            URLastPage = body.getString("last_page_url");

                            JSONArray jsonArray = body.getJSONArray("data");
                            if (jsonArray.length() == 0) {
                                notFound.setVisibility(View.VISIBLE);
                                inquiryResultLayout.setVisibility(View.GONE);
                                bottomBar.setVisibility(View.GONE);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    InquiryModal plan = new InquiryModal();
                                    JSONObject data = jsonArray.getJSONObject(i);

                                    JSONObject monitoringArray = data.getJSONObject("monitoring");
                                    if (!monitoringArray.getString("name").equalsIgnoreCase("null"))
                                        plan.setSupervisionName(monitoringArray.getString("name"));
                                    else plan.setSupervisionName("");
                                    planList.add(plan);

                                    plan = new InquiryModal();

                                    plan.setPlanID(data.getString("id"));
                                    if (!data.getString("work_prosess").equalsIgnoreCase("null"))
                                        plan.setPlanStage(data.getString("work_prosess"));
                                    else plan.setPlanStage("تعریف نشده");
                                    if (!data.getString("status_inspection").equalsIgnoreCase("null"))
                                        plan.setPlanSituation(data.getString("status_inspection"));
                                    else plan.setPlanSituation("");

                                    JSONObject planData = data.getJSONObject("tarh_api");
                                    plan.setPlanTitle(planData.getString("id_title_plan"));

                                    JSONObject employerArray = data.getJSONObject("karfarma");
                                    plan.setName(employerArray.getString("name"));
                                    plan.setFamilyName(employerArray.getString("family"));
                                    plan.setNatCode(employerArray.getString("national_code"));
                                    if (!employerArray.getString("phone").equalsIgnoreCase("null"))
                                        plan.setPhone(employerArray.getString("phone"));
                                    else plan.setPhone("");

                                    JSONObject supervisor = data.getJSONObject("inspection_user_api");
                                    if (!supervisor.getString("name").equalsIgnoreCase("null"))
                                        plan.setInspectorName(supervisor.getString("name"));
                                    else plan.setInspectorName("");
                                    if (!supervisor.getString("family").equalsIgnoreCase("null"))
                                        plan.setInspectorFamilyName(supervisor.getString("family"));
                                    else plan.setInspectorFamilyName("");

                                    planList.add(plan);

                                }
                                inquiryResultLayout.setVisibility(View.VISIBLE);
                                inquirySearchLayout.setVisibility(View.GONE);
                                bottomBar.setVisibility(View.GONE);
                                notFound.setVisibility(View.GONE);

                                adapter = new InquiryRecyclerViewAdapter(planList, InquiryActivity.this);
                                inquiryRecyclerView.setLayoutManager(new LinearLayoutManager(InquiryActivity.this));
                                inquiryRecyclerView.setAdapter(adapter);
                            }

                        } else if (response.getString("status").equals("0")) {
                            inquirySearchLayout.setVisibility(View.GONE);
                            inquiryResultLayout.setVisibility(View.GONE);
                            bottomBar.setVisibility(View.GONE);
                            notFound.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس دریافت اطلاعات");
                        startInquiry.setVisibility(View.VISIBLE);
                        inquiryProgressBar.setVisibility(View.INVISIBLE);
//                        e.printStackTrace();
                    }
                },
                error -> {
                    errorToastMaker("خطای سرویس استعلام");
                    startInquiry.setVisibility(View.VISIBLE);
                    inquiryProgressBar.setVisibility(View.INVISIBLE);
//                    error.printStackTrace();
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
    }

    private void refreshAccessToken() {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", dbHandler.getUser().getUsername());
            jsonBody.put("password", dbHandler.getUser().getPassword());

        } catch (JSONException ignored) {
            // never thrown in this case
        }

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
            } catch (JSONException e) {
                errorToastMaker("خطای سرویس ساخت توکن");
            }
        }, error -> {
            // show error to user. refresh failed.
            errorToastMaker("ارتباط با سرور برقرار نشد!");

        });
        requestQueue.add(refreshTokenRequest);
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(InquiryActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}