package com.stormkid.itchat4ktx.util

import android.os.Handler
import android.os.Message


/**

@author ke_li
@date 2019/5/16
 */
class TimeTaskDo private constructor() {


    companion object {
        /**
         * 倒计时或正计时所计算的时间
         */
        private var countTime = 60
        /**
         * 延迟触发所用的时间
         */
        private var delay = 1000L
        private var handler = LimitHandler()
        /**
         * 倒数
         */
        private const val FORBACK_COUNT = 100865
        /**
         * 正数
         */
        private const val UPDATE_COUNT = 100866
        /**
         * 暂停
         */
        private const val PAUSE_COUNT = 100867
        /**
         * 完成
         */
        private const val FINISH_COUNT = 100000
        /**
         * 间隔触发
         */
        private const val AT_COUNT = 100868
        val instance by lazy { TimeTaskDo() }
    }

    fun setRecall(boolean: Boolean){
        handler.isReCall = boolean
    }

    fun startForBack(finish: () -> Unit, tink: (Int) -> Unit) {
        reInit()
        handler.initCallback(finish, tink)
        handler.sendEmptyMessage(FORBACK_COUNT)
    }

    fun startUpDate(finish: () -> Unit, tink: (Int) -> Unit) {
        reInit()
        handler.initCallback(finish, tink)
        handler.sendEmptyMessage(UPDATE_COUNT)
    }

    fun startAtTime(finish: () -> Unit, tink: (Int) -> Unit){
        reInit()
        handler.initCallback(finish, tink)
        handler.sendEmptyMessage(AT_COUNT)
    }

    private fun reInit() {
        if (handler.hasMessages(UPDATE_COUNT) || handler.hasMessages(FORBACK_COUNT)) return
        else handler = LimitHandler()
    }


    fun finish() {
        handler.reSetDone()
    }

    fun pause() {
        handler.sendEmptyMessage(PAUSE_COUNT)
    }

    fun initDelay(time: Long): TimeTaskDo {
        delay = time
        return this
    }


    fun initTime(time: Int): TimeTaskDo {
        countTime = time / 1000
        return this
    }

    private class LimitHandler : Handler() {

        private lateinit var finish: () -> Unit
        private lateinit var tink: (Int) -> Unit
        private var tempCount = 0
        var isReCall = false
        fun initCallback(finish: () -> Unit, tink: (Int) -> Unit) {
            this.finish = finish
            this.tink = tink
        }

        override fun handleMessage(msg: Message?) {
            when (msg?.what) {
                FORBACK_COUNT -> {
                    tink.invoke(countTime)
                    countTime -= 1
                    if (countTime == 0) sendEmptyMessageDelayed(FINISH_COUNT, delay)
                    else sendEmptyMessageDelayed(FORBACK_COUNT, delay)
                }
                UPDATE_COUNT -> {
                    tink.invoke(countTime)
                    countTime += 1
                    sendEmptyMessageDelayed(UPDATE_COUNT, delay)
                }
                AT_COUNT -> {
                    if (tempCount == 0){
                        tempCount = (delay/1000 +2).toInt()*3
                        tink.invoke(countTime)
                    }else {
                        tink.invoke(-100)
                    }
                    tempCount -= 1
                    countTime -= 1
                    if (countTime == 0) sendEmptyMessageDelayed(FINISH_COUNT, delay)
                    else sendEmptyMessageDelayed(AT_COUNT, delay)
                }
                PAUSE_COUNT -> {
                    removeCallbacksAndMessages(null)
                }
                FINISH_COUNT -> {
                    reSetDone()
                    finish.invoke()
                }
            }
        }

        fun reSetDone() {
            delay = 1000L
            countTime = 60
            removeCallbacksAndMessages(null)
        }
    }


}