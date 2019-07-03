package com.stormkid.itchartdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stormkid.itchat4ktx.Config
import com.stormkid.itchat4ktx.util.Utils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Config.instance.configWorker!!.getUUid {
            Utils.showToast(this,it)
        }

        doNow.setOnClickListener {
//            Config.instance.loginWorker!!.getQrCode{
//                qrCode.setImageBitmap(it)
//            }

            Config.instance.loginWorker!!.pushLogin()
        }



    }

    override fun onDestroy() {
        super.onDestroy()
        Config.instance.configWorker!!.close()
    }
}
