package com.hermanowicz.currencyapp.app

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

open class CustomInstrumentationRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader,
                                className: String,
                                context: Context): Application {
        return super.newApplication(cl,
            TestMainApp::class.java.name,
            context)
    }
}