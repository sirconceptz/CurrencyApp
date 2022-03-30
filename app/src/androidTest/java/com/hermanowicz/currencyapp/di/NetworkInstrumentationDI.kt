package com.hermanowicz.currencyapp.di

import com.google.gson.GsonBuilder
import com.hermanowicz.currencyapp.api.CurrencyApi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun configureNetworkForInstrumentationTest(baseTestApi: String) = module {

    single (override = true){
        Retrofit.Builder()
            .baseUrl(baseTestApi)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
    factory{ get<Retrofit>().create(CurrencyApi::class.java) }
}