package com.example.musicapp.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.musicapp.R
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.databinding.ActivityMain2Binding
import com.example.musicapp.service.ExampleIntentService


const val TYPE_ONE = 1
const val TYPE_TWO = 2

class MainActivity2 : BaseActivity<ActivityMain2Binding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val intent1 = Intent(this, ExampleIntentService::class.java)
        intent1.putExtra("type", TYPE_ONE)
        startService(intent1)

        val intent2 = Intent(this, ExampleIntentService::class.java)
        intent2.putExtra("type", TYPE_TWO)
        startService(intent2)

    }

    override fun initLayout(): Int = R.layout.activity_main2


    override fun init() {
        TODO("Not yet implemented")
    }

    override fun initViews() {
        TODO("Not yet implemented")
    }

    override fun setOnClickForViews() {
        TODO("Not yet implemented")
    }
}

