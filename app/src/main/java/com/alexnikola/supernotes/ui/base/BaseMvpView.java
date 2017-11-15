package com.alexnikola.supernotes.ui.base;

import android.support.annotation.StringRes;

public interface BaseMvpView {

    void showLoading();

    void hideLoading();

    void showError(String message);

    void showError(@StringRes int resId);

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();
}
