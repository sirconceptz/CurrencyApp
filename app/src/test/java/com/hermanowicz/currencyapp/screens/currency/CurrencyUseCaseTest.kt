package com.hermanowicz.currencyapp.screens.currency

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hermanowicz.currencyapp.api.CurrencyApi
import com.hermanowicz.currencyapp.base.BaseUTTest
import com.hermanowicz.currencyapp.data.repository.CurrencyRatesRepository
import com.hermanowicz.currencyapp.di.configureTestAppComponent
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class CurrencyUseCaseTest : BaseUTTest(){

    private lateinit var useCase: CurrencyUseCase

    val currencyRatesRepository : CurrencyRatesRepository by inject()
    val apiService : CurrencyApi by inject()
    val mockWebServer : MockWebServer by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start(){
        super.setUp()
        startKoin{ modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_currency_use_case_returns_expected_value()= runBlocking{
        mockNetworkResponseWithFileContent("latest.json", HttpURLConnection.HTTP_OK)
        useCase = CurrencyUseCase()

        val dataReceived = useCase.processCurrencyUseCase()

        val expectedCurrencyBase = "USD"
        assertNotNull(dataReceived)
        assertEquals(expectedCurrencyBase, dataReceived.base)
    }
}