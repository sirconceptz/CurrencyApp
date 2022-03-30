package com.hermanowicz.currencyapp.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hermanowicz.currencyapp.screens.currency.CurrencyActivityViewModel
import com.hermanowicz.currencyapp.screens.currency.CurrencyUseCase
import kotlinx.coroutines.CoroutineDispatcher

class BaseViewModelFactory constructor(
    private val mainDispather: CoroutineDispatcher
    ,private val ioDispatcher: CoroutineDispatcher
    ,private val useCase: CurrencyUseCase
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CurrencyActivityViewModel::class.java)) {
            CurrencyActivityViewModel(mainDispather, ioDispatcher,useCase) as T
        } else {
            throw IllegalArgumentException("ViewModel Not configured") as Throwable
        }
    }
}