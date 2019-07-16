package com.stormkid.itchat4ktx.core

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.text.TextUtils
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.stormkid.itchat4ktx.Config
import com.stormkid.itchat4ktx.PushResult
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.itchat4ktx.util.Utils
import com.stormkid.libs.dimen.DimenUtils
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.utils.GsonFactory
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.DefaultRedirectHandler
import org.apache.http.protocol.HttpContext
import java.io.File
import java.io.InputStream

/**
对外处理登录等事项
@author ke_li
@date 2019/6/24
 */
class LoginWorker(private val context: Context) {


    /**
     * 查看是否登录
     */
    fun checkLogin(callback: (String) -> Unit) {
        val uuid = PublicSharePreference.getString(context, ConfigConstants.UUID_KEY)
        if (TextUtils.isEmpty(uuid)) return
        val localTime = System.currentTimeMillis()
        val r = ((-localTime) / 1579).toInt()
        val params = hashMapOf(
            "tip" to "1",
            "uuid" to uuid!!,
            "r" to "$r",
            "_" to "$localTime"
        )
        Okkt.instance.Builder().setUrl(UrlConstants.LOGIN_URL).setParams(params)
            .getString(object : StringCallback {
                override suspend fun onFailed(error: String) {
                    Utils.showToast(context, "请重新刷新并扫码登录")
                }

                override suspend fun onSuccess(entity: String, flag: String) {
                    try {
                        val code = entity.split(";")[0].split("=")[1]
                        if (code == "200") callback.invoke(entity)
                        else {
                            Config.instance.cleanLoginInfo()
                            Utils.showToast(context, "请重新刷新并扫码登录")
                        }
                    } catch (e: Exception) {
                        Utils.showToast(context, ConfigConstants.ERR)
                    }


                }

            })
    }

    /**
     * 获取登录二维码
     */
    fun getQrCode(callback: (Bitmap?) -> Unit) {
        val path = "https://login.weixin.qq.com/l/"
        val uuid = PublicSharePreference.getString(context, ConfigConstants.UUID_KEY)
        runBlocking {
            launch(Dispatchers.Unconfined) {
                val bitmap = QRCodeEncoder.syncEncodeQRCode(path + uuid, DimenUtils.dip2px(context, 200f))
                callback.invoke(bitmap)
            }
        }
    }

    /**
     * 虚拟登录，这里如果登录了就会得到登录信息来获取状态
     */
    fun pushLogin(callback: (Boolean) -> Unit) {
//        val wxin = Config.instance.baseInfoData.wxuin
        val wxin = "164274720"
        if (!TextUtils.isEmpty(wxin)) {
            val url = UrlConstants.WEB_WX_PUSH_LOGIN
            Okkt.instance.Builder().setUrl(url).setParams(hashMapOf("uin" to wxin))
                .get(object : CallbackRule<PushResult> {
                    override suspend fun onFailed(error: String) {
                        Utils.showToast(context, ConfigConstants.ERR)
                    }

                    override suspend fun onSuccess(entity: PushResult, flag: String) {
                        if (TextUtils.isEmpty(entity.uuid)) callback.invoke(false)
                        else {
                            PublicSharePreference.putString(context, ConfigConstants.UUID_KEY, entity.uuid)
                            callback.invoke(true)
                        }

                    }

                })

        }

    }

    /**
     * 直接登录获取登录具体内容
     */
    fun toLoginIn(url: String, callback: (String) -> Unit) {
        val get = HttpGet(url)
        get.addHeader("User-Agent", ConfigConstants.USER_AGENT)
        val client = DefaultHttpClient()
        client.redirectHandler = RedirectHander()
        runBlocking {
            launch(Dispatchers.IO) {
                try {
                    val response = client.execute(get)
                    val input = response.entity.content
                    callback.invoke(getResult(input))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    fun webInit() {
        val url = Config.instance.loginConfigData.wxUrl + UrlConstants.INIT_URL
        val time = -(System.currentTimeMillis() / 1579).toInt()
        val params = hashMapOf(
            "r" to "$time",
            "pass_ticket" to Config.instance.baseInfoData.pass_ticket
        )
        val body = hashMapOf("BaseRequest" to Config.instance.baseRequest)
        val json = GsonFactory.toJson(body)
        Okkt.instance.Builder().setFullUrl(url).setParams(params).postJson(json, object : CallbackRule<String> {
            override suspend fun onFailed(error: String) {
                Utils.showToast(context, ConfigConstants.ERR)
            }

            override suspend fun onSuccess(entity: String, flag: String) {
                Utils.putPermission(context, Permission.Group.STORAGE) {
                    val input = entity.toByteArray()
                    val extenalPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    if (!extenalPath.exists())extenalPath.mkdir()
                    val path = extenalPath.absolutePath+"/kk.json"
                    val file = File(path)
                    file.writeBytes(input)
                }

            }

        })

    }


    private fun getResult(inputstream: InputStream) = String(inputstream.readBytes())

    inner class RedirectHander : DefaultRedirectHandler() {
        override fun isRedirectRequested(p0: HttpResponse?, p1: HttpContext?): Boolean {
            return false
        }

    }
}