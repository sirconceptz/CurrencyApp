package com.hermanowicz.currencyapp.screens.currency

import android.os.SystemClock
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.hermanowicz.currencyapp.R
import com.hermanowicz.currencyapp.base.BaseUITest
import com.hermanowicz.currencyapp.di.generateTestAppComponent
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.test.inject
import java.net.HttpURLConnection

@RunWith(AndroidJUnit4ClassRunner::class)
class CurrencyActivityTest : BaseUITest() {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @JvmField
    val activityRule: IntentsTestRule<CurrencyActivity> = IntentsTestRule(CurrencyActivity::class.java, true, false)

    val useCase : CurrencyUseCase by inject()
    val mockWebServer : MockWebServer by inject()

    @Before
    fun start(){
        super.setUp()
        loadKoinModules(generateTestAppComponent(getMockWebServerUrl()).toMutableList())
        initView()
    }

    @Test
    fun test_ui_responsibility() {
        val testCurrencyNameBHD = "Bahrain (BHD)"
        val testCurrencyValueWithSymbolBHD = "0.28 د.ب"
        val testCurrencyNameCNY = "China (CNY)"
        val testCurrencyValueWithSymbolCNY = "4.78 ¥"
        val amountToConvert = 234.56
        val expectedConvertedAmount = "234.56 AUD ≈ 1121.58 CNY"

        //Set main currency
        Espresso.onView(ViewMatchers.withId(R.id.mainCurrencyCodeSpinner))
            .perform(click())
        Espresso.onView(withText("AUD")).perform(click())

        //Set output currency
        Espresso.onView(ViewMatchers.withId(R.id.outputCurrencyCodeSpinner))
            .perform(click())
        Espresso.onView(withText("CNY")).perform(click())

        //Set converted amount
        Espresso.onView(ViewMatchers.withId(R.id.amountToConvert))
            .perform(clearText(), typeText(amountToConvert.toString()))

        //Check converted amount
        Espresso.onView(ViewMatchers.withId(R.id.convertedAmount))
            .check(matches(withText(expectedConvertedAmount)))

        //Check recycler view example items
        Espresso.onView(ViewMatchers.withId(R.id.currencyListRecyclerView))
            .check(
                matches(
                    com.hermanowicz.currencyapp.recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(withText(testCurrencyNameBHD))
                    )
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.currencyListRecyclerView))
            .check(
                matches(
                    com.hermanowicz.currencyapp.recyclerItemAtPosition(
                        0,
                        ViewMatchers.hasDescendant(withText(testCurrencyValueWithSymbolBHD))
                    )
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.currencyListRecyclerView))
            .check(
                matches(
                    com.hermanowicz.currencyapp.recyclerItemAtPosition(
                        2,
                        ViewMatchers.hasDescendant(withText(testCurrencyNameCNY))
                    )
                )
            )

        Espresso.onView(ViewMatchers.withId(R.id.currencyListRecyclerView))
            .check(
                matches(
                    com.hermanowicz.currencyapp.recyclerItemAtPosition(
                        2,
                        ViewMatchers.hasDescendant(withText(testCurrencyValueWithSymbolCNY))
                    )
                )
            )
    }

    private fun initView() {
        activityRule.launchActivity(null)
        mockNetworkResponseWithFileContent("latest.json", HttpURLConnection.HTTP_OK)

        SystemClock.sleep(1000)
    }
}