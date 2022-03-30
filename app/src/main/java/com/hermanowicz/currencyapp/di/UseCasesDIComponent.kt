package com.hermanowicz.currencyapp.di

import com.hermanowicz.currencyapp.screens.currency.CurrencyUseCase
import org.koin.dsl.module

val UseCaseDependency = module {

    factory {
        CurrencyUseCase()
    }
}