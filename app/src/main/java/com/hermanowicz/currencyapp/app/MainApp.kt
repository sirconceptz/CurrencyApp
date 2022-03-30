package com.hermanowicz.currencyapp.app

import android.app.Application
import com.hermanowicz.currencyapp.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

open class MainApp : Application() {

    companion object {
        lateinit var instance: MainApp private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initiateKoin()
    }

    private fun initiateKoin() {
        startKoin{
            androidContext(this@MainApp)
            modules(provideDependency())
        }
    }

    open fun provideDependency() = appComponent
}