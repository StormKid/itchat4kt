package com.stormkid.itchat4ktx.constants

import android.content.Context
import android.os.Environment

/**

@author ke_li
@date 2019/6/25
 */
object ConfigConstants {
    const val API_WXAPPID = "API_WXAPPID"
    /**
     * qrcode 的本地路径
     */
    fun getPicDir(context: Context) = context.externalCacheDir?.let {
        if (!it.exists()) it.mkdir()
        it.absolutePath
    }
    const val VERSION = "1.3.10"
    const val BASE_URL = "https://login.weixin.qq.com"
    const val OS = "Linux"
    fun DIR(context: Context) = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let {
        if (!it.exists()) it.mkdir()
        it.absolutePath
    }
    const val DEFAULT_QR = "QR.jpg"
    const val USER_AGENT =
        "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36"


}