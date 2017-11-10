package com.alexnikola.fingerprint

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import android.support.annotation.RequiresApi
import android.widget.ImageView
import android.widget.TextView

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerHelper internal constructor(private val context: Context,
                                        private val icon: ImageView,
                                        private val errorTextView: TextView,
                                        private val callback: Callback) {

    private val fingerprintManager: FingerprintManager = context.getSystemService(FingerprintManager::class.java)
    private var isSelfCancelled: Boolean = false

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback = object : FingerprintManager.AuthenticationCallback() {
        override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
            if (!isSelfCancelled) {
                showError(errString)
                icon.postDelayed({ callback.onError() }, ERROR_TIMEOUT_MILLIS)
            }
        }

        override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
            showError(helpString)
        }

        override fun onAuthenticationFailed() {
            showError(icon.resources.getString(
                    R.string.fingerprint_not_recognized))
        }

        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult) {
            errorTextView.removeCallbacks(resetErrorTextRunnable)
            icon.setImageResource(R.drawable.ic_fingerprint_success)
            errorTextView.setTextColor(
                    errorTextView.resources.getColor(R.color.success_color, null))
            errorTextView.text = errorTextView.resources.getString(R.string.fingerprint_success)
            icon.postDelayed({ callback.onAuthenticated() }, SUCCESS_DELAY_MILLIS)
        }
    }

    // The line below prevents the false positive inspection from Android Studio
    internal val isFingerprintAuthAvailable: Boolean
        get() = fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()

    private val resetErrorTextRunnable = Runnable {
        errorTextView.setTextColor(
                errorTextView.resources.getColor(R.color.hint_color, null))
        errorTextView.text = errorTextView.resources.getString(R.string.fingerprint_hint)
        icon.setImageResource(R.drawable.ic_fingerprint_black_24dp)
    }

    internal fun startListening(cryptoObject: FingerprintManager.CryptoObject?) {
        if (!isFingerprintAuthAvailable) {
            return
        }
        cancellationSignal = CancellationSignal()
        isSelfCancelled = false

        // The line below prevents the false positive inspection from Android Studio
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, authenticationCallback, null)
        icon.setImageResource(R.drawable.ic_fingerprint_black_24dp)
    }

    internal fun stopListening() {
        if (cancellationSignal != null) {
            isSelfCancelled = true
            cancellationSignal!!.cancel()
            cancellationSignal = null
        }
    }

    private fun showError(error: CharSequence) {
        icon.setImageResource(R.drawable.ic_fingerprint_error)
        errorTextView.text = error
        errorTextView.setTextColor(errorTextView.resources.getColor(R.color.warning_color, null))
        errorTextView.removeCallbacks(resetErrorTextRunnable)
        errorTextView.postDelayed(resetErrorTextRunnable, ERROR_TIMEOUT_MILLIS)
    }

    interface Callback {
        fun onAuthenticated()
        fun onError()
    }

    companion object {
        private val ERROR_TIMEOUT_MILLIS: Long = 1600
        private val SUCCESS_DELAY_MILLIS: Long = 1300
    }
}