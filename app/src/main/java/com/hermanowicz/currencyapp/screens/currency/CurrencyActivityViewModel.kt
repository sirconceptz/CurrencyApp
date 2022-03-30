package com.hermanowicz.currencyapp.screens.currency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hermanowicz.currencyapp.data.repository.CurrencyDataProcess
import com.hermanowicz.currencyapp.models.Currency
import com.hermanowicz.currencyapp.models.CurrencyRates
import com.hermanowicz.currencyapp.platform.LiveDataWrapper
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

class CurrencyActivityViewModel(
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
    private val useCase: CurrencyUseCase
) : ViewModel(), KoinComponent
{
    var currencyDataProcess = CurrencyDataProcess()

    var currencyList = MutableLiveData<MutableList<Currency>>()
    var currencyCodeList = currencyDataProcess.getCurrencyCodeList()
    var mainCurrencyCode = MutableLiveData<String>()
    var outputCurrencyCode = MutableLiveData<String>()
    var outputCurrencyCodeList = MutableLiveData<List<String>>()
    var amountToConvert = MutableLiveData<String>()
    var convertedAmount = MutableLiveData<String>()

    var currencyResponse = MutableLiveData<LiveDataWrapper<CurrencyRates>>()
    val loadingLiveData = MutableLiveData<Boolean>()
    private val job = SupervisorJob()
    val mUiScope = CoroutineScope(mainDispatcher + job)
    val mIoScope = CoroutineScope(ioDispatcher + job)

    fun setMainCurrency(code: String) {
        val currencyCodeListWithoutCode = currencyDataProcess.getCurrencyCodeListWithoutCode(code)
        mainCurrencyCode.postValue(code)
        outputCurrencyCodeList.postValue(currencyCodeListWithoutCode)
    }

    fun requestCurrencyActivityData() {
        if(currencyResponse.value == null){
            mUiScope.launch {
                currencyResponse.value = LiveDataWrapper.loading()
                setLoadingVisibility(true)
                try {
                    val data = mIoScope.async {
                        return@async useCase.processCurrencyUseCase()
                    }.await()
                    try {
                        currencyResponse.value = LiveDataWrapper.success(data)
                    } catch (e: Exception) {
                    }
                    setLoadingVisibility(false)
                } catch (e: Exception) {
                    setLoadingVisibility(false)
                    currencyResponse.value = LiveDataWrapper.error(e)
                }
            }
        }
    }

    fun setCurrencyList(currency: CurrencyRates){
        val mainCurrencyRate: Double = CurrencyDataProcess().getRateValue(
            currency.rates,
            mainCurrencyCode.value.toString()
        )
        currencyList.postValue(CurrencyDataProcess().getCurrencyList(currency.rates,
            mainCurrencyCode.value.toString(), mainCurrencyRate))
    }

    fun convertMoney(){
        val currency: CurrencyRates? = currencyResponse.value?.response
        if(currency!=null) {
            val mainCurrencyRate: Double = CurrencyDataProcess().getRateValue(currency.rates,
                mainCurrencyCode.value.toString())
            val outputCurrencyRate: Double = CurrencyDataProcess().getRateValue(currency.rates,
                outputCurrencyCode.value.toString())
            var amount = 0.0
            if(!amountToConvert.value.toString().equals("")) {
                amount = amountToConvert.value?.toDouble() ?: 0.0
            }
            val converted = CurrencyDataProcess().getConvertedAmount(outputCurrencyRate, mainCurrencyRate, amount).toString()
            val fullConversion = amount.toString() + " " + mainCurrencyCode.value + " ≈ " + converted + " " + outputCurrencyCode.value.toString()
            if(converted!="0.0") {
                convertedAmount.postValue(fullConversion)
            }
            setCurrencyList(currency)
        }
    }

    private fun setLoadingVisibility(visible: Boolean) {
        loadingLiveData.postValue(visible)
    }

    override fun onCleared() {
        super.onCleared()
        this.job.cancel()
    }
}