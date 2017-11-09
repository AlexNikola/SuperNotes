package com.alexnikola.supernotes.di.component

import com.alexnikola.supernotes.MyApplication
import com.alexnikola.supernotes.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(application: MyApplication)
}