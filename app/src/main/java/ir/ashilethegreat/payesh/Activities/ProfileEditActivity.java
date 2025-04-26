package ir.ashilethegreat.payesh.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class ProfileEditActivity extends AppCompatActivity {

    ImageView back;
    EditText editName, editFamilyName, editPhone;
    TextInputLayout editNameLayout, editFamilyNameLayout, editPhoneLayout;
    ExtendedFloatingActionButton confirmEdit;
    ProgressBar confirmEditProgressBar;
    DBHandler dbHandler;
    RequestQueue requestQueue;
    UserModal userModal;
    final int DEFAULT_TIMEOUT = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        back = findViewById(R.id.back);
        editName = findViewById(R.id.editName);
        editFamilyName = findViewById(R.id.editFamilyName);
        editPhone = findViewById(R.id.editPhone);
        editNameLayout = findViewById(R.id.editNameLayout);
        editFamilyNameLayout = findViewById(R.id.editFamilyNameLayout);
        editPhoneLayout = findViewById(R.id.editPhoneLayout);
        confirmEdit = findViewById(R.id.confirmEdit);
        confirmEditProgressBar = findViewById(R.id.confirmEditProgressBar);

        dbHandler = new DBHandler(this);
        requestQueue = Volley.newRequestQueue(this);

        userModal = dbHandler.getUser();

        if (!userModal.getPhone().equals("null"))
            editPhone.setText(userModal.getPhone());
        if (!userModal.getFirstName().equals("null"))
            editName.setText(userModal.getFirstName());
        if (!userModal.getLastName().equals("null"))
            editFamilyName.setText(userModal.getLastName());

        editName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editFamilyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editFamilyNameLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                editPhoneLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmEdit.setOnClickListener(v -> {
            if (checkErrors())
                alertToastMaker("خطاهای فرم را برطرف کنید.");
            else {
                confirmEditProgressBar.setVisibility(View.VISIBLE);
                confirmEdit.setVisibility(View.INVISIBLE);
                createConfirmDialog(editName.getText().toString().trim(), editFamilyName.getText().toString().trim(), editPhone.getText().toString().trim());
            }
        });

        back.setOnClickListener(v -> onBackPressed());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileEditActivity.this, ProfileDetailsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    private void createConfirmDialog(String name, String familyName, String phone) {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_profile_edit_confirmation_dialog_layout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        Button confirm = dialog.findViewById(R.id.confirm);
        Button cancel = dialog.findViewById(R.id.cancel);

        confirm.setOnClickListener(v -> {
            updateProfileDetails(name, familyName, phone, dialog);
            dialog.dismiss();
        });

        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            confirmEditProgressBar.setVisibility(View.INVISIBLE);
            confirmEdit.setVisibility(View.VISIBLE);
        });

        dialog.show();
    }

    private void updateProfileDetails(String name, String familyName, String phone, Dialog dialog) {
        JSONObject jsonBody = new JSONObject();
        try {
            if (name.length() > 0) {
                jsonBody.put("name", name);
                userModal.setFirstName(name);
            }
            if (familyName.length() > 0) {
                jsonBody.put("family", familyName);
                userModal.setLastName(familyName);
            }
            if (phone.length() == 11) {
                jsonBody.put("phone", phone);
                userModal.setPhone(phone);
            }
        } catch (JSONException ignored) {
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLEditProfile), jsonBody,
                response -> {

                    try {
                        if (response.getString("status").equals("Token is Expired")) {
                            refreshAccessToken();
                        } else if (response.getString("status").equals("1")) {
                            successfulToastMaker("اطلاعات کاربری با موفقیت تغییر یافت!");
                            dialog.dismiss();
                            dbHandler.updateUserInfo(userModal);
                            Intent intent = new Intent(ProfileEditActivity.this, ProfileDetailsActivity.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        } else {
                            confirmEditProgressBar.setVisibility(View.INVISIBLE);
                            confirmEdit.setVisibility(View.VISIBLE);
                            errorToastMaker("متأسفانه خطایی رخ داد! دوباره تلاش کنید.");
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس به روز رسانی اطلاعات کاربری");
                        confirmEditProgressBar.setVisibility(View.INVISIBLE);
                        confirmEdit.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    errorToastMaker("متأسفانه خطایی رخ داد! ارتباط اینترنتی خود را بررسی کنید.");
                    confirmEditProgressBar.setVisibility(View.INVISIBLE);
                    confirmEdit.setVisibility(View.VISIBLE);
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                UserModal user = dbHandler.getUser();
                headers.put("Authorization", "Bearer " + user.getToken());
                return headers;
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                return super.parseNetworkError(volleyError);
            }
        };

        requestQueue.add(jsonRequest);
        jsonRequest.setRetryPolicy(new

                DefaultRetryPolicy(
                DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }


    private boolean checkErrors() {
        boolean retVal = false;

        if (editName.getText().toString().trim().length() < 2) {
            editNameLayout.setError("نام حداقل باید دو حرف داشته باشد!");
            retVal = true;
        }
        if (editName.getText().toString().trim().isEmpty()) {
            editNameLayout.setError("نام نمی تواند خالی باشد!");
            retVal = true;
        }
        if (editFamilyName.getText().toString().trim().length() < 2) {
            editFamilyNameLayout.setError("نام خانوادگی حداقل باید دو حرف داشته باشد!");
            retVal = true;
        }
        if (editFamilyName.getText().toString().trim().isEmpty()) {
            editFamilyNameLayout.setError("نام خانوادگی نمی تواند خالی باشد!");
            retVal = true;
        }
        if (!editPhone.getText().toString().trim().startsWith("09")) {
            editPhoneLayout.setError("شماره تلفن همراه را به شکل درست بنویسید!");
            retVal = true;
        }
        if (editPhone.getText().toString().trim().isEmpty()) {
            editPhoneLayout.setError("شماره تلفن نمی تواند خالی باشد!");
            retVal = true;
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

        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void successfulToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_successful, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void alertToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_alert, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(this);
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

        JsonObjectRequest refreshTokenRequest = new JsonObjectRequest(Request.Method.POST, getString(R.string.URLLogin), jsonBody, response -> {
            try {
                String token = response.getString("token");
                dbHandler.updateUserToken(dbHandler.getUser().getId(), token);
                recreate();
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