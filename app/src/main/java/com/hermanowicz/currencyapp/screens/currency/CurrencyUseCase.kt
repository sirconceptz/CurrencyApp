package com.hermanowicz.currencyapp.screens.currency

import com.hermanowicz.currencyapp.data.repository.CurrencyRatesRepository
import com.hermanowicz.currencyapp.models.CurrencyRates
import org.koin.core.KoinComponent
import org.koin.core.inject

class CurrencyUseCase : KoinComponent {

    val repository : CurrencyRatesRepository by inject()

    suspend fun processCurrencyUseCase() : CurrencyRates {
        for (x in 0 until 3){
            println(" Pre Data manipulation $x")
        }
        val response =  repository.getCurrencyRates()

        for (x in 0 until 3){
            println(" Post Data manipulation $x")
        }

        return response
    }
}