package com.alexnikola.supernotes.utils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public final class LoadingUiHelper {

    public enum Type {
        FULL_SCREEN,
        DIALOG
    }

    public static ProgressDialogFragment showProgress(FragmentManager fragmentManager) {
        return showProgress(Type.FULL_SCREEN, fragmentManager);
    }

    public static ProgressDialogFragment showProgress(Type type, FragmentManager fragmentManager) {
        ProgressDialogFragment dialogFragment = new ProgressDialogFragment();
        dialogFragment.type = type;
        dialogFragment.show(fragmentManager, ProgressDialogFragment.TAG);
        return dialogFragment;
    }

    public static class ProgressDialogFragment extends DialogFragment {

        public static final String TAG = "ProgressDialogFragment";

        private Type type;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
            setCancelable(false);
        }

        @Override
        public void onStart() {
            super.onStart();

            Window window = getDialog().getWindow();

            switch (type) {
                case DIALOG: {
                    Display display = window.getWindowManager().getDefaultDisplay();
                    Point size = new Point();
                    display.getSize(size);
                    int width = size.x;
                    int height = size.y;
                    window.setLayout((int) (width * 0.9f), ViewGroup.LayoutParams.WRAP_CONTENT);
                    break;
                }
                case FULL_SCREEN: {
                    window.setBackgroundDrawable(null);
                    break;
                }
            }
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            FrameLayout frameLayout = new FrameLayout(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.setLayoutParams(lp);

            ProgressBar progressBar = new ProgressBar(getContext());
            lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER;
            int margin = DimensUtils.dpToPx(16);
            lp.setMargins(margin, margin, margin, margin);
            progressBar.setLayoutParams(lp);

            frameLayout.addView(progressBar);

            return frameLayout;
        }

        /*@NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {


            ProgressDialog dialog = new ProgressDialog(getActivity(), getTheme());
            dialog.setTitle(getString(R.string.please_wait));
            dialog.setMessage(getString(R.string.loading));
            dialog.setIndeterminate(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            return dialog;
        }*/
    }
}