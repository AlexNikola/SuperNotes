package com.alexnikola.fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

public class FingerPrintDialog extends DialogFragment
        implements TextView.OnEditorActionListener, UiHelper.Callback {

    private static final String ARG_PASSWORD = "ARG_PASSWORD";
    private static final String ARG_WITH_FINGER = "ARG_WITH_FINGER";
    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_FINGER_MESSAGE = "ARG_FINGER_MESSAGE";
    private static final String ARG_PASSWORD_MESSAGE = "ARG_PASSWORD_MESSAGE";

    @Nullable
    private String password;

    private boolean withFinger = true;

    @Nullable
    private String title;

    @Nullable
    private String fingerMessage;

    @Nullable
    private String passwordMessage;

    private Button cancelButton;
    private Button secondDialogButton;
    private View fingerprintContent;
    private View backupContent;
    private EditText passwordEditText;
    private TextView passwordDescriptionTextView;
    private TextView newFingerprintEnrolledTextView;

    private Stage stage = Stage.FINGERPRINT;

    private FingerprintManager.CryptoObject cryptoObject;
    private UiHelper uiHelper;

    private InputMethodManager inputMethodManager;

    @Nullable
    private Callback callback;

    public static FingerPrintDialog newInstance(@Nullable String password,
                                                boolean withFinger,
                                                @Nullable String title,
                                                @Nullable String fingerMessage,
                                                @Nullable String passwordMessage) {

        FingerPrintDialog dialog = new FingerPrintDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PASSWORD, password);
        args.putBoolean(ARG_WITH_FINGER, withFinger);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_FINGER_MESSAGE, fingerMessage);
        args.putString(ARG_PASSWORD_MESSAGE, passwordMessage);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Callback) {
            callback = (Callback) context;
        } else if (getParentFragment() instanceof Callback) {
            callback = (Callback) getParentFragment();
        }

        inputMethodManager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        if (getArguments() != null) {
            password = getArguments().getString(ARG_PASSWORD);
            withFinger = getArguments().getBoolean(ARG_WITH_FINGER);
            title = getArguments().getString(ARG_TITLE);
            fingerMessage = getArguments().getString(ARG_FINGER_MESSAGE);
            passwordMessage = getArguments().getString(ARG_PASSWORD_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (title != null) {
            getDialog().setTitle(title);
        }

        View view = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);

        FingerprintManager fingerprintManager = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            fingerprintManager = getActivity().getSystemService(FingerprintManager.class);
        }

        uiHelper = new UiHelper(
                fingerprintManager,
                (ImageView) view.findViewById(R.id.fingerprintIcon),
                (TextView) view.findViewById(R.id.fingerprintStatus), this);

        view.findViewById(R.id.fingerprintIcon);
                view.findViewById(R.id.fingerprintStatus);

        cancelButton = view.findViewById(R.id.cancelButton);
        secondDialogButton = view.findViewById(R.id.secondDialogButton);

        fingerprintContent = view.findViewById(R.id.fingerprintContainer);
        backupContent = view.findViewById(R.id.backupContainer);

        passwordEditText = view.findViewById(R.id.passwordEditText);
        passwordDescriptionTextView = view.findViewById(R.id.passwordDescriptionTextView);

        newFingerprintEnrolledTextView = view.findViewById(R.id.newFingerprintEnrolledTextView);




        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        secondDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stage == Stage.FINGERPRINT) {
                    goToBackup();
                } else {
                    verifyPassword();
                }
            }
        });


        passwordEditText.setOnEditorActionListener(this);

        updateStage();

        // If fingerprint authentication is not available, switch immediately to the backup
        // (password) screen.
        if (!uiHelper.isFingerprintAuthAvailable()) {
            goToBackup();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stage == Stage.FINGERPRINT) {
            uiHelper.startListening(cryptoObject);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.stopListening();
    }



    /**
     * Sets the crypto object to be passed in when authenticating with fingerprint.
     */
    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        this.cryptoObject = cryptoObject;

    }

    /**
     * Switches to backup (password) screen. This either can happen when fingerprint is not
     * available or the user chooses to use the password authentication method by pressing the
     * button. This can also happen when the user had too many fingerprint attempts.
     */
    private void goToBackup() {
        stage = Stage.PASSWORD;
        updateStage();
        passwordEditText.requestFocus();

        // Show the keyboard.
        passwordEditText.postDelayed(mShowKeyboardRunnable, 500);

        // Fingerprint is not used anymore. Stop listening for it.
        uiHelper.stopListening();
    }

    /**
     * Checks whether the current entered password is correct, and dismisses the the dialog and
     * let's the activity know about the result.
     */
    private void verifyPassword() {
        if (!checkPassword(passwordEditText.getText().toString())) {
            return;
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
        dismiss();
    }

    /**
     * @return true if {@code password} is correct, false otherwise
     */
    private boolean checkPassword(String password) {
        // Assume the password is always correct.
        // In the real world situation, the password needs to be verified in the server side.
        return password.length() > 0;
    }

    private final Runnable mShowKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            inputMethodManager.showSoftInput(passwordEditText, 0);
        }
    };

    private void updateStage() {
        switch (stage) {
            case FINGERPRINT:
                cancelButton.setText(R.string.cancel);
                secondDialogButton.setText(R.string.use_password);
                fingerprintContent.setVisibility(View.VISIBLE);
                backupContent.setVisibility(View.GONE);
                break;
            case NEW_FINGERPRINT_ENROLLED:
                // Intentional fall through
            case PASSWORD:
                cancelButton.setText(R.string.cancel);
                secondDialogButton.setText(R.string.ok);
                fingerprintContent.setVisibility(View.GONE);
                backupContent.setVisibility(View.VISIBLE);
                if (stage == Stage.NEW_FINGERPRINT_ENROLLED) {
                    passwordDescriptionTextView.setVisibility(View.GONE);
                    newFingerprintEnrolledTextView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword();
            return true;
        }
        return false;
    }

    @Override
    public void onAuthenticated() {
        // Callback from UiHelper. Let the activity know that authentication was
        // successful.
        //mActivity.onPurchased(true /* withFingerprint */, cryptoObject);
        dismiss();
    }

    @Override
    public void onError() {
        goToBackup();
    }



    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    public enum Stage {
        FINGERPRINT,
        NEW_FINGERPRINT_ENROLLED,
        PASSWORD
    }

    public interface Callback {

    }
}