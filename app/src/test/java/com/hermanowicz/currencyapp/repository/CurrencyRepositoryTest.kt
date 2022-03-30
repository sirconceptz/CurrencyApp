package com.hermanowicz.currencyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hermanowicz.currencyapp.api.CurrencyApi
import com.hermanowicz.currencyapp.base.BaseUTTest
import com.hermanowicz.currencyapp.data.repository.CurrencyRatesRepository
import com.hermanowicz.currencyapp.di.configureTestAppComponent
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.koin.core.context.startKoin
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(JUnit4::class)
class CurrencyRepositoryTest : BaseUTTest(){

    private lateinit var repository: CurrencyRatesRepository

    val apiService : CurrencyApi by inject()
    val mockWebServer : MockWebServer by inject()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun start(){
        super.setUp()
        startKoin{modules(configureTestAppComponent(getMockWebServerUrl()))}
    }

    @Test
    fun test_currency_repo_retrieves_expected_data() =  runBlocking {
        mockNetworkResponseWithFileContent("latest.json", HttpURLConnection.HTTP_OK)
        repository = CurrencyRatesRepository()

        val dataReceived = repository.getCurrencyRates()

        val expectedCurrencyBase = "USD"
        assertNotNull(dataReceived)
        assertEquals(expectedCurrencyBase, dataReceived.base)
    }
}