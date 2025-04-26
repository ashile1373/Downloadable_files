package ir.ashilethegreat.payesh.Activities;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.Adapters.ReadyToSendRequestsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class ReadyToSendRequestsActivity extends AppCompatActivity {

    public static CardView searchedContents, notFound;
    CardView searchLayout;
    public static FloatingActionButton toEnd;
    public static ExtendedFloatingActionButton search;
    Button backToHome, searchStart, searchAgain, searchClose, searchedContentsClose;
    LinearLayout /*searchedContentsPhoneNumberLayout,*/ searchedContentsNationalCodeLayout, searchedContentsFamilyLayout,
            searchedContentsNameLayout;
    TextInputLayout searchNameLayout, searchFamilyLayout, searchNationalCodeLayout;
    EditText searchName, searchFamily, searchNationalCode/*, searchPhoneNumber*/;
    TextView searchedContentsPhoneNumber, searchedContentsNationalCode, searchedContentsFamily, searchedContentsName;

    ImageView back;
    public static RecyclerView readyToSendRecyclerView;
    public static CardView offlineLayout;
    ArrayList<PlanModal> readyToSend;
    ReadyToSendRequestsRecyclerViewAdapter adapter;

    DBHandler dbHandler;
    String userID;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready_to_send_requests);

        back = findViewById(R.id.back);
        readyToSendRecyclerView = findViewById(R.id.readyToSendRecyclerView);
        backToHome = findViewById(R.id.backToHome);
        offlineLayout = findViewById(R.id.offlineLayout);
        toEnd = findViewById(R.id.toEnd);
        search = findViewById(R.id.search);
        searchLayout = findViewById(R.id.searchLayout);
        searchClose = findViewById(R.id.searchClose);
        searchName = findViewById(R.id.searchName);
        searchFamily = findViewById(R.id.searchFamily);
        searchNationalCode = findViewById(R.id.searchNationalCode);
        searchNameLayout = findViewById(R.id.searchNameLayout);
        searchFamilyLayout = findViewById(R.id.searchFamilyLayout);
        searchNationalCodeLayout = findViewById(R.id.searchNationalCodeLayout);
//        searchPhoneNumber = findViewById(R.id.searchPhoneNumber);
        searchStart = findViewById(R.id.searchStart);
        searchedContents = findViewById(R.id.searchedContents);
        searchedContentsPhoneNumber = findViewById(R.id.searchedContentsPhoneNumber);
        searchedContentsNationalCode = findViewById(R.id.searchedContentsNationalCode);
        searchedContentsFamily = findViewById(R.id.searchedContentsFamily);
        searchedContentsName = findViewById(R.id.searchedContentsName);
//        searchedContentsPhoneNumberLayout = findViewById(R.id.searchedContentsPhoneNumberLayout);
        searchedContentsNationalCodeLayout = findViewById(R.id.searchedContentsNationalCodeLayout);
        searchedContentsFamilyLayout = findViewById(R.id.searchedContentsFamilyLayout);
        searchedContentsNameLayout = findViewById(R.id.searchedContentsNameLayout);
        searchedContentsClose = findViewById(R.id.searchedContentsClose);
        searchAgain = findViewById(R.id.searchAgain);
        notFound = findViewById(R.id.notFound);

        dbHandler = new DBHandler(ReadyToSendRequestsActivity.this);
        userID = dbHandler.getUser().getId();
        requestQueue = Volley.newRequestQueue(this);
        readyToSend = dbHandler.readPlans(userID, 1);

        if (readyToSend.size() == 0) {
            offlineLayout.setVisibility(View.VISIBLE);
            readyToSendRecyclerView.setVisibility(View.INVISIBLE);
            search.hide();
            toEnd.setVisibility(View.INVISIBLE);
            notFound.setVisibility(View.GONE);
        } else {
            offlineLayout.setVisibility(View.INVISIBLE);
            search.show();
            readyToSendRecyclerView.setVisibility(View.VISIBLE);
            toEnd.setVisibility(View.VISIBLE);
            notFound.setVisibility(View.GONE);
        }

        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchFamily.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchFamilyLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchNationalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNationalCodeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        adapter = new ReadyToSendRequestsRecyclerViewAdapter(readyToSend, userID, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        readyToSendRecyclerView.setLayoutManager(linearLayoutManager);
        readyToSendRecyclerView.scrollToPosition(readyToSend.size()-1);
        readyToSendRecyclerView.setAdapter(adapter);


        readyToSendRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    toEnd.show();
                    search.hide();
                } else if (dy < 0) {
                    toEnd.hide();
                    if (searchLayout.getVisibility() == View.INVISIBLE && offlineLayout.getVisibility() != View.VISIBLE)
                        search.show();
                }
                if (!readyToSendRecyclerView.canScrollVertically(1) && readyToSend.size() > 0) {
                    toEnd.hide();
                }
            }
        });

        toEnd.setOnClickListener(v -> readyToSendRecyclerView.smoothScrollToPosition(0));

        back.setOnClickListener(v -> onBackPressed());
        backToHome.setOnClickListener(v -> onBackPressed());

        search.setOnClickListener(v -> {
            searchLayout.setVisibility(View.VISIBLE);
//            searchedContents.setVisibility(View.GONE);
            search.hide();
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        searchStart.setOnClickListener(v -> {
            if (searchName.getText().toString().isEmpty() && searchFamily.getText().toString().isEmpty() &&
                    searchNationalCode.getText().toString().isEmpty() /*&& searchPhoneNumber.getText().toString().isEmpty()*/)
                alertToastMaker("حداقل یک مورد را وارد کنید!");
            else if (checkErrors()) {
                alertToastMaker("خطاهای جستجو را بررسی کنید!");
            } else {
                View view = this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                searchedContentsNameLayout.setVisibility(View.GONE);
                searchedContentsFamilyLayout.setVisibility(View.GONE);
                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                String searchedNationalCode = "", searchedName = "", searchedFamily = "";
                readyToSend = new ArrayList<>();

                if (!searchName.getText().toString().isEmpty()) {
                    searchedContentsNameLayout.setVisibility(View.VISIBLE);
                    searchedName = searchName.getText().toString();
                    searchedContentsName.setText(searchedName);

                }
                if (!searchFamily.getText().toString().isEmpty()) {
                    searchedContentsFamilyLayout.setVisibility(View.VISIBLE);
                    searchedFamily = searchFamily.getText().toString();
                    searchedContentsFamily.setText(searchedFamily);

                }
                if (!searchNationalCode.getText().toString().isEmpty()) {
                    searchedContentsNationalCodeLayout.setVisibility(View.VISIBLE);
                    searchedNationalCode = searchNationalCode.getText().toString();
                    searchedContentsNationalCode.setText(searchedNationalCode);

                }

                readyToSend = dbHandler.loadSearchedPlans(searchedName, searchedFamily, searchedNationalCode, userID, 1);

//                shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
//                shimmerFrameLayoutPlaceHolder.startShimmer();
                readyToSendRecyclerView.setVisibility(View.INVISIBLE);
                notFound.setVisibility(View.GONE);
                search.show();
//                searchedContentsNameLayout.setVisibility(View.GONE);
//                searchedContentsFamilyLayout.setVisibility(View.GONE);
//                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                searchedContents.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.INVISIBLE);

                if (readyToSend.size() == 0) {
                    readyToSendRecyclerView.setVisibility(View.GONE);
                    search.hide();
                    notFound.setVisibility(View.VISIBLE);
                    toEnd.hide();
                } else {
                    adapter = new ReadyToSendRequestsRecyclerViewAdapter(readyToSend, userID, this);
                    readyToSendRecyclerView.setAdapter(adapter);
                    readyToSendRecyclerView.setVisibility(View.VISIBLE);
                    search.show();
                    notFound.setVisibility(View.GONE);
                    if (readyToSendRecyclerView.canScrollVertically(1)) {
                        toEnd.show();
                    }
                }
            }
        });

        searchClose.setOnClickListener(v -> {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
            searchLayout.setVisibility(View.INVISIBLE);
            if (notFound.getVisibility() == View.VISIBLE)
                search.hide();
            else search.show();
        });

        searchAgain.setOnClickListener(v -> search.performClick());

        searchedContentsClose.setOnClickListener(v -> {
            searchedContents.setVisibility(View.GONE);
            notFound.setVisibility(View.GONE);
            readyToSendRecyclerView.setVisibility(View.VISIBLE);
            search.show();

            readyToSend = dbHandler.readPlans(userID,1);
            adapter = new ReadyToSendRequestsRecyclerViewAdapter(readyToSend, userID, this);
            readyToSendRecyclerView.setAdapter(adapter);

            if (readyToSend.size() == 0) {
                searchedContents.setVisibility(View.GONE);
                notFound.setVisibility(View.GONE);
                readyToSendRecyclerView.setVisibility(View.GONE);
                offlineLayout.setVisibility(View.VISIBLE);
                search.hide();
            }

            if (readyToSendRecyclerView.canScrollVertically(1)) {
                toEnd.show();
            }

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        });
    }

    @Override
    public void onBackPressed() {
        if (requestQueue != null)
            requestQueue.cancelAll(this);

        if (searchLayout.getVisibility() == View.VISIBLE)
            searchClose.performClick();
        else if (searchedContents.getVisibility() == View.VISIBLE)
            searchedContentsClose.performClick();
        else {
            super.onBackPressed();
            Intent intent = new Intent(ReadyToSendRequestsActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    private boolean checkErrors() {
        boolean retVal = false;
        if (!searchName.getText().toString().isEmpty()) {
            if (searchName.getText().toString().length() < 2) {
                searchNameLayout.setError("نام نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchFamily.getText().toString().isEmpty()) {
            if (searchFamily.getText().toString().length() < 2) {
                searchFamilyLayout.setError("نام خانوادگی نمی تواند کمتر از 2 حرف داشته باشد!");
                retVal = true;
            }
        }
        if (!searchNationalCode.getText().toString().isEmpty()) {
            if (searchNationalCode.getText().toString().length() < 10) {
                searchNationalCodeLayout.setError("کد ملی باید 10 رقم باشد!");
                retVal = true;
            }
        }
        return retVal;
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(ReadyToSendRequestsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(ReadyToSendRequestsActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null)
            requestQueue.cancelAll(this);
    }
}