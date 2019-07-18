package com.stormkid.itchat4ktx.core

import android.content.Context
import com.stormkid.itchat4ktx.*
import com.stormkid.itchat4ktx.constants.ConfigConstants
import com.stormkid.itchat4ktx.constants.UrlConstants
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


    /**
     * 获取微信服务数据 用于不断刷新数据使用
     */
    fun webInit(callback: () -> Unit) {
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
                Utils.showToast(context, error)
            }

            override suspend fun onSuccess(entity: InitInfo, flag: String) {
                if (entity.BaseResponse.Ret == 0){
                    Config.instance.friends.clear()
                    Config.instance.rooms.clear()
                    Config.instance.services.clear()
                    // 填充可显示的信息
                    val contacts = entity.ContactList
                    contacts.forEach {
                        if (it.Sex!=0||it.UserName.contains("@")){
                            val friend = createFriend(it)
                            Config.instance.friends.add(friend)
                        }else if (it.UserName.contains("@@")){
                            val room = createChartRoom(it)
                            Config.instance.rooms.add(room)
                        }else {
                            val friend = createFriend(it)
                            Config.instance.services.add(friend)
                        }
                    }
                    // 存储用户信息
                    val user = LitePal.findFirst(User::class.java)
                    user?.delete()
                    entity.User.save()
                    callback.invoke()
                }else{
                    Utils.showToast(context, "您的用户信息受限，请用其他账号登录此应用")
                }
            }

        })

    }

    private fun createFriend(contact: Contact) =
        FriendData(contact.DisplayName,contact.NickName,contact.RemarkName,contact.HeadImgUrl,contact.Signature,contact.UserName,contact.Sex)

    private fun createChartRoom(contact: Contact)=
        RoomData(contact.DisplayName,contact.NickName,contact.RemarkName,contact.UserName,contact.MemberCount,contact.MemberList)

}