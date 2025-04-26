package ir.ashilethegreat.payesh;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class FingerPrintDetector extends FingerprintManager.AuthenticationCallback {

    private final Context context;
    final TextView textView;
    final ImageView imageView;
    final CallActivity callActivity;
    final ProgressBar loginProgressBar;
    final ExtendedFloatingActionButton cancel;


    public FingerPrintDetector(Context context, TextView textView, ImageView imageView, ProgressBar loginProgressBar, ExtendedFloatingActionButton cancel, CallActivity callActivity) {
        this.context = context;
        this.textView = textView;
        this.imageView = imageView;
        this.callActivity = callActivity;
        this.loginProgressBar = loginProgressBar;
        this.cancel = cancel;
    }

    public void startAuth(FingerprintManager manager , FingerprintManager.CryptoObject cryptoObject){

        CancellationSignal cancellationSignal = new CancellationSignal();

        if(ActivityCompat.checkSelfPermission(context , Manifest.permission.USE_FINGERPRINT) == PackageManager.PERMISSION_GRANTED){
            manager.authenticate(cryptoObject, cancellationSignal, 0 , this , null);
        }



    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        imageView.setColorFilter(ContextCompat.getColor(context , R.color.blue_200));
        textView.setText("اثر انگشت شناسایی شد");
        loginProgressBar.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.INVISIBLE);

        new Handler().postDelayed(() -> callActivity.setResult(), 1000);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        imageView.setColorFilter(ContextCompat.getColor(context , R.color.red));
        textView.setText("دقایقی صبر کرده سپس دوباره تلاش کنید.");
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        imageView.setColorFilter(ContextCompat.getColor(context , R.color.red));
        textView.setText("تطابق ندارد.");
        new Handler().postDelayed(() -> {
            textView.setText("انگشت خود را روی حسگر قرار دهید.");
            imageView.setColorFilter(ContextCompat.getColor(context , R.color.gray));
        }, 1000);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        imageView.setColorFilter(ContextCompat.getColor(context , R.color.red));
        textView.setText("انگشت خود را به درستی روی حسگر قرار دهید.");
        new Handler().postDelayed(() -> {
            textView.setText("انگشت خود را روی حسگر قرار دهید.");
            imageView.setColorFilter(ContextCompat.getColor(context , R.color.gray));
        }, 1000);
    }

    public interface CallActivity{
        void setResult();
    }
}

