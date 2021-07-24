package com.example.musicapp.ui.activities

import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.databinding.ActivityMiniGameBinding
import com.example.musicapp.media.MediaManager
import com.example.musicapp.model.SongItem
import java.io.IOException
import java.util.*

class MiniGameActivity : BaseActivity<ActivityMiniGameBinding>(), View.OnClickListener, Runnable {

    private var arrAllSong: MutableList<SongItem>? = ArrayList<SongItem>()
    private val arrIndex = ArrayList<Int>()
    private val arrIndexAnswer = ArrayList<Int>()
    private var mPlayer: MediaPlayer? = null
    private var isRunning = false

    private var thread: Thread? = null

    override fun initLayout(): Int = R.layout.activity_mini_game

    override fun init() {
        queryAudioSongs()
        initIndexArr()
        initPlayer()
    }

    override fun setOnClickForViews() {
        binding?.btnAnswerA!!.setOnClickListener(this)
        binding?.btnAnswerB!!.setOnClickListener(this)
        binding?.btnAnswerC!!.setOnClickListener(this)
        binding?.btnAnswerD!!.setOnClickListener(this)
        binding?.imvStartMiniGame!!.setOnClickListener(this)
    }

    override fun initViews() {
        changeTheColorOfTheStatusBar()
    }

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorImageDark)
    }

    private fun queryAudioSongs() {
        arrAllSong = if (arrAllSong!!.size > 0) {
            return
        } else {
            MediaManager(this).getInstance(this)?.getSongList()
        }
    }

    private fun initIndexArr() {
        for (index in arrAllSong!!.indices) {
            arrIndex.add(index)
        }
        for (index in 0..3) {
            arrIndexAnswer.add(index)
        }
    }

    fun initPlayer() {
        mPlayer = MediaPlayer()
        mPlayer!!.setOnPreparedListener {

        }
        mPlayer!!.setOnErrorListener { mp, what, extra ->
            false
        }
        mPlayer!!.setOnCompletionListener {

        }
    }

    fun play(position: Int) {
        mPlayer!!.reset()
        try {
            mPlayer!!.setDataSource(this, Uri.parse(arrAllSong!![position].dataPath))
            mPlayer!!.prepare()
            mPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun stop() {
        mPlayer!!.stop()
        mPlayer!!.reset()
    }

    fun seekTo(msec: Int) {
        mPlayer!!.seekTo(msec)
        //mPlayer.start();
    }

    private fun startThread() {
        thread = Thread(this)
        thread?.start()
    }


    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_answer_a -> {

            }
            R.id.btn_answer_b -> {

            }
            R.id.btn_answer_c -> {

            }
            R.id.btn_answer_d -> {

            }
            R.id.imv_start_mini_game -> {

            }
        }
    }

    override fun run() {

    }


}