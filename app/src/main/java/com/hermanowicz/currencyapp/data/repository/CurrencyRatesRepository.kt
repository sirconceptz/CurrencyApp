package com.hermanowicz.currencyapp.data.repository

import com.hermanowicz.currencyapp.api.CurrencyApi
import com.hermanowicz.currencyapp.constants.Secrets
import com.hermanowicz.currencyapp.models.CurrencyRates
import org.koin.core.KoinComponent
import org.koin.core.inject

class CurrencyRatesRepository : KoinComponent {

    val apiService: CurrencyApi by inject()

    suspend fun getCurrencyRates(): CurrencyRates {

        return processDataFetchLogic()
    }

    suspend fun processDataFetchLogic(): CurrencyRates{

        for (x in 0 until 3){
            println(" Data manipulation prior to REST API request if required $x")
        }

        val dataReceived = apiService.getCurrencyRates(Secrets.API_KEY)

        for (x in 0 until 3){
            println(" Data manipulation post REST API request if required $x")
        }

        return dataReceived
    }


}