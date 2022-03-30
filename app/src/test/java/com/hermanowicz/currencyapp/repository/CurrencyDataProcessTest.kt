package com.hermanowicz.currencyapp.repository

import com.hermanowicz.currencyapp.data.repository.CurrencyDataProcess
import com.hermanowicz.currencyapp.model.RatesTestModel
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class CurrencyDataProcessTest{

    val repository = CurrencyDataProcess()

    @Test
    fun test_currency_converter_gives_expected_data() {
        val expectedConvertedAmount = 20.75
        val mainCurrencyRate = 1.5
        val outputCurrencyRate = 3.3
        val amount = 45.65
        val receivedConvertedAmount = repository.getConvertedAmount(
            mainCurrencyRate,
            outputCurrencyRate,
            amount
        )
        assertEquals("Converter", expectedConvertedAmount, receivedConvertedAmount, 0.01)
    }

    @Test
    fun test_get_rate_value_gives_expected_data() {
        val testRates = RatesTestModel().rates
        val eurRate = 0.912695
        val gbpRate = 0.763327
        val plnRate = 4.303316

        assertEquals("EUR", eurRate, repository.getRateValue(testRates, "EUR"), 0.000001)
        assertEquals("GBP", gbpRate, repository.getRateValue(testRates, "GBP"), 0.000001)
        assertEquals("PLN", plnRate, repository.getRateValue(testRates, "PLN"), 0.000001)
    }
}