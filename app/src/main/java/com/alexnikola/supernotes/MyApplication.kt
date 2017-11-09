package com.alexnikola.supernotes

import android.app.Application
import com.alexnikola.supernotes.di.component.AppComponent
import com.alexnikola.supernotes.di.component.DaggerAppComponent
import com.alexnikola.supernotes.di.module.AppModule

class MyApplication: Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
}