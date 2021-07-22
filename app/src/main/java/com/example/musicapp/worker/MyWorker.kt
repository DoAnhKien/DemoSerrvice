package com.example.musicapp.worker

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.musicapp.ui.activities.MainActivity
import com.example.musicapp.ui.activities.MainMusicActivity
import com.example.musicapp.ui.activities.SplashActivity


class MyWorker(
    context: Context,
    workerParameters: WorkerParameters,
) :
    Worker(context, workerParameters) {

    companion object {
        val SPLASH_DISPLAY_LENGTH = 2000L

    }

    private val context: Context = context


    override fun doWork(): Result {

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent = Intent(context, MainMusicActivity::class.java)
            context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
        }, SPLASH_DISPLAY_LENGTH)

        return Result.success()
    }
}