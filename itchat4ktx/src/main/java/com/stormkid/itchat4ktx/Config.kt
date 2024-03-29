package com.stormkid.itchat4ktx

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.util.Xml
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.KeyContants
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
    val loginConfigData = LoginConfigData()

    /**
     * 缓存登录核心信息
     */
    val baseInfoData = BaseInfoData()

    /**
     * 缓存登录请求核心信息
     */
    val baseRequest = BaseRequest()

    /**
     *  联系人列表，用于显示
     */
    val friends = arrayListOf<FriendData>()

    /**
     * 讨论群组，用于显示
     */
    val rooms = arrayListOf<RoomData>()

    /**
     *  系统或服务号
     */
    val services = arrayListOf<FriendData>()


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
                .setErr(KeyContants.ERR)
                .setTimeOut(ConfigConstants.TIME_OUT)
                .isAllowRedirect(false)
                .isLogShow(true).initHttpClient()
            LitePal.initialize(context)
        }
    }


    /**
     * 显示二维码
     */
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
                loginConfigData.configUrl = url.split("?")[0]
                loginConfigData.wxUrl = loginConfigData.configUrl.replace("webwxnewloginpage", "")
                if (glayUrl()) else { //虚拟地址
                    loginConfigData.fileUrl = loginConfigData.wxUrl
                    loginConfigData.syncUrl = loginConfigData.wxUrl
                }
                loginConfigData.deviceId = "e" + "${Math.random()}".subSequence(2, 17).toString()
                loginConfigData.loginTime = System.currentTimeMillis()
                Okkt.instance.setBase(loginConfigData.wxUrl)
                getBaseData(url)
            }
        }, 15000)
    }

    /**
     * 关闭处理
     */
    fun close(context: Context) {
        PublicSharePreference.removeAll(context)
        cleanLoginInfo()
    }


    private fun getBaseData(url: String) {
        loginWorker?.toLoginIn(url) {
            getBataConfigData(it)
            configWorker?.webInit {
                loginWorker?.showMobileLogin {
                    configWorker?.getContacts()
                }
            }
        }
    }

    /**
     * 开始记录登录状态
     */
    fun startRec(){
        //TODO
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


    private fun getBataConfigData(result: String) {
        try {
            Xml.parse(result, BaseDataHandler(baseInfoData))
            baseRequest.Uin = baseInfoData.wxuin
            baseRequest.Skey = baseInfoData.skey
            baseRequest.Sid = baseInfoData.wxsid
            baseRequest.DeviceID = baseInfoData.pass_ticket
        } catch (e: Exception) {
            Log.e("Your login is none data")
        }
    }


    fun cleanLoginInfo() {
        baseInfoData.pass_ticket = ""
        baseInfoData.wxsid = ""
        baseInfoData.wxuin = ""
        baseInfoData.skey = ""
        baseInfoData.synckey = ""
        loginConfigData.wxUrl = ""
        loginConfigData.syncUrl = ""
        loginConfigData.fileUrl = ""
        loginConfigData.configUrl = ""
        loginConfigData.deviceId = ""
        loginConfigData.loginTime = 0L
        baseRequest.DeviceID = ""
        baseRequest.Sid = ""
        baseRequest.Skey = ""
        baseRequest.Uin = ""
    }

}