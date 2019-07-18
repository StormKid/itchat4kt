package com.stormkid.itchat4ktx.core

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder
import com.stormkid.itchat4ktx.Config
import com.stormkid.itchat4ktx.InitInfo
import com.stormkid.itchat4ktx.PushResult
import com.stormkid.itchat4ktx.User
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.itchat4ktx.util.Utils
import com.stormkid.libs.dimen.DimenUtils
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.rule.TestCallbackRule
import com.stormkid.okhttpkt.utils.GsonFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.litepal.LitePal
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
        val wxin = Config.instance.baseInfoData.wxuin
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
        Okkt.instance.TestBuilder().setUrl(url).testGet(object :TestCallbackRule{
            override suspend fun onErr(err: String) {
                Utils.showToast(context,err)
            }

            override suspend fun onResponse(response: TestCallbackRule.Response) {
                callback.invoke(response.body)
            }

        })


    }


    /**
     * 获取微信服务数据
     */
    fun webInit() {
        val url = Config.instance.loginConfigData.wxUrl + UrlConstants.INIT_URL
        val time = -(System.currentTimeMillis() / 1579).toInt()
        val params = hashMapOf(
            "r" to "$time",
            "pass_ticket" to Config.instance.baseInfoData.pass_ticket
        )
        val body = hashMapOf("BaseRequest" to Config.instance.baseRequest)
        val json = GsonFactory.toJson(body)
        Okkt.instance.Builder().setFullUrl(url).setParams(params).postJson(json, object : CallbackRule<InitInfo> {
            override suspend fun onFailed(error: String) {
                Utils.showToast(context, ConfigConstants.ERR)
            }

            override suspend fun onSuccess(entity: InitInfo, flag: String) {
                if (entity.BaseResponse.Ret == 0){
                       val user = LitePal.findFirst(User::class.java)
                       user?.delete()
                       entity.User.save()

                }else{
                    Utils.showToast(context, "您的用户信息受限，请用其他账号登录此应用")
                }
            }

        })

    }





    private fun getResult(inputstream: InputStream) = String(inputstream.readBytes())

}