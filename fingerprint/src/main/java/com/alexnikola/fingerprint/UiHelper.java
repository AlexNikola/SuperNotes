package com.alexnikola.fingerprint;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

public class UiHelper {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    @Nullable
    private final FingerprintManager fingerprintManager;
    private final ImageView icon;
    private final TextView errorTextView;
    private final Callback callback;
    private CancellationSignal cancellationSignal;

    private boolean isSelfCancelled;

    private FingerprintManager.AuthenticationCallback authenticationCallback = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            if (!isSelfCancelled) {
                showError(errString);
                icon.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.onError();
                    }
                }, ERROR_TIMEOUT_MILLIS);
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            showError(helpString);
        }

        @Override
        public void onAuthenticationFailed() {
            showError(icon.getResources().getString(
                    R.string.fingerprint_not_recognized));
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            errorTextView.removeCallbacks(mResetErrorTextRunnable);
            icon.setImageResource(R.drawable.ic_fingerprint_success);
            errorTextView.setTextColor(
                    errorTextView.getResources().getColor(R.color.success_color, null));
            errorTextView.setText(
                    errorTextView.getResources().getString(R.string.fingerprint_success));
            icon.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onAuthenticated();
                }
            }, SUCCESS_DELAY_MILLIS);
        }
    };

    UiHelper(@Nullable FingerprintManager fingerprintManager,
             ImageView icon, TextView errorTextView, Callback callback) {
        this.fingerprintManager = fingerprintManager;
        this.icon = icon;
        this.errorTextView = errorTextView;
        this.callback = callback;

        icon.getBackground();
        icon.getDrawable();
    }

    boolean isFingerprintAuthAvailable() {
        // The line below prevents the false positive inspection from Android Studio
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && fingerprintManager != null
                && fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
    }

    void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        cancellationSignal = new CancellationSignal();
        isSelfCancelled = false;
        // The line below prevents the false positive inspection from Android Studio
        // noinspection ResourceType
        fingerprintManager
                .authenticate(cryptoObject, cancellationSignal, 0 /* flags */, authenticationCallback, null);
        icon.setImageResource(R.drawable.ic_fingerprint_black_24dp);
    }

    void stopListening() {
        if (cancellationSignal != null) {
            isSelfCancelled = true;
            cancellationSignal.cancel();
            cancellationSignal = null;
        }
    }



    private void showError(CharSequence error) {
        icon.setImageResource(R.drawable.ic_fingerprint_error);
        errorTextView.setText(error);
        errorTextView.setTextColor(
                errorTextView.getResources().getColor(R.color.warning_color, null));
        errorTextView.removeCallbacks(mResetErrorTextRunnable);
        errorTextView.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS);
    }

    private Runnable mResetErrorTextRunnable = new Runnable() {
        @Override
        public void run() {
            errorTextView.setTextColor(
                    errorTextView.getResources().getColor(R.color.hint_color, null));
            errorTextView.setText(
                    errorTextView.getResources().getString(R.string.fingerprint_hint));
            icon.setImageResource(R.drawable.ic_fingerprint_black_24dp);
        }
    };

    public interface Callback {

        void onAuthenticated();

        void onError();
    }
}