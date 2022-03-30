package com.hermanowicz.currencyapp.data.repository

import com.hermanowicz.currencyapp.models.Currency
import com.hermanowicz.currencyapp.models.Rates
import java.util.*

class CurrencyDataProcess {
    private val repository = CurrencyDetailRepository()

    fun getCountryNameList(): List<String> {
        return repository.getCountryNameList()
    }

    fun getCurrencyCodeList(): List<String> {
        return repository.getCurrencyCodeList()
    }

    fun getCurrencySymbolList(): List<String> {
        return repository.getCurrencySymbolList()
    }

    fun getCurrencyCodeListWithoutCode(symbcodel: String) : List<String> {
        val currencyCodeList = getCurrencyCodeList().toMutableList()
        currencyCodeList.remove(symbcodel)
        return currencyCodeList
    }

    fun getCurrencyList(rates: Rates, withoutCode: String, mainCurrencyRate: Double): MutableList<Currency> {
        val currencyList: MutableList<Currency> = emptyList<Currency>().toMutableList()
        val currencyCodeList = getCurrencyCodeList()
        val symbolList = getCurrencySymbolList()
        val countryNameList = getCountryNameList()
        for(i in countryNameList.indices) {
            val code = currencyCodeList[i]
            if(code != withoutCode) {
                val countryName = countryNameList[i]
                val symbol = symbolList[i]
                val rate = getConvertedAmount(getRateValue(rates, code), mainCurrencyRate, 1.0)
                val currency =
                    Currency(code, countryName, symbol, rate)
                currencyList.add(currency)
            }
        }
        currencyList.sortBy { currencyDetail -> currencyDetail.countryName }
        return currencyList
    }

    fun getRateValue(rates: Rates, code: String): Double {
        when(code) {
            "USD" -> return rates.USD
            "EUR" -> return rates.EUR
            "GBP" -> return rates.GBP
            "CHF" -> return rates.CHF
            "CAD" -> return rates.CAD
            "AED" -> return rates.AED
            "AUD" -> return rates.AUD
            "PLN" -> return rates.PLN
            "JPY" -> return rates.JPY
            "ZAR" -> return rates.ZAR
            "CNY" -> return rates.CNY
            "NZD" -> return rates.NZD
            "SEK" -> return rates.SEK
            "NOK" -> return rates.NOK
            "KWD" -> return rates.KWD
            "BHD" -> return rates.BHD
            "OMR" -> return rates.OMR
            "JOD" -> return rates.JOD
            "ILS" -> return rates.ILS
            "TRY" -> return rates.TRY
            "UAH" -> return rates.UAH
        }
        return 1.0
    }

    fun getConvertedAmount(
        mainCurrencyRate: Double,
        outputCurrencyRate: Double,
        amount: Double
    ) : Double {
        var convertedValue = mainCurrencyRate * amount / outputCurrencyRate
        convertedValue = String.format(Locale.US, "%.2f", convertedValue).toDouble()
        return convertedValue
    }
}