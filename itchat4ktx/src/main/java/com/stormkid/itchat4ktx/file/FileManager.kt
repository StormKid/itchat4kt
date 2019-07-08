package com.stormkid.itchat4ktx.file

import android.content.Context
import java.io.File

/**

@author ke_li
@date 2019/7/5
 */
object FileManager {
    fun makeDir(context: Context, type: String): File? {
        if (context.getExternalFilesDir(type)?.exists()!!)
        else context.getExternalFilesDir(type)?.mkdir()
        return context.getExternalFilesDir(type)
    }

    fun getFile(context: Context, type: String, path: String) = try {
        File(context.getExternalFilesDir(type)?.absolutePath + path)
    } catch (e: Exception) {
        null
    }
}