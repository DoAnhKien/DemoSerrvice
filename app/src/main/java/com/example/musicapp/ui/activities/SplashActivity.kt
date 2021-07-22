package com.example.musicapp.ui.activities

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.musicapp.R
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.databinding.ActivitySplashBinding
import com.example.musicapp.worker.MyWorker
import java.util.*

class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    override fun initLayout(): Int = R.layout.activity_splash

    companion object{
        private const val TAG = "kienda"
    }

    override fun init() {
        changeTheColorOfTheStatusBar()
        doInBackGround()
    }

    private fun doInBackGround() {

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(this, MainMusicActivity::class.java)
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            finish()
        }, MyWorker.SPLASH_DISPLAY_LENGTH)

//        val mRequest: WorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
//            .addTag("kienda")
//            .build()
//        val info = WorkManager.getInstance(this)
//            .enqueue(mRequest)
    }

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorBottom)
    }


    override fun setOnClickForViews() {

    }

    override fun initViews() {

    }

}