package com.stormkid.itchat4ktx.core

import android.content.Context
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.okhttpkt.core.OkTk
import com.stormkid.okhttpkt.rule.CallbackRule

/**
基本初始化请求
@author ke_li
@date 2019/6/27
 */
class ConfigWorker(private val context: Context){

    private val API_KEY = "wx782c26e4c19acffb"
    private val FUNC = "new"

    fun init(){
        OkTk.instance.setBase(UrlConstants.BASE_URL).initHead(hashMapOf("User-Agent" to ConfigConstants.USER_AGENT)).isLogShow(true).initHttpClient()
    }

    fun getUUid(callback:(String)->Unit){
        OkTk.instance.Builder().setUrl(UrlConstants.UUID_URL).setParams(hashMapOf(
            "appid" to API_KEY,
            "fun" to FUNC
        )).getString(object : CallbackRule<String>{
            override suspend fun onFailed(error: String) {
            }

            override suspend fun onSuccess(entity: String, flag: String) {
                val key = entity.split(";")[1]
                val uuid = key.split("uuid")[1].split("\"")[1]
                PublicSharePreference.putString(context,ConfigConstants.UUID_KEY,uuid)
                callback.invoke(uuid)
            }

        })
    }


    fun webInit(){
        val r = -System.currentTimeMillis()/1579
        val body = hashMapOf(
            "r" to "$r"

        )
    }


    fun getQR(){

    }
}