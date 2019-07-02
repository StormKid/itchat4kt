package com.stormkid.itchat4ktx.core

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.libs.dimen.DimenUtils
import com.stormkid.okhttpkt.cache.CookieManager
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
对外处理登录等事项
@author ke_li
@date 2019/6/24
 */
class LoginWorker(private val context: Context) {

    fun checkLogin(callback: (String) -> Unit) {
        val uuid = PublicSharePreference.getString(context, ConfigConstants.UUID_KEY)
        if (TextUtils.isEmpty(uuid)) return
        val localTime = System.currentTimeMillis()
        val r = (-localTime) / 1579
        val params = hashMapOf(
            "tip" to "1",
            "uuid" to uuid!!,
            "r" to "$r",
            "_" to "$localTime"
        )
        Okkt.instance.Builder().setUrl(UrlConstants.LOGIN_URL).setParams(params)
            .getString(object : CallbackRule<String> {
                override suspend fun onFailed(error: String) {
                }

                override suspend fun onSuccess(entity: String, flag: String) {
                    val code = entity.split(";")[0].split("=")[1]
                    callback.invoke(code)
                }

            })
    }

    fun getQrCode(callback: (Bitmap?) -> Unit) {
        val path = "https://login.weixin.qq.com/l/"
        val uuid = PublicSharePreference.getString(context, ConfigConstants.UUID_KEY)
        var bitmap: Bitmap? = null
        runBlocking {
            launch(Dispatchers.Unconfined) {
                bitmap = QRCodeEncoder.syncEncodeQRCode(path + uuid, DimenUtils.dip2px(context,200f))
                callback.invoke(bitmap)
            }
        }
    }


    fun pushLogin(){
        val cookies = CookieManager.instance.getCookies()

    }

}