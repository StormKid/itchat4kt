package com.stormkid.itchat4ktx

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.Xml
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.core.ConfigWorker
import com.stormkid.itchat4ktx.core.FriendWorker
import com.stormkid.itchat4ktx.core.LoginWorker
import com.stormkid.itchat4ktx.core.RoomWorker
import com.stormkid.itchat4ktx.util.BaseDataHandler
import com.stormkid.itchat4ktx.util.Log
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.okhttpkt.core.Okkt
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
    /**
     * 缓存本地的login
     */
    private val loginConfigData = LoginConfigData()

    /**
     * 缓存登录核心信息
     */
     val baseInfoData = BaseInfoData()

    private val indexUrls = arrayListOf(
        "wx2.qq.com", "wx8.qq.com", "qq.com", "web2.wechat.com", "wechat.com"
    )
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

    fun showQr(callback: (Bitmap) -> Unit) {
        configWorker?.getUUid {
            loginWorker?.getQrCode {
                callback.invoke(it!!)
            }
        }
    }


    /**
     * 登录
     */
    fun login() {
        if (isLogin && isAlive) return
        Handler().postDelayed({
            loginWorker!!.checkLogin {
                val url = it.split(";")[1].split("redirect_uri")[1].split("\"")[1]
                loginConfigData.configUrl = url
                if (glayUrl())else { //虚拟地址
                    loginConfigData.fileUrl = loginConfigData.configUrl
                    loginConfigData.syncUrl = loginConfigData.configUrl
                }
                loginConfigData.deviceId ="e" +"${Math.random()}".subSequence(2,17).toString()
                loginConfigData.loginTime = System.currentTimeMillis()
            }
        }, 15000)
    }

    fun close(context: Context) {
        PublicSharePreference.removeAll(context)
    }



    private fun glayUrl(): Boolean {
        indexUrls.forEach {
            if (loginConfigData.configUrl.contains("https://$it")) {
                loginConfigData.fileUrl = "file.$it"
                loginConfigData.syncUrl = "webpush.$it"
                isLogin = true
                return@forEach
            }
        }
        return isLogin
    }


     private fun getBataConfigData(result:String){
        try {
            Xml.parse(result, BaseDataHandler(baseInfoData))
        }catch (e:Exception){
            Log.e("Your login is none data")
        }
    }

}