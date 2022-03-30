package com.hermanowicz.currencyapp.di

import com.hermanowicz.currencyapp.data.repository.CurrencyRatesRepository
import org.koin.dsl.module

val RepoDependency = module {

    factory {
        CurrencyRatesRepository()
    }
}