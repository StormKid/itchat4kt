package com.stormkid.itchat4ktx.constants

/**
各种微信请求
@author ke_li
@date 2019/6/25
 */
object UrlConstants {
    const val BASE_URL = "https://login.weixin.qq.com"
    const val UUID_URL = "/jslogin"
    const val QRCODE_URL = "/qrcode" // 二维码初始化url
    const val STATUS_NOTIFY_URL = "/webwxstatusnotify" // 微信状态通知 params:pass_ticket
    const val LOGIN_URL = "/cgi-bin/mmwebwx-bin/login" // 登录URI
    const val INIT_URL = "webwxinit" //初始化URL
    const val SYNC_CHECK_URL = "/synccheck" // 检查心跳
    const val WEB_WX_SYNC_URL = "/webwxsync" //web微信消息同步Url
    const val WEB_WX_SEND_MSG = "/webwxsendmsg" //发送消息URL
    const val WEB_WX_UPLOAD_MEDIA = "/webwxuploadmedia" // 上传文件到服务器
    const val WEB_WX_GET_MSG_IMG = "/webwxgetmsgimg" // 下载图片消息
    const val WEB_WX_GET_VOICE = "/webwxgetvoice" //下载语音消息
    const val WEB_WX_GET_VIEDO = "/webwxgetvideo" //下载视频消息
    const val WEB_WX_PUSH_LOGIN = "/cgi-bin/mmwebwx-bin/webwxpushloginurl" // 不扫码登录
    const val WEB_WX_LOGOUT = "/webwxlogout"// 退出登录
    const val WEB_WX_BATCH_GET_CONTACT = "/webwxbatchgetcontact"// 查询群信息
    const val WEB_WX_REMARKNAME = "/webwxoplog" //修改好友备注
    const val WEB_WX_VERIFYUSER = "/webwxverifyuser" // 被动添加好友
    const val WEB_WX_GET_MEDIA = "/webwxgetmedia" // 下载文件



}