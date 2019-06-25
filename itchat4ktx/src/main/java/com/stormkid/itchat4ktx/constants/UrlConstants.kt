package com.stormkid.itchat4ktx.constants

/**
各种微信请求
@author ke_li
@date 2019/6/25
 */
object UrlConstants {
    const val BASE_URL = "https://login.weixin.qq.com"
    const val UUID_URL = "$BASE_URL/jslogin"
    const val QRCODE_URL = "$BASE_URL/qrcode/" // 二维码初始化url
    const val STATUS_NOTIFY_URL = "$BASE_URL/webwxstatusnotify?hang=zn_CN&pass_ticket=%s" // 微信状态通知
    const val LOGIN_URL = "$BASE_URL/cgi-bin/mmwebwx-bin/login"
    const val INIT_URL = "%s/webwxinit?r=%s&pass_ticket=%s" //初始化URL
    const val SYNC_CHECK_URL = "/synccheck" // 检查心跳
    const val WEB_WX_SYNC_URL = "%s/webwxsync?sid=%s&skey=%s&pass_ticket=%s" //web微信消息同步Url
    const val WEB_WX_SEND_MSG = "%s/webwxsendmsg" //发送消息URL
    const val WEB_WX_UPLOAD_MEDIA = "%s/webwxuploadmedia?f=json" // 上传文件到服务器
    const val WEB_WX_GET_MSG_IMG = "%s/webwxgetmsgimg" // 下载图片消息
    const val WEB_WX_GET_VOICE = "%s/webwxgetvoice" //下载语音消息
    const val WEB_WX_GET_VIEDO = "%s/webwxgetvideo" //下载视频消息
    const val WEB_WX_PUSH_LOGIN = "%s/webwxpushloginurl" // 不扫码登录
    const val WEB_WX_LOGOUT = "%s/webwxlogout"// 退出登录
    const val WEB_WX_BATCH_GET_CONTACT = "%s/webwxbatchgetcontact?type=ex&r=%s&lang=zh_CN&pass_ticket=%s"// 查询群信息
    const val WEB_WX_REMARKNAME = "%s/webwxoplog?lang=zh_CN&pass_ticket=%s" //修改好友备注
    const val WEB_WX_VERIFYUSER = "%s/webwxverifyuser?r=%s&lang=zh_CN&pass_ticket=%s" // 被动添加好友
    const val WEB_WX_GET_MEDIA = "%s/webwxgetmedia" // 下载文件



}