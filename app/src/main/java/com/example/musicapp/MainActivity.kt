package com.example.musicapp

import android.Manifest

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.service.MusicService
import com.example.musicapp.util.Const
import com.teamdev.myapplication.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {
    private val STORAGE_PERMISSION = 1000
    private var sbTime: SeekBar? = null
    private var musicService: MusicService? = null
    private val handler = Handler()


    private val connection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val binder: MusicService.MusicBinder = iBinder as MusicService.MusicBinder
            musicService = binder.musicService
            runOnUiThread(runnable)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMusic()
            } else {
                Toast.makeText(this, "you are so stupid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startMusic() {
        val intent = Intent(this, MusicService::class.java)
        startService(intent)
        bindService(intent, connection, BIND_AUTO_CREATE)
    }

    private fun checkThePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) ==
            PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION
            )
        } else {
            startMusic()
        }
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            updateThePlayPauseIconForUi()
            setTitleForSong()
            handler.postDelayed(this, 150)
//            setTitleForSong()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.imgNext -> {
                nextSong()
            }
            R.id.imgPrevious -> {
                previousSong()
            }
            R.id.imgPausePlay -> {
                updateThePlayPauseIconForUi()
                playSong()
            }
        }
    }

    fun setTitleForSong() {
        binding?.tvSongName?.text = musicService?.getMediaManager()?.getCurrentSong()?.songName
    }

    fun updateThePlayPauseIconForUi() {
        if (musicService?.getMediaPlayer()!!.isPlaying) {
            binding?.imgPausePlay?.setImageResource(R.drawable.ic_pause)
            return
        }
        binding?.imgPausePlay?.setImageResource(R.drawable.ic_play)
    }

    private fun nextSong() {
        val intent = Intent(Const.ACTION_NEXT)
        sendBroadcast(intent)
    }

    private fun cancelSong() {
        val intent = Intent(Const.ACTION_CANCEL)
        sendBroadcast(intent)
    }

    private fun previousSong() {
        val intent = Intent(Const.ACTION_PREVIOUS)
        sendBroadcast(intent)
    }

    private fun playSong() {
        val intent = Intent(Const.ACTION_PLAY)
        sendBroadcast(intent)
    }

    private fun pauseSong() {
        val intent = Intent(Const.ACTION_PAUSE)
        sendBroadcast(intent)
    }

    override fun initLayout(): Int = R.layout.activity_main

    override fun init() {
        checkThePermission()
        changeTheColorOfTheStatusBar()
        musicService?.setActivity(this@MainActivity)
    }

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorImageDark)
    }

    override fun setOnClickForViews() {
        binding?.imgNext?.setOnClickListener(this)
        binding?.imgPausePlay?.setOnClickListener(this)
        binding?.imgPrevious?.setOnClickListener(this)
    }

    private fun runAnimationForTextView() {
        val a: Animation = AnimationUtils.loadAnimation(this, R.anim.animation_translate)
        a.reset()
        binding?.tvSongName?.clearAnimation()
        binding?.tvSongName?.startAnimation(a)
    }

    private fun runAnimationForImageView() {
        val a: Animation = AnimationUtils.loadAnimation(this, R.anim.anim_img_rotate)
        a.reset()
        binding?.imgCenter?.clearAnimation()
        binding?.imgCenter?.startAnimation(a)
    }

    override fun initViews() {
        runAnimationForTextView()
        runAnimationForImageView()
    }
}