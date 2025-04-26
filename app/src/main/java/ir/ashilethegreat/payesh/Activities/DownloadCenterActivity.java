package ir.ashilethegreat.payesh.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import ir.ashilethegreat.payesh.R;

public class DownloadCenterActivity extends AppCompatActivity {

    WebView webView;
    ImageView back;
    ExtendedFloatingActionButton payeshDownload, bazaarDownload, myketDownload;
    Dialog dialog;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_center);

        back = findViewById(R.id.back);
        webView = findViewById(R.id.web);
        myketDownload = findViewById(R.id.myketDownload);
        bazaarDownload = findViewById(R.id.bazaarDownload);
        payeshDownload = findViewById(R.id.payeshDownload);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            //Checking runtime permission for devices above Marshmallow.
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                createDownloadDialog();
            }
        });

        payeshDownload.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S_V2 &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                Dialog dialog = new Dialog(DownloadCenterActivity.this);

                dialog.setContentView(R.layout.custom_camera_permission_dialog_layout);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                ExtendedFloatingActionButton confirm = dialog.findViewById(R.id.confirm);

                confirm.setOnClickListener(v15 -> {
                    dialog.dismiss();
                    ActivityCompat.requestPermissions(DownloadCenterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                });

                dialog.show();

            } else {
                File apkFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + Environment.DIRECTORY_DOWNLOADS + "/payesh.apk");
                Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                Uri photoURI = FileProvider.getUriForFile(DownloadCenterActivity.this, DownloadCenterActivity.this.getApplicationContext().getPackageName() + ".provider", apkFile);
                install.setDataAndType(photoURI, "application/vnd.android.package-archive");

                startActivity(install);

//                webView.loadUrl("https://dls.music-fa.com/tagdl/1402/Alireza%20Talischi%20-%20Pa%20Ghadam%20(320).mp3");
//                webView.getSettings().setJavaScriptEnabled(true);
//                webView.setWebViewClient(new WebViewClient());
            }
        });

        webView.setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            createDownloadDialog();
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        overridePendingTransition(0, 0);
        finish();
    }

    public void createDownloadDialog() {
        dialog = new Dialog(this);

        dialog.setContentView(R.layout.custom_update_dialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);

        ExtendedFloatingActionButton close = dialog.findViewById(R.id.close);

        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://dls.music-fa.com/tagdl/1402/Alireza%20Talischi%20-%20Pa%20Ghadam%20(320).mp3");

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Payesh.apk");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "payesh.apk");
        request.setVisibleInDownloadsUi(true);
        request.allowScanningByMediaScanner();

        downloadmanager.enqueue(request);

        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private final BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                successfulToastMaker("دانلود فایل به اتمام رسید.", "اکنون می توانید به روزرسانی را به اتمام برسانید.");
                unregisterReceiver(this);
                dialog.dismiss();

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DownloadCenterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {

                    File apkFile = new File(getExternalFilesDir("Download") + "payesh.apk");
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Uri installUri = FileProvider.getUriForFile(DownloadCenterActivity.this, DownloadCenterActivity.this.getApplicationContext().getPackageName() + ".provider", apkFile);
                    List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(install, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        DownloadCenterActivity.this.grantUriPermission(packageName, installUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                   install.setDataAndType(installUri, "application/vnd.android.package-archive");
                    startActivity(install);
                }

            }
        }
    };

    public void successfulToastMaker(String title, String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_successful, findViewById(R.id.toast_layout_root));

        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(DownloadCenterActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void errorToastMaker(String message) {
        LayoutInflater toastInflater = getLayoutInflater();
        View layout = toastInflater.inflate(R.layout.toast_message_error, findViewById(R.id.toast_layout_root));

//        TextView titleText = layout.findViewById(R.id.toastTitle);
        TextView messageText = layout.findViewById(R.id.toastMessage);
//        titleText.setText(title);
        messageText.setText(message);
        Toast toast = new Toast(DownloadCenterActivity.this);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 100) {
                File apkFile = new File(getExternalFilesDir("Download") + "payesh.apk");
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Uri photoURI = FileProvider.getUriForFile(DownloadCenterActivity.this, DownloadCenterActivity.this.getApplicationContext().getPackageName() + ".provider", apkFile);
                install.setDataAndType(photoURI, "application/vnd.android.package-archive");
                startActivity(install);
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}