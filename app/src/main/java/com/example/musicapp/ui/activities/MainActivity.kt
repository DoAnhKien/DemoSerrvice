package com.example.musicapp.ui.activities

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Handler
import android.os.IBinder
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.service.MusicService
import com.example.musicapp.util.Const
import com.example.musicapp.util.Const.MAIN_ACTIVITY_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {
    private val STORAGE_PERMISSION = 1000
    private var sbTime: SeekBar? = null
    private var musicService: MusicService? = null
    private val handler = Handler()


    companion object {
        private const val TAG = "Kienda"

    }


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
            handleSeekbarEventAndTimeInSeekbar()
            handler.postDelayed(this, 150)
        }
    }

    private fun handleSeekbarEventAndTimeInSeekbar() {
        binding?.tvTotalTime?.text = milliSecondsToTimer(
            musicService?.getMediaManager()?.getMediaPlayer()?.duration!!.toLong()
        )
        binding?.tvStartTime?.text = milliSecondsToTimer(
            musicService?.getMediaManager()?.getMediaPlayer()?.currentPosition!!.toLong()
        )
        binding?.mainSeekBar?.progress =
            musicService?.getMediaManager()?.getMediaPlayer()?.currentPosition!!
        binding?.mainSeekBar?.max = musicService?.getMediaManager()?.getMediaPlayer()?.duration!!

        binding?.mainSeekBar?.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {

                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {
                    musicService?.getMediaManager()?.getMediaPlayer()?.seekTo(p0!!.progress)
                    Log.d(TAG, "onStopTrackingTouch: ${p0?.progress}")
                }

            }
        )

    }


    fun milliSecondsToTimer(milliseconds: Long): String? {
        var finalTimerString = ""
        var secondsString = ""
        // Convert total duration into time
        val hours = (milliseconds / (1000 * 60 * 60)).toInt()
        val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
        val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
        // Add hours if there
        if (hours > 0) {
            finalTimerString = "$hours:"
        }
        // Prepending 0 to seconds if it is one digit
        secondsString = if (seconds < 10) {
            "0$seconds"
        } else {
            "" + seconds
        }
        finalTimerString = "$finalTimerString$minutes:$secondsString"
        // return timer string
        return finalTimerString
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
            R.id.imgMenu -> {
                sendMessage()
            }
        }
    }

    fun sendMessage() {
        val intent = Intent(this@MainActivity, AllSongActivity::class.java)
        intent.putExtra("kienda", "kkk")
        startActivityForResult(intent, MAIN_ACTIVITY_REQUEST_CODE)
    }

    fun setTitleForSong() {
        binding?.tvSongName?.text = musicService?.getMediaManager()?.getCurrentSong()?.title
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

    private fun playMusicByPosition() {
        val intent = Intent(Const.ACTION_PLAY_MUSIC_BY_POSITION)
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
        binding?.imgMenu?.setOnClickListener(this)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MAIN_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            val position = data.getIntExtra("kienda", 88)
            musicService?.getMediaManager()?.setCurrentIndex(position)
            playMusicByPosition()
            Log.d(TAG, "onActivityResult: ${data.getIntExtra("kienda", 88)}")
        }
    }


}