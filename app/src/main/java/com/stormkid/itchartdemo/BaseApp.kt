package com.stormkid.itchartdemo

import android.app.Application
import com.stormkid.itchat4ktx.Config

/**

@author ke_li
@date 2019/6/27
 */
class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Config.instance.Init(this).runInit()

    }
}