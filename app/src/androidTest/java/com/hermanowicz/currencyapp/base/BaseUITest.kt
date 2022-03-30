package com.hermanowicz.currencyapp.base

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import java.io.BufferedReader
import java.io.Reader

abstract class BaseUITest : KoinTest {

    private lateinit var mockServer: MockWebServer

    private var mShouldStart = false

    @Before
    open fun setUp(){
        startMockServer(true)
    }

    fun mockNetworkResponseWithFileContent(fileName: String, responseCode: Int) = mockServer.enqueue(

        MockResponse()
            .setResponseCode(responseCode)
            .setBody(getJson(fileName)))

    fun getJson(path : String) : String {
        var content = ""
            val testContext = InstrumentationRegistry.getInstrumentation().context
            val inputStream = testContext.assets.open(path)
            val reader = BufferedReader(inputStream.reader() as Reader?)
            reader.use { reader ->
                content = reader.readText()
            }
        return content
    }

    private fun startMockServer(shouldStart:Boolean){
        if (shouldStart){
            mShouldStart = shouldStart
            mockServer = MockWebServer()
            mockServer.start()
        }
    }

    fun getMockWebServerUrl() = mockServer.url("/").toString()

    private fun stopMockServer() {
        if (mShouldStart){
            mockServer.shutdown()
        }
    }

    @After
    open fun tearDown(){
        stopMockServer()
        stopKoin()
    }
}