package com.alexnikola.supernotes.ui.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.view.View

import com.alexnikola.supernotes.MyApplication
import com.alexnikola.supernotes.di.component.DaggerFragmentComponent
import com.alexnikola.supernotes.di.component.FragmentComponent
import com.alexnikola.supernotes.di.module.FragmentModule

abstract class BaseFragment : Fragment(), BaseMvpView {

    lateinit var baseActivity: BaseActivity

    lateinit var fragmentComponent: FragmentComponent

    private var presenter: BasePresenter<*>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseActivity = context as BaseActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        fragmentComponent = DaggerFragmentComponent.builder()
                .fragmentModule(FragmentModule(this))
                .appComponent(MyApplication.appComponent)
                .build()
    }

    protected fun partialSyncPresenterLifeCycle(presenter: BasePresenter<*>) {
        this.presenter = presenter
        this.presenter?.onCreate()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.isClickable = true
        view.setBackgroundColor(Color.WHITE)
    }

    override fun onPause() {
        super.onPause()
        presenter?.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter?.onResume()
    }

    override fun onDestroyView() {
        presenter?.onDetachView()
        super.onDestroyView()
    }

    override fun onDestroy() {
        presenter?.onDestroy()
        super.onDestroy()
    }






    /*BaseMvpView contract*/
    override fun showLoading() {
        baseActivity.showLoading()
    }

    override fun hideLoading() {
        baseActivity.hideLoading()
    }

    override fun showError(message: String) {
        baseActivity.showError(message)
    }

    override fun showError(resId: Int) {
        baseActivity.showError(resId)
    }

    override fun showMessage(message: String) {
        baseActivity.showMessage(message)
    }

    override fun showMessage(resId: Int) {
        baseActivity.showMessage(resId)
    }

    override fun isNetworkConnected(): Boolean {
        return baseActivity.isNetworkConnected
    }

    override fun hideKeyboard() {
        baseActivity.hideKeyboard()
    }
    /*BaseMvpView contract*/
}
