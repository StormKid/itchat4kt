package com.stormkid.itchartdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.stormkid.itchat4ktx.Config
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        doNow.setOnClickListener {
            Config.instance.loginWorker!!.checkLogin{ value ->
                doNow.text = value
            }
        }


    }
}
