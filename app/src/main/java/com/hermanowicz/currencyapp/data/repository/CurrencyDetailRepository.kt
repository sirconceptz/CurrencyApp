package com.hermanowicz.currencyapp.data.repository

import com.hermanowicz.currencyapp.R
import com.hermanowicz.currencyapp.app.MainApp

class CurrencyDetailRepository {

    fun getCountryNameList(): MutableList<String> {
        val resources = MainApp.instance.resources
        val countryNameList = resources.getStringArray(R.array.country_name_list).toMutableList()
        return countryNameList
    }

    fun getCurrencyCodeList(): MutableList<String> {
        val resources = MainApp.instance.resources
        val codeList = resources.getStringArray(R.array.code_list).toMutableList()
        return codeList
    }

    fun getCurrencySymbolList(): MutableList<String> {
        val resources = MainApp.instance.resources
        val symbolList = resources.getStringArray(R.array.symbol_list).toMutableList()
        return symbolList
    }
}