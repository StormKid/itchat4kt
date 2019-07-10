package com.stormkid.itchat4ktx.util

import android.content.Context
import android.widget.Toast

/**
各种工具集合
@author ke_li
@date 2019/6/24
 */
object Utils {
    fun  showToast(context: Context,msg:String) = Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()
}