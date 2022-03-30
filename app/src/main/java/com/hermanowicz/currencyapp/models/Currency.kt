package com.hermanowicz.currencyapp.models

data class Currency (
    val code: String,
    val countryName: String,
    val symbol: String,
    val rate: Double
)