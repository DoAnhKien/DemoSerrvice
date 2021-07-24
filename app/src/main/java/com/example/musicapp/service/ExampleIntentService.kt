package com.example.musicapp.service

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.musicapp.ui.activities.TYPE_ONE
import com.example.musicapp.ui.activities.TYPE_TWO


class ExampleIntentService : IntentService("ExampleIntentService") {
    private val TAG = "kienda"

    override fun onHandleIntent(p0: Intent?) {
        when(p0?.getIntExtra("type",0)){
            TYPE_ONE ->{
                for (i in 1..5) {
                    Log.d(TAG, "onHandleIntent: Task 1.$i")
                    Thread.sleep(1000)
                }
            }
            TYPE_TWO ->{
                for (i in 1..5) {
                    Log.d(TAG, "onHandleIntent: Task 2.$i")
                    Thread.sleep(1000)
                }
            }
        }
    }

}