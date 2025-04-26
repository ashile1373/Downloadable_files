package ir.ashilethegreat.payesh.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ir.ashilethegreat.payesh.Adapters.ProfileDetailsRecyclerViewAdapter;
import ir.ashilethegreat.payesh.DBhandlers.DBHandler;
import ir.ashilethegreat.payesh.DBhandlers.UserModal;
import ir.ashilethegreat.payesh.R;

public class ProfileDetailsActivity extends AppCompatActivity {

    static final int REQUEST_CAMERA_PERMISSION = 100;

    RecyclerView profileDetailsRecyclerView;
    ArrayList<String> profileList;
    ArrayList<String> profileTitles;
    ImageView back, profilePic;
    FloatingActionButton addProfilePic;
    ExtendedFloatingActionButton editProfile;
    ProgressBar confirmEditProgressBar;

    DBHandler dbHandler;
    RequestQueue requestQueue;
    UserModal userModal;
    final int DEFAULT_TIMEOUT = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        profileDetailsRecyclerView = findViewById(R.id.profileDetailsRecyclerView);
        back = findViewById(R.id.back);
        addProfilePic = findViewById(R.id.addProfilePic);
        profilePic = findViewById(R.id.profilePic);
        editProfile = findViewById(R.id.editProfile);
        confirmEditProgressBar = findViewById(R.id.confirmEditProgressBar);

        dbHandler = new DBHandler(this);
        UserModal userModal = dbHandler.getUser();
        requestQueue = Volley.newRequestQueue(this);

        profileTitles = new ArrayList<>();
        profileTitles.add("نام کاربری");
        profileTitles.add("نام و نام خانوادگی");
        profileTitles.add("شماره تلفن");

        profileList = new ArrayList<>();
        profileList.add(userModal.getUsername());
        profileList.add(userModal.getFirstName() + " " + userModal.getLastName());
        profileList.add(userModal.getPhone());

        ProfileDetailsRecyclerViewAdapter adapter = new ProfileDetailsRecyclerViewAdapter(profileList, profileTitles);
        profileDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileDetailsRecyclerView.setAdapter(adapter);

        if (dbHandler.getUser().getPic() != null) {
            byte[] decodedString = Base64.decode(dbHandler.getUser().getPic(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profilePic.setImageBitmap(decodedByte);
        }

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileDetailsActivity.this, ProfileEditActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        back.setOnClickListener(v -> onBackPressed());

        addProfilePic.setOnClickListener(v -> {

            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.custom_choose_profile_picture);
            Objects.requireNonNull(bottomSheetDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout insertImageFromCamera = bottomSheetDialog.findViewById(R.id.insertImageFromCamera);
            LinearLayout insertImageFromGallery = bottomSheetDialog.findViewById(R.id.insertImageFromGallery);

            Objects.requireNonNull(insertImageFromCamera).setOnClickListener(view1 -> {
                ImagePicker.Companion.with(this)
                        .cameraOnly()
//                    .galleryMimeTypes(new String[] = {"image/png","image/jpeg","image/jpg"});
                        .maxResultSize(750, 1000)
                        .compress(1024)
                        .crop()
                        .start(REQUEST_CAMERA_PERMISSION);
                bottomSheetDialog.cancel();
            });

            Objects.requireNonNull(insertImageFromGallery).setOnClickListener(view1 -> {
                ImagePicker.Companion.with(this)
                        .galleryOnly()
//                    .galleryMimeTypes(new String[] = {"image/png","image/jpeg","image/jpg"});
                        .maxResultSize(750, 1000)
                        .compress(1024)
                        .crop()
                        .start(REQUEST_CAMERA_PERMISSION);
                bottomSheetDialog.cancel();
            });

            bottomSheetDialog.show();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileDetailsActivity.this, SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == REQUEST_CAMERA_PERMISSION) {
                Uri uri = data.getData();
                profilePic.setImageURI(uri);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    String firstPhotoToBase64 = Base64.encodeToString(getBitmapAsByteArray(bitmap), Base64.DEFAULT);
                    confirmEditProgressBar.setVisibility(View.VISIBLE);
                    updateProfilePic(firstPhotoToBase64);
                } catch (IOException e) {
                    errorToastMaker("خطای تصویر");
                }
            }
        }

    }

    private void updateProfilePic(String image) {
        JSONObject jsonBody = new JSONObject();
        try {
            if (image.length() > 0) {
                jsonBody.put("profile_image", "data:image/jpeg;base64," + image);
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
                            confirmEditProgressBar.setVisibility(View.GONE);
                            dbHandler.updateProfilePicture(image);
                        } else {
                            confirmEditProgressBar.setVisibility(View.INVISIBLE);
                            errorToastMaker("متأسفانه خطایی رخ داد! دوباره تلاش کنید.");
                            profilePic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile));
                        }
                    } catch (JSONException e) {
                        errorToastMaker("خطای سرویس به روز رسانی اطلاعات کاربری");
                        confirmEditProgressBar.setVisibility(View.INVISIBLE);
                        profilePic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile));

                    }
                },
                error -> {
                    errorToastMaker("متأسفانه خطایی رخ داد! ارتباط اینترنتی خود را بررسی کنید.");
                    confirmEditProgressBar.setVisibility(View.INVISIBLE);
                    profilePic.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile));
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

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);

        Toast toast = new Toast(ProfileDetailsActivity.this);
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

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        return outputStream.toByteArray();
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
