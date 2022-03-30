package com.hermanowicz.currencyapp.di

import com.hermanowicz.currencyapp.utils.AppUtils
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AppUtilDependency = module {
    factory { AppUtils(androidContext()) }
}