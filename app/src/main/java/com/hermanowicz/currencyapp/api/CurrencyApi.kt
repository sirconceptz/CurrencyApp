package com.hermanowicz.currencyapp.api

import com.hermanowicz.currencyapp.models.CurrencyRates
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi{

    @GET("latest.json")
    suspend fun getCurrencyRates(@Query("app_id") apiKey: String): CurrencyRates
}