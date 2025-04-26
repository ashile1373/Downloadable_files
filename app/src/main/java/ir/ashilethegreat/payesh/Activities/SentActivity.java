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

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ir.ashilethegreat.payesh.Adapters.SentRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.PlanModal;
import ir.ashilethegreat.payesh.R;

public class SentActivity extends AppCompatActivity {

    public static CardView searchedContents, notFound;
    CardView searchLayout;
    FloatingActionButton toEnd;
    public static ExtendedFloatingActionButton search;
    Button backToHome, searchStart, searchAgain, searchClose, searchedContentsClose;
    LinearLayout /*searchedContentsPhoneNumberLayout,*/ searchedContentsNationalCodeLayout, searchedContentsFamilyLayout,
            searchedContentsNameLayout;
    TextInputLayout searchNameLayout, searchFamilyLayout, searchNationalCodeLayout;
    EditText searchName, searchFamily, searchNationalCode/*, searchPhoneNumber*/;
    TextView searchedContentsPhoneNumber, searchedContentsNationalCode, searchedContentsFamily, searchedContentsName;

    ImageView back;
    LinearLayout offlineLayout;
    RecyclerView sentRecyclerView;
    ArrayList<PlanModal> sentPlans;
    SentRecyclerViewAdapter adapter;
    DBHandler dbHandler;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent);

        back = findViewById(R.id.back);
        sentRecyclerView = findViewById(R.id.sentRecyclerView);
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

        dbHandler = new DBHandler(this);
        userID = dbHandler.getUser().getId();
        sentPlans = dbHandler.readPlans(userID,2);

        if (sentPlans.size() == 0) {
            offlineLayout.setVisibility(View.VISIBLE);
            sentRecyclerView.setVisibility(View.INVISIBLE);
            search.hide();
            toEnd.setVisibility(View.INVISIBLE);
            notFound.setVisibility(View.GONE);
        } else {
            offlineLayout.setVisibility(View.INVISIBLE);
            search.show();
            sentRecyclerView.setVisibility(View.VISIBLE);
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

        adapter = new SentRecyclerViewAdapter(sentPlans , this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        sentRecyclerView.setLayoutManager(linearLayoutManager);
        sentRecyclerView.scrollToPosition(sentPlans.size()-1);
        sentRecyclerView.setAdapter(adapter);

        sentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (!sentRecyclerView.canScrollVertically(1) && sentPlans.size() > 0) {
                    toEnd.hide();
                }
            }
        });

        toEnd.setOnClickListener(v -> sentRecyclerView.smoothScrollToPosition(0));

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
                sentPlans = new ArrayList<>();

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

                sentPlans = dbHandler.loadSearchedPlans(searchedName, searchedFamily, searchedNationalCode, userID , 2);

//                shimmerFrameLayoutPlaceHolder.setVisibility(View.VISIBLE);
//                shimmerFrameLayoutPlaceHolder.startShimmer();
                sentRecyclerView.setVisibility(View.INVISIBLE);
                notFound.setVisibility(View.GONE);
                search.show();
//                searchedContentsNameLayout.setVisibility(View.GONE);
//                searchedContentsFamilyLayout.setVisibility(View.GONE);
//                searchedContentsNationalCodeLayout.setVisibility(View.GONE);
//                searchedContentsPhoneNumberLayout.setVisibility(View.GONE);
                searchedContents.setVisibility(View.VISIBLE);
                searchLayout.setVisibility(View.INVISIBLE);

                if (sentPlans.size() == 0) {
                    sentRecyclerView.setVisibility(View.GONE);
                    search.hide();
                    notFound.setVisibility(View.VISIBLE);
                    toEnd.hide();
                } else {
                    adapter = new SentRecyclerViewAdapter(sentPlans, this);
                    sentRecyclerView.setAdapter(adapter);
                    sentRecyclerView.setVisibility(View.VISIBLE);
                    search.show();
                    notFound.setVisibility(View.GONE);
                    if (sentRecyclerView.canScrollVertically(1)) {
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
            sentRecyclerView.setVisibility(View.VISIBLE);

            sentPlans = dbHandler.readPlans(userID,2);
            adapter = new SentRecyclerViewAdapter(sentPlans, this);
            sentRecyclerView.setAdapter(adapter);
            search.show();
            if (sentRecyclerView.canScrollVertically(1)) {
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
        if (searchLayout.getVisibility() == View.VISIBLE)
            searchClose.performClick();
        else if (searchedContents.getVisibility() == View.VISIBLE)
            searchedContentsClose.performClick();
        else {
            super.onBackPressed();
            Intent intent = new Intent(SentActivity.this, HomeActivity.class);
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
        Toast toast = new Toast(SentActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

        TextView messageText = layout.findViewById(R.id.toastMessage);
        messageText.setText(message);
        Toast toast = new Toast(SentActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}