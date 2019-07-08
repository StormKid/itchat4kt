package com.stormkid.itchat4ktx

import org.litepal.crud.LitePalSupport
import java.io.Serializable

/**
各种 Bean
@author ke_li
@date 2019/6/25
 */

open class BaseDao : LitePalSupport(),Serializable

data class LoginData(
    val id:Int
) :BaseDao()