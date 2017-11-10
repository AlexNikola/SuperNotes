package com.alexnikola.fingerprint

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.fingerprint_dialog_backup.*
import kotlinx.android.synthetic.main.fingerprint_dialog_backup.view.*
import kotlinx.android.synthetic.main.fingerprint_dialog_container.*
import kotlinx.android.synthetic.main.fingerprint_dialog_container.view.*
import kotlinx.android.synthetic.main.fingerprint_dialog_content.*
import kotlinx.android.synthetic.main.fingerprint_dialog_content.view.*

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerPrintDialog : DialogFragment(), TextView.OnEditorActionListener, FingerHelper.Callback {

    private var stage = Stage.FINGERPRINT

    private var password: String? = null
    private var withFinger = true
    private lateinit var title: String
    private lateinit var fingerMessage: String
    private lateinit var passwordMessage: String

    private var cryptoObject: FingerprintManager.CryptoObject? = null
    private var callback: Callback? = null

    private lateinit var fingerHelper: FingerHelper
    private lateinit var inputMethodManager: InputMethodManager

    private val mShowKeyboardRunnable = Runnable { inputMethodManager.showSoftInput(passwordEditText, 0) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Callback) {
            callback = context
        } else if (parentFragment is Callback) {
            callback = parentFragment as Callback
        }
        inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog)

        withFinger = arguments?.getBoolean(ARG_WITH_FINGER) ?: false
        password = arguments?.getString(ARG_PASSWORD)
        title = arguments?.getString(ARG_TITLE) ?: getString(R.string.title)
        fingerMessage = arguments?.getString(ARG_FINGER_MESSAGE) ?: getString(R.string.fingerprint_description)
        passwordMessage = arguments?.getString(ARG_PASSWORD_MESSAGE) ?: getString(R.string.password_description)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog.setTitle(title)

        val view = inflater.inflate(R.layout.fingerprint_dialog_container, container, false)

        fingerHelper = FingerHelper(activity, view.fingerprintIcon, view.fingerprintStatus, this)

        view.cancelButton.setOnClickListener { dismiss() }

        view.secondDialogButton.setOnClickListener {
            if (stage == Stage.FINGERPRINT) {
                goToBackup()
            } else {
                verifyPassword()
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.passwordEditText.setOnEditorActionListener(this)
        updateStage()

        // If fingerprint authentication is not available, switch immediately to the backup
        // (password) screen.
        if (!fingerHelper.isFingerprintAuthAvailable) {
            goToBackup()
        }
    }

    override fun onResume() {
        super.onResume()
        if (stage == Stage.FINGERPRINT) {
            fingerHelper.startListening(cryptoObject)
        }
    }

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    override fun onPause() {
        super.onPause()
        fingerHelper.stopListening()
    }


    /**
     * Sets the crypto object to be passed in when authenticating with fingerprint.
     */
    fun setCryptoObject(cryptoObject: FingerprintManager.CryptoObject) {
        this.cryptoObject = cryptoObject

    }

    /**
     * Switches to backup (password) screen. This either can happen when fingerprint is not
     * available or the user chooses to use the password authentication method by pressing the
     * button. This can also happen when the user had too many fingerprint attempts.
     */
    private fun goToBackup() {
        stage = Stage.PASSWORD
        updateStage()
        view!!.passwordEditText.requestFocus()

        // Show the keyboard.
        view!!.passwordEditText.postDelayed(mShowKeyboardRunnable, 500)

        // Fingerprint is not used anymore. Stop listening for it.
        fingerHelper.stopListening()
    }

    /**
     * Checks whether the current entered password is correct, and dismisses the the dialog and
     * let's the activity know about the result.
     */
    private fun verifyPassword() {
        if (!checkPassword(passwordEditText!!.text.toString())) {
            return
        }
        /*if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                    mUseFingerprintFutureCheckBox.isChecked());
            editor.apply();

            if (mUseFingerprintFutureCheckBox.isChecked()) {
                // Re-create the key so that fingerprints including new ones are validated.
                mActivity.createKey(MainActivity.DEFAULT_KEY_NAME, true);
                stage = Stage.FINGERPRINT;
            }
        }
        passwordEditText.setText("");
        mActivity.onPurchased(false  without Fingerprint , null);*/
        dismiss()
    }

    /**
     * @return true if `password` is correct, false otherwise
     */
    private fun checkPassword(password: String): Boolean {
        // Assume the password is always correct.
        // In the real world situation, the password needs to be verified in the server side.
        return password.length > 0
    }

    private fun updateStage() {
        when (stage) {
            FingerPrintDialog.Stage.FINGERPRINT -> {
                view!!.cancelButton.setText(R.string.cancel)
                view!!.secondDialogButton.setText(R.string.use_password)
                view!!.fingerprintContainer.visibility = View.VISIBLE
                view!!.backupContainer.visibility = View.GONE
            }
            FingerPrintDialog.Stage.NEW_FINGERPRINT_ENROLLED,
                // Intentional fall through
            FingerPrintDialog.Stage.PASSWORD -> {
                view!!.cancelButton.setText(R.string.cancel)
                view!!.secondDialogButton.setText(R.string.ok)
                view!!.fingerprintContainer.visibility = View.GONE
                view!!.backupContainer.visibility = View.VISIBLE
                if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
                    view!!.passwordDescriptionTextView.visibility = View.GONE
                    view!!.newFingerprintEnrolledTextView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword()
            return true
        }
        return false
    }

    override fun onAuthenticated() {
        // Callback from FingerHelper. Let the activity know that authentication was
        // successful.
        //mActivity.onPurchased(true /* withFingerprint */, cryptoObject);
        dismiss()
    }

    override fun onError() {
        goToBackup()
    }


    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    enum class Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }

    interface Callback

    companion object {

        private val ARG_PASSWORD = "ARG_PASSWORD"
        private val ARG_WITH_FINGER = "ARG_WITH_FINGER"
        private val ARG_TITLE = "ARG_TITLE"
        private val ARG_FINGER_MESSAGE = "ARG_FINGER_MESSAGE"
        private val ARG_PASSWORD_MESSAGE = "ARG_PASSWORD_MESSAGE"

        @RequiresApi(api = Build.VERSION_CODES.M)
        fun newInstance(password: String?,
                        withFinger: Boolean,
                        title: String?,
                        fingerMessage: String?,
                        passwordMessage: String?): FingerPrintDialog {

            val dialog = FingerPrintDialog()
            val args = Bundle()
            args.putString(ARG_PASSWORD, password)
            args.putBoolean(ARG_WITH_FINGER, withFinger)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_FINGER_MESSAGE, fingerMessage)
            args.putString(ARG_PASSWORD_MESSAGE, passwordMessage)
            dialog.arguments = args
            return dialog
        }
    }
}