package com.stormkid.itchat4ktx.util

import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**

@author ke_li
@date 2019/7/9
 */
class XmlParseHandler private constructor():DefaultHandler(){

    companion object {
        val instance by lazy { XmlParseHandler() }
    }


    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        super.startElement(uri, localName, qName, attributes)

    }


    override fun endElement(uri: String?, localName: String?, qName: String?) {
        super.endElement(uri, localName, qName)

    }



}