package com.alexnikola.supernotes.di.component

import com.alexnikola.supernotes.data.DataManager
import com.alexnikola.supernotes.di.module.AppModule
import com.alexnikola.supernotes.schedulers.SchedulerProvider
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {

    fun getDataManager(): DataManager

    fun getSchedulerProvider(): SchedulerProvider
}