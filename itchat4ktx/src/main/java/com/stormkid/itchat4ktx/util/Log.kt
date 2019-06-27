package com.stormkid.itchat4ktx.util

import android.util.Log

/**

@author ke_li
@date 2019/6/14
 */
object Log {
    private const val APP_LOGGER = "APP_LOGGER"
    private var isEnable = true

    fun init(isEnable: Boolean){
        this.isEnable = isEnable
    }

    fun w(msg: Any) {
        if (isEnable)
            Log.w(APP_LOGGER, msg.toString())
    }

    fun d(msg: Any) {
        if (isEnable)
            Log.w(APP_LOGGER, msg.toString())
    }

    fun e(msg: Any) {
        if (isEnable)
            Log.w(APP_LOGGER, msg.toString())
    }

    fun i(msg: Any) {
        if (isEnable)
            Log.w(APP_LOGGER, msg.toString())
    }
}