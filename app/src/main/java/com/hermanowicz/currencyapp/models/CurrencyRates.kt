package com.hermanowicz.currencyapp.models

data class CurrencyRates(
    val base: String,
    val disclaimer: String,
    val license: String,
    val rates: Rates,
    val timestamp: Int
)