package com.hermanowicz.currencyapp.app

import org.koin.core.module.Module

class TestMainApp : MainApp() {
    override fun provideDependency() = listOf<Module>()
}