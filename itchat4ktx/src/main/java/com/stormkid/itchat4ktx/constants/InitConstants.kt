package com.stormkid.itchat4ktx.constants

/**
init 常量
@author ke_li
@date 2019/6/25
 */
object InitConstants {
    const val TEXT = "Text"
    const val MAP = "Map"
    const val CARD = "Card"
    const val NOTE = "Note"
    const val SHARING = "Sharing"
    const val PICTURE = "Picture"
    const val VOICE = "Recording"
    const val RECORDING = VOICE
    const val ATTACHMENT = "Attachment"
    const val VIDEO = "Video"
    const val FRIENDS = "Friends"
    const val SYSTEM = "System"

    val INCOME_MSG = arrayListOf(
        TEXT, MAP, CARD, NOTE, SHARING, PICTURE,
        RECORDING, VOICE, ATTACHMENT, VIDEO, FRIENDS, SYSTEM
    )
}