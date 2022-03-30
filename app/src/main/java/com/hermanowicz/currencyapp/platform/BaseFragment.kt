package com.hermanowicz.currencyapp.platform

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.hermanowicz.currencyapp.contracts.AlertCallBack
import com.hermanowicz.currencyapp.contracts.AppLogger

abstract class BaseFragment: Fragment(), AppLogger, AlertCallBack {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    abstract fun getLayoutId(): Int

    private var mCallBackAlertDialog: AlertDialog? = null


    fun showToast(msg: String) {
        requireActivity().runOnUiThread {
            val toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }
    }

    override fun negativeAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun positiveAlertCallBack() {
        mCallBackAlertDialog!!.dismiss()
    }

    override fun logD(tag: String, message: String) {
        Log.d(tag,message)
    }

    override fun logE(tag: String, message: String) {
        Log.e(tag,message)
    }

    override fun logV(tag: String, message: String) {
        Log.v(tag,message)
    }

    override fun logI(tag: String, message: String) {
        Log.i(tag,message)
    }
}