package com.stormkid.itchat4ktx.core

import android.content.Context
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.itchat4ktx.util.Utils
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.StringCallback

/**
基本初始化请求
@author ke_li
@date 2019/6/27
 */
class ConfigWorker(private val context: Context){

    private val API_KEY = "wx782c26e4c19acffb"
    private val FUNC = "new"

    fun getUUid(callback:(String)->Unit){
        Okkt.instance.Builder().setUrl(UrlConstants.UUID_URL).setParams(hashMapOf(
            "appid" to API_KEY,
            "fun" to FUNC
        )).getString(object : StringCallback{
            override suspend fun onFailed(error: String) {
            }

            override suspend fun onSuccess(entity: String, flag: String) {
                try {
                    val key = entity.split(";")[1]
                    val uuid = key.split("uuid")[1].split("\"")[1]
                    PublicSharePreference.putString(context,ConfigConstants.UUID_KEY,uuid)
                    callback.invoke(uuid)
                }catch (e:Exception){
                    Utils.showToast(context,ConfigConstants.ERR)
                }

            }

        })
    }


}