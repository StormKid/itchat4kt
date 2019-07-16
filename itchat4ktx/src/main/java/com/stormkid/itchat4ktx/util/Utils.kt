package com.stormkid.itchat4ktx.util

import android.content.Context
import android.widget.Toast
import com.yanzhenjie.permission.AndPermission

/**
各种工具集合
@author ke_li
@date 2019/6/24
 */
object Utils {
    fun  showToast(context: Context,msg:String) = Toast.makeText(context,msg,Toast.LENGTH_SHORT).show()

    fun  putPermission(context: Context,permission:Array<out String>,callback:()->Unit){
       AndPermission.with(context).runtime().permission(permission).onDenied {
           showToast(context,"请打开所需权限")
       }.onGranted {
           callback.invoke()
       }.start()
    }
}