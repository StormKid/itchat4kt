package com.stormkid.itchartdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stormkid.itchat4ktx.Config

class MainActivity : AppCompatActivity() {

    private val data = "<error>" +
            "　　<ret>0</ret>" +
            "　　<message></message>" +
            "　　<skey>@crypt_8b4f09cc_a5871dc10130a48703b9afd5602152e4</skey>" +
            "　　<wxsid>+urBrYI292xoIknf</wxsid>" +
            "　　<wxuin>211722515</wxuin>" +
            "　　<pass_ticket>qg%2BL%2BfjNnoHyqYsL3xj0KoNi5nqchsInPWWSxDwqCJ8%3D</pass_ticket>" +
            "　　<isgrayscale>1</isgrayscale>" +
            "</error>"


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        doNow.setOnClickListener {
//            Config.instance.getBataConfigData(data)
//            doNow.text = "${Config.instance.baseInfoData.skey}-【】-${Config.instance.baseInfoData.wxuin}-【】-${Config.instance.baseInfoData.wxsid}-【】-${Config.instance.baseInfoData.pass_ticket}"
//        }



    }

    override fun onDestroy() {
        super.onDestroy()
        Config.instance.close(this)
    }
}
