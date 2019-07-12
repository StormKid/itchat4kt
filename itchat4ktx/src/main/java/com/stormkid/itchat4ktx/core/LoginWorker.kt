package com.stormkid.itchat4ktx.core

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.itchat4ktx.util.Utils
import com.stormkid.libs.dimen.DimenUtils
import com.stormkid.okhttpkt.cache.CookieManager
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.StringCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.DefaultRedirectHandler
import org.apache.http.protocol.HttpContext
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
        val r = (-localTime) / 1.579
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
                        else Utils.showToast(context, "请重新刷新并扫码登录")
                    }catch (e:Exception){
                         Utils.showToast(context,ConfigConstants.ERR)
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
     * 虚拟登录，需要轮询登录状态
     */
    fun pushLogin(callback: (String, Boolean) -> Unit) {
        val cookie = CookieManager.instance.getCookieValue("wxuin")
        if (!TextUtils.isEmpty(cookie)) {
            val url = UrlConstants.WEB_WX_PUSH_LOGIN
            Okkt.instance.Builder().setUrl(url).setParams(hashMapOf("uin" to cookie))
                .getString(object : StringCallback {
                    override suspend fun onFailed(error: String) {
                        callback.invoke(error, false)
                    }

                    override suspend fun onSuccess(entity: String, flag: String) {
                        //TODO 这里返回UUID
                        callback.invoke(entity, true)
                    }

                })

        }

    }

    fun toLoginIn(url:String,callback: (String) -> Unit){
        val get = HttpGet(url)
        get.addHeader("User-Agent",ConfigConstants.USER_AGENT)
        val client = DefaultHttpClient()
        client.redirectHandler = RedirectHander()
        runBlocking{
            launch(Dispatchers.IO) {
                try {
                    val response = client.execute(get)
                    val input = response.entity.content
                    callback.invoke(getResult(input))
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
         }
    }




    private fun getResult(inputstream:InputStream) = String(inputstream.readBytes())

    inner class RedirectHander : DefaultRedirectHandler(){
        override fun isRedirectRequested(p0: HttpResponse?, p1: HttpContext?): Boolean {
            return false
        }

    }
}