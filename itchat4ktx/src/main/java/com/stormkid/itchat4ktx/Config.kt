package com.stormkid.itchat4ktx

import android.content.Context
import com.stormkid.itchat4ktx.core.ConfigWorker
import com.stormkid.itchat4ktx.core.FriendWorker
import com.stormkid.itchat4ktx.core.LoginWorker
import com.stormkid.itchat4ktx.core.RoomWorker

/**
更新下载请求
@author ke_li
@date 2019/6/25
 */
class Config private constructor() {

    /**
     * 初始化各类对象
     */
    var configWorker: ConfigWorker? = null
    var loginWorker: LoginWorker? = null
    var friendWorker: FriendWorker? = null
    var roomWorker: RoomWorker? = null


    companion object {
        val instance by lazy { Config() }
    }

    inner class Init(context: Context) {
        init {
            configWorker = ConfigWorker(context)
            loginWorker = LoginWorker(context)
            friendWorker = FriendWorker()
            roomWorker = RoomWorker()
        }

        fun runInit(){
            configWorker?.init()
        }
    }


}