package com.hermanowicz.currencyapp.screens.currency

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hermanowicz.currencyapp.R
import com.hermanowicz.currencyapp.models.Currency
import com.hermanowicz.currencyapp.models.CurrencyRates
import com.hermanowicz.currencyapp.platform.BaseFragment
import com.hermanowicz.currencyapp.platform.BaseViewModelFactory
import com.hermanowicz.currencyapp.platform.LiveDataWrapper
import kotlinx.android.synthetic.main.fragment_main_activity.*
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.android.inject

class CurrencyActivityFragment : BaseFragment() {

    companion object{
        fun getMainActivityFragment() = CurrencyActivityFragment()
    }

    val useCase : CurrencyUseCase by inject()
    private val baseViewModelFactory : BaseViewModelFactory =
        BaseViewModelFactory(Dispatchers.Main, Dispatchers.IO,useCase)
    private val TAG = CurrencyActivityFragment::class.java.simpleName
    lateinit var recyclerViewAdapter: CurrencyRecyclerViewAdapter

    private val viewModel : CurrencyActivityViewModel by lazy {
        ViewModelProviders.of(this,baseViewModelFactory)
            .get(CurrencyActivityViewModel::class.java)    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        setDataLiveObservers()

        setListeners()

        this.viewModel.requestCurrencyActivityData()
    }

    private fun initView(){
        mainCurrencyCodeSpinner.adapter =
            context?.let { ArrayAdapter(it, R.layout.symbol_item_view, viewModel.currencyCodeList) }

        recyclerViewAdapter = CurrencyRecyclerViewAdapter(requireActivity(), arrayListOf())
        currencyListRecyclerView.adapter = recyclerViewAdapter
        currencyListRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun setDataLiveObservers(){
        viewModel.currencyResponse.observe(viewLifecycleOwner, this.dataObserver)
        viewModel.loadingLiveData.observe(viewLifecycleOwner, this.loadingObserver)
        viewModel.outputCurrencyCodeList.observe(viewLifecycleOwner, this.outputCurrencyCodeListObserver)
        viewModel.mainCurrencyCode.observe(viewLifecycleOwner, this.convertObserver)
        viewModel.outputCurrencyCode.observe(viewLifecycleOwner, this.convertObserver)
        viewModel.amountToConvert.observe(viewLifecycleOwner, this.convertObserver)
        viewModel.convertedAmount.observe(viewLifecycleOwner, this.convertedAmountObserver)
        viewModel.currencyList.observe(viewLifecycleOwner, this.mainCurrencyCodeObserver)
    }

    private fun setListeners(){
        mainCurrencyCodeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val code = mainCurrencyCodeSpinner.selectedItem.toString()
                viewModel.setMainCurrency(code)
            }
        }

        outputCurrencyCodeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val code = outputCurrencyCodeSpinner.selectedItem.toString()
                viewModel.outputCurrencyCode.postValue(code)
            }
        }

        amountToConvert.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                viewModel.amountToConvert.postValue(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private val outputCurrencyCodeListObserver = Observer<List<String>> { codeList ->
        outputCurrencyCodeSpinner.adapter =
            context?.let { ArrayAdapter(it, R.layout.symbol_item_view, codeList) }
    }

    private val dataObserver = Observer<LiveDataWrapper<CurrencyRates>> { result ->
        when (result?.responseStatus) {
            LiveDataWrapper.RESPONSESTATUS.LOADING -> {
            }
            LiveDataWrapper.RESPONSESTATUS.ERROR -> {
                logD(TAG,"LiveDataResult.Status.ERROR = ${result.response}")
                setErrorView()
                showToast(getString(R.string.error_in_retrieving_data))

            }
            LiveDataWrapper.RESPONSESTATUS.SUCCESS -> {
                logD(TAG,"LiveDataResult.Status.SUCCESS = ${result.response}")
                val mainItemReceived = result.response as CurrencyRates
                processData(mainItemReceived)
            }
        }
    }

    private val mainCurrencyCodeObserver = Observer<MutableList<Currency>> { currencyList ->
        recyclerViewAdapter.updateCurrencyList(currencyList)
        val code = viewModel.mainCurrencyCode.value
        currencyRates.setText("""${getString(R.string.currencyRates)} ($code)""")
    }

    private val convertObserver = Observer<String> { viewModel.convertMoney() }

    private val convertedAmountObserver = Observer<String> { amount ->
        if(amount!="0.0") {
            convertedAmount.setText(amount)
            convertedAmount.visibility = View.VISIBLE
        }
        else {
            convertedAmount.visibility = View.GONE
        }
    }

    private val loadingObserver = Observer<Boolean> { visible ->
        if(visible){
            progress_circular.visibility = View.VISIBLE
        }else{
            progress_circular.visibility = View.INVISIBLE
        }
    }

    private fun setErrorView(){
        error_image.visibility = View.VISIBLE
        error_statement.visibility = View.VISIBLE
        mainCurrencyText.visibility = View.GONE
        mainCurrencyCodeSpinner.visibility = View.GONE
        outputCurrencyText.visibility = View.GONE
        outputCurrencyCodeSpinner.visibility = View.GONE
        amountToConvert.visibility = View.GONE
        currencyRates.visibility = View.GONE
        currencyListRecyclerView.visibility = View.GONE
    }

    override fun getLayoutId(): Int = R.layout.fragment_main_activity

    private fun processData(currencyRates: CurrencyRates) {
        logD(TAG,"processData currencyRates = ${currencyRates}")

        val refresh = Handler(Looper.getMainLooper())
        refresh.post {
            this.viewModel.setCurrencyList(currencyRates)
        }
    }
}