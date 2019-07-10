package com.stormkid.itchat4ktx.util

import com.stormkid.itchat4ktx.BaseInfoData
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**

@author ke_li
@date 2019/7/9
 */
class BaseDataHandler(private val baseInfoData: BaseInfoData) : DefaultHandler() {

    private var key = ""
    private var isStart = false
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        super.startElement(uri, localName, qName, attributes)
        key = localName!!
        isStart = true
    }


    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)
        isStart = false
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        super.characters(ch, start, length)
        val result = String(ch!!, start, length).trim()
        if (isStart)
        when (key) {
            "skey" -> {
                baseInfoData.skey = result
            }
            "wxsid" -> {
                baseInfoData.wxsid = result
            }
            "wxuin" -> {
                baseInfoData.wxuin = result
            }
            "pass_ticket"->{
                baseInfoData.pass_ticket = result
            }
        }


    }

    override fun endDocument() {
        super.endDocument()
        key = ""
    }

}