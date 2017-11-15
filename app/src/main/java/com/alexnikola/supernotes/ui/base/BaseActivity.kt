package com.alexnikola.supernotes.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

import com.alexnikola.supernotes.utils.KeyboardUtils
import com.alexnikola.supernotes.utils.LoadingUiHelper
import com.alexnikola.supernotes.utils.NetworkUtils

abstract class BaseActivity : AppCompatActivity(), BaseMvpView {

    private var toast: Toast? = null

    var progressDialog: LoadingUiHelper.ProgressDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = supportFragmentManager.findFragmentByTag(LoadingUiHelper.ProgressDialogFragment.TAG) as LoadingUiHelper.ProgressDialogFragment?
    }





    
    /*BaseMvpView contract*/
    override fun showLoading() {
        if(progressDialog == null) {
            progressDialog = LoadingUiHelper.showProgress(supportFragmentManager)
        }
    }

    override fun hideLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun showError(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun showError(resId: Int) {
        showError(getString(resId))
    }

    override fun showMessage(message: String) {
        toast?.cancel()
        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    override fun showMessage(resId: Int) {
        showMessage(getString(resId))
    }

    override fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(applicationContext)
    }

    override fun hideKeyboard() {
        KeyboardUtils.hideSoftInput(this)
    }
    /*BaseMvpView contract*/
}
