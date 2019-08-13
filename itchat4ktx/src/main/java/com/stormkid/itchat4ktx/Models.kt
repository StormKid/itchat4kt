package com.stormkid.itchat4ktx

import com.stormkid.itchat4ktx.constants.KeyContants
import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
各种 Bean
@author ke_li
@date 2019/6/25
 */

open class BaseDao : LitePalSupport(), Serializable


/**
 * 好友信息
 */
data class FriendData(
    @Column(nullable = true)
    val DisplayName: String,
    @Column(nullable = true)
    val NickName: String,
    @Column(nullable = true)
    val RemarkName: String,
    @Column(nullable = true)
    val HeadImgUrl: String,
    @Column(nullable = true)
    val Signature: String,
    @Column(nullable = true)
    val UserName: String,
    @Column
    val Sex: Int
) : BaseDao()

/**
 * 群组信息
 */
data class RoomData(
    @Column(nullable = true)
    val DisplayName: String,
    @Column(nullable = true)
    val NickName: String,
    @Column(nullable = true)
    val RemarkName: String,
    @Column(nullable = true)
    val UserName: String,
    @Column
    val MemberCount: Int
) : BaseDao()

/**
 * 登录操作缓存的内容
 */
data class LoginConfigData(
    var configUrl: String = "",
    var fileUrl: String = "",
    var syncUrl: String = "",
    var deviceId: String = "",
    var wxUrl: String = "",
    var loginTime: Long = 0L
) : Serializable

/**
 * 登录核心信息
 */
data class BaseInfoData(
    var skey: String = "",
    var wxsid: String = "",
    var wxuin: String = "",
    var pass_ticket: String = "",
    var synckey: String = ""
) : Serializable

/**
 * 登录核心请求body
 */
data class BaseRequest(
    var Skey: String = "",
    var Sid: String = "",
    var Uin: String = "",
    var DeviceID: String = ""
) : Serializable

/**
 * push login
 */
data class PushResult(
    val ret: String,
    val msg: String,
    val uuid: String
) : Serializable

/**
 *  仿造手机进行的登录
 */
data class MobileLogin(
    val BaseRequest: BaseRequest,
    val FromUserName: String,
    val ToUserName: String,
    val Code: Int = 3,
    val ClientMsgId: Long = System.currentTimeMillis()
) : Serializable


/**
 *  登录后webinit 获得的信息
 */
data class InitInfo(
    val BaseResponse: BaseResponse,
    val ChatSet: String,
    val ClickReportInterval: Int,
    val ClientVersion: Int,
    val ContactList: MutableList<Contact>,
    val Count: Int,
    val GrayScale: Int,
    val InviteStartCount: Int,
    val MPSubscribeMsgCount: Int,
    val MPSubscribeMsgList: MutableList<MPSubscribeMsg>,
    val SKey: String,
    val SyncKey: SyncKey,
    val SystemTime: Int,
    val User: User
) : Serializable

data class SyncKey(
    val Count: Int,
    val List: MutableList<KeyList>
) : Serializable

/**
 * 外键钥匙
 */
data class KeyList(
    val Key: Int,
    val Val: Int
) : Serializable

/**
 * 服务号文章
 */
data class MPSubscribeMsg(
    val MPArticleCount: Int,
    val MPArticleList: MutableList<MPArticle>,
    val NickName: String,
    val Time: Int,
    val UserName: String
) : Serializable

/**
 * 公众号标题
 */
data class MPArticle(
    val Cover: String,
    val Digest: String,
    val Title: String,
    val Url: String
) : Serializable

/**
 * 本机登录用户信息
 */
data class User(
    @Column
    val flag: String = KeyContants.USER_FLAG,
    @Column
    val AppAccountFlag: Int,
    @Column
    val ContactFlag: Int,
    @Column
    val HeadImgFlag: Int,
    @Column(nullable = true)
    val HeadImgUrl: String,
    @Column
    val HideInputBarFlag: Int,
    @Column(nullable = true)
    val NickName: String,
    @Column(nullable = true)
    val PYInitial: String,
    @Column(nullable = true)
    val PYQuanPin: String,
    @Column(nullable = true)
    val RemarkName: String,
    @Column(nullable = true)
    val RemarkPYInitial: String,
    @Column(nullable = true)
    val RemarkPYQuanPin: String,
    @Column
    val Sex: Int,
    @Column
    val Signature: String,
    @Column
    val SnsFlag: Int,
    @Column
    val StarFriend: Int,
    @Column
    val Uin: Int,
    @Column(nullable = true)
    val UserName: String,
    @Column
    val VerifyFlag: Int,
    @Column
    val WebWxPluginSwitch: Int
) : BaseDao()

data class BaseResponse(
    val ErrMsg: String,
    val Ret: Int
) : Serializable

/**
 * 联系人信息(包含群组)
 */
data class Contact(
    val Alias: String,
    val AppAccountFlag: Int,
    val AttrStatus: Int,
    val ChatRoomId: Int,
    val City: String,
    val ContactFlag: Int,
    val DisplayName: String,
    val EncryChatRoomId: String,
    val HeadImgUrl: String,
    val HideInputBarFlag: Int,
    val IsOwner: Int,
    val KeyWord: String,
    val MemberCount: Int,
    /**
     * 如果是群组，那么这是群组内部的成员对象
     */
    val MemberList: MutableList<Contact>,
    val NickName: String,
    val OwnerUin: Int,
    val PYInitial: String,
    val PYQuanPin: String,
    val Province: String,
    val RemarkName: String,
    val RemarkPYInitial: String,
    val RemarkPYQuanPin: String,
    val Sex: Int,
    val Signature: String,
    val SnsFlag: Int,
    val StarFriend: Int,
    val Statues: Int,
    val Uin: Int,
    val UniFriend: Int,
    val UserName: String,
    val VerifyFlag: Int
) : Serializable


/**
 *  发送信息id验签
 */
data class MsgPass(
    val BaseResponse: BaseResponse,
    val MsgID: String
) : Serializable

/**
 * 联系人
 */
data class Contacts(
    val BaseResponse: BaseResponse,
    val MemberCount: Int,
    val MemberList: ArrayList<Contact>,
    val Seq: Int
)

