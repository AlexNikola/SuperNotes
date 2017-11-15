package com.alexnikola.supernotes.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.alexnikola.supernotes.AppConstants
import com.alexnikola.supernotes.data.DataManagerImpl
import com.alexnikola.supernotes.data.DataManager
import com.alexnikola.supernotes.data.db.DbManager
import com.alexnikola.supernotes.data.db.DbManagerImpl
import com.alexnikola.supernotes.data.network.ApiManager
import com.alexnikola.supernotes.data.prefs.PrefsManager
import com.alexnikola.supernotes.data.prefs.PrefsManagerImpl
import com.alexnikola.supernotes.schedulers.AppSchedulerProvider
import com.alexnikola.supernotes.schedulers.SchedulerProvider
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideSharedPrefs() : SharedPreferences = application.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDbManager(dbManager: DbManagerImpl): DbManager = dbManager

    @Provides
    @Singleton
    internal fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Provides
    @Singleton
    internal fun provideApiManager(): ApiManager {
        val rxAdapter = RxJava2CallAdapterFactory.create()

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .build()

        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val converterFactory = GsonConverterFactory.create(gson)

        val retrofit = Retrofit.Builder()
                .baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(rxAdapter)
                .client(okHttpClient)
                .build()
        return retrofit.create(ApiManager::class.java)
    }

    @Provides
    @Singleton
    internal fun provideDataManager(dataManager: DataManagerImpl): DataManager = dataManager
}