package com.alexnikola.supernotes.ui.base

import android.content.Intent

interface BasePresenter<in V : BaseMvpView> {

    fun onCreate()

    fun onAttachView(mvpView: V)

    fun onResume()

    fun onPause()

    fun onDetachView()

    fun onDestroy()

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
}
