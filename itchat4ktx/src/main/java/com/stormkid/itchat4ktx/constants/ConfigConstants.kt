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
    const val OS = "Linux"
    fun DIR(context: Context) = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)?.let {
        if (!it.exists()) it.mkdir()
        it.absolutePath
    }
    const val DEFAULT_QR = "QR.jpg"
    const val USER_AGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36"

    const val UUID_KEY = "UUID_KEY"

    const val ERR = "请检查您的网络链接"
}