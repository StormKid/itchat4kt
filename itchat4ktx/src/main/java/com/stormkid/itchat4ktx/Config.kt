package com.stormkid.itchat4ktx

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.core.ConfigWorker
import com.stormkid.itchat4ktx.core.FriendWorker
import com.stormkid.itchat4ktx.core.LoginWorker
import com.stormkid.itchat4ktx.core.RoomWorker
import com.stormkid.itchat4ktx.util.Log
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.StringCallback
import org.litepal.LitePal

/**
更新下载请求
@author ke_li
@date 2019/6/25
 */
class Config private constructor() {

    /**
     * 初始化各类对象
     */
    private var configWorker: ConfigWorker? = null
    private var loginWorker: LoginWorker? = null
    private var friendWorker: FriendWorker? = null
    private var roomWorker: RoomWorker? = null
    private var isAlive = false
    private var isLogin = false

    companion object {
        val instance by lazy { Config() }
    }

    inner class Init(private val context: Context) {
        init {
            configWorker = ConfigWorker(context)
            loginWorker = LoginWorker(context)
            friendWorker = FriendWorker()
            roomWorker = RoomWorker()
        }

        fun runInit() {
            Okkt.instance.setBase(UrlConstants.BASE_URL)
                .setNetClientType(Okkt.HTTPS_TYPE)
                .initHead(hashMapOf("User-Agent" to ConfigConstants.USER_AGENT))
                .isNeedCookie(true)
                .isLogShow(true).initHttpClient()
            LitePal.initialize(context)
        }
    }

    fun showQr(callback:(Bitmap)->Unit){
        configWorker?.getUUid {
            loginWorker?.getQrCode {
                    callback.invoke(it!!)
            }
        }
    }




    fun login(){
        Handler().postDelayed({loginWorker!!.checkLogin {
            val url = it.split(";")[1].split("redirect_uri")[1].split("\"")[1]
            glayUrl(url)
        }},15000)
    }

    fun close(context: Context){
        PublicSharePreference.removeAll(context)
    }


    private fun glayUrl(url:String){
        Okkt.instance.Builder().setFullUrl(url).getString(object :StringCallback {
            override suspend fun onFailed(error: String) {
            }

            override suspend fun onSuccess(entity: String, flag: String) {
                Log.w(entity)
            }

        })
    }



}