package com.hermanowicz.currencyapp.screens.currency

import android.os.Bundle
import com.hermanowicz.currencyapp.R
import com.hermanowicz.currencyapp.platform.BaseActivity

class CurrencyActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        configureAndShowFragment()
    }

    private fun configureAndShowFragment() {
        val fragment = supportFragmentManager.findFragmentById(R.id.base_frame_layout) as CurrencyActivityFragment?
        if(fragment == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.base_frame_layout, CurrencyActivityFragment.getMainActivityFragment())
                .commit()
        }
    }
}