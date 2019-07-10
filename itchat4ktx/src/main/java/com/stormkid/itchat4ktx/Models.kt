package com.stormkid.itchat4ktx

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
各种 Bean
@author ke_li
@date 2019/6/25
 */

open class BaseDao : LitePalSupport(), Serializable

/**
 * 登录过后的用户信息
 */
data class LoginData(
    val id: Int
) : BaseDao()


/**
 * 好友信息
 */
data class FriendData(
    val id: Int
) : BaseDao()

/**
 * 群组信息
 */
data class RoomData(
    val id: Int
) : BaseDao()

/**
 * 登录操作缓存的内容
 */
data class LoginConfigData(
    var configUrl: String = "",
    var fileUrl: String = "",
    var syncUrl: String = "",
    var deviceId: String = "",
    var loginTime: Long = 0L
) : Serializable

/**
 * 登录核心信息
 */
data class BaseInfoData(
    var skey: String = "",
    var wxsid: String = "",
    var wxuin: String = "",
    var pass_ticket : String = ""
) : Serializable