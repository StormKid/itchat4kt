package com.stormkid.itchat4ktx.core

import android.content.Context
import android.text.TextUtils
import com.stormkid.itchat4ktx.*
import com.stormkid.itchat4ktx.constants.KeyContants
import com.stormkid.itchat4ktx.constants.UrlConstants
import com.stormkid.itchat4ktx.util.Log
import com.stormkid.itchat4ktx.util.PublicSharePreference
import com.stormkid.itchat4ktx.util.Utils
import com.stormkid.okhttpkt.core.Okkt
import com.stormkid.okhttpkt.rule.CallbackRule
import com.stormkid.okhttpkt.rule.StringCallback
import com.stormkid.okhttpkt.utils.GsonFactory
import org.litepal.LitePal

/**
基本初始化请求
@author ke_li
@date 2019/6/27
 */
class ConfigWorker(private val context: Context) {

    private val API_KEY = "wx782c26e4c19acffb"
    private val FUNC = "new"

    /**
     * 获取串码钥匙
     */
    fun getUUid(callback: (String) -> Unit) {
        Okkt.instance.Builder().setUrl(UrlConstants.UUID_URL).setParams(
            hashMapOf(
                "appid" to API_KEY,
                "fun" to FUNC
            )
        ).getString(object : StringCallback {
            override suspend fun onFailed(error: String) {
                Utils.showToast(context, error)
            }

            override suspend fun onSuccess(entity: String, flag: String) {
                try {
                    val key = entity.split(";")[1]
                    val uuid = key.split("uuid")[1].split("\"")[1]
                    PublicSharePreference.putString(context, KeyContants.UUID_KEY, uuid)
                    callback.invoke(uuid)
                } catch (e: Exception) {
                    Utils.showToast(context, KeyContants.ERR)
                }

            }

        })
    }


    /**
     * 获取微信服务数据 用于不断刷新数据使用
     */
    fun webInit(callback: () -> Unit) {
        val time = -(System.currentTimeMillis() / 1579).toInt()
        val params = hashMapOf(
            "r" to "$time",
            "pass_ticket" to Config.instance.baseInfoData.pass_ticket
        )
        val body = hashMapOf("BaseRequest" to Config.instance.baseRequest)
        val json = GsonFactory.toJson(body)
        Okkt.instance.Builder().setUrl(UrlConstants.INIT_URL).setParams(params)
            .postJson(json, object : CallbackRule<InitInfo> {
                override suspend fun onFailed(error: String) {
                    Utils.showToast(context, error)
                }

                override suspend fun onSuccess(entity: InitInfo, flag: String) {
                    if (entity.BaseResponse.Ret == 0) {
                        Config.instance.friends.clear()
                        Config.instance.rooms.clear()
                        Config.instance.services.clear()
                        // 填充可显示的信息
                        val contacts = entity.ContactList
                        contacts.forEach {
                            if (it.Sex != 0 || it.UserName.contains("@")) {
                                val friend = createFriend(it)
                                Config.instance.friends.add(friend)
                            } else if (it.UserName.contains("@@")) {
                                val room = createChartRoom(it)
                                Config.instance.rooms.add(room)
                            } else {
                                val friend = createFriend(it)
                                Config.instance.services.add(friend)
                            }
                        }
                        Config.instance.baseInfoData.synckey = initSyncKey(entity.SyncKey)
                        Log.w( Config.instance.baseInfoData.synckey)
                        // 存储用户信息
                        val user = LitePal.findFirst(User::class.java)
                        user?.delete()
                        entity.User.save()
                        callback.invoke()
                    } else {
                        Utils.showToast(context, "您的用户信息受限，请用其他账号登录此应用")
                    }
                }

            })

    }

    /**
     * 获取所有的联系人
     */
    fun getContacts() {
        val skey = Config.instance.baseInfoData.skey
        if (TextUtils.isEmpty(skey)) {
            Utils.showToast(context, "登录信息异常，请重新扫码登录")
            return
        }
        val params = hashMapOf("r" to "${System.currentTimeMillis()}", "seq" to "0", "skey" to skey)
        Okkt.instance.Builder().setUrl(UrlConstants.WEB_WX_GET_CONTACT).setParams(params)
            .postStringJson(object : StringCallback {
                override suspend fun onFailed(error: String) {
                }

                override suspend fun onSuccess(entity: String, flag: String) {
//                    val path = context.externalCacheDir?.absolutePath + "/pp.json"
//                    val file = File(path)
//                    file.writeText(entity)
                    Log.w("---$entity")
                }

            })
    }


    /**
     * 检查是不是活跃用户
     */
    fun checkIsAlive() {
        val path = UrlConstants.SYNC_CHECK_URL
        val params = hashMapOf(
            "r" to "${System.currentTimeMillis()}",
            "skey" to Config.instance.baseInfoData.skey,
            "sid" to Config.instance.baseInfoData.wxsid,
            "uin" to Config.instance.baseInfoData.wxuin,
            "deviceid" to Config.instance.loginConfigData.deviceId,
            "synckey" to Config.instance.baseInfoData.synckey,
            "_" to Config.instance.loginConfigData.loginTime
        )
        Config.instance.loginConfigData.loginTime += 1

    }


    private fun createFriend(contact: Contact) =
        FriendData(
            contact.DisplayName,
            contact.NickName,
            contact.RemarkName,
            contact.HeadImgUrl,
            contact.Signature,
            contact.UserName,
            contact.Sex
        )

    private fun createChartRoom(contact: Contact) =
        RoomData(
            contact.DisplayName,
            contact.NickName,
            contact.RemarkName,
            contact.UserName,
            contact.MemberCount,
            contact.MemberList
        )

    private fun initSyncKey(key: SyncKey): String {
        val list = key.List
        var result = ""
        list.forEach {
            result =  result.plus("${it.Key}_${it.Val}|")
        }
        result = result.subSequence(0, result.length-1).toString()
        return result
    }

}