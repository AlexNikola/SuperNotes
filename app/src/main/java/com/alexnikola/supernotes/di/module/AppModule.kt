package com.alexnikola.supernotes.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.alexnikola.supernotes.data.prefs.PrefsManager
import com.alexnikola.supernotes.data.prefs.PrefsManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun providePrefsManager(prefsManager: PrefsManagerImpl): PrefsManager = prefsManager

    @Provides
    @Singleton
    fun provideSharedPrefs() : SharedPreferences = application.getSharedPreferences("supernotes.pref", Context.MODE_PRIVATE)
}