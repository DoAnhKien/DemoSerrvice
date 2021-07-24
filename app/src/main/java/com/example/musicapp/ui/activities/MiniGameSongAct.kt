package com.example.musicapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.media.MediaManager
import com.example.musicapp.model.SongItem
import com.example.musicapp.util.Const
import java.io.IOException
import java.util.*

class MiniGameSongAct : Activity(), Runnable, View.OnClickListener {
    private var tvScore: TextView? = null
    private var tvTimeProgress: TextView? = null
    private var btnAnswerA: Button? = null
    private var btnAnswerB: Button? = null
    private var btnAnswerC: Button? = null
    private var btnAnswerD: Button? = null
    private var imvPlayGame: ImageView? = null
    private var llAnswer: LinearLayout? = null
    private var arrAllSong: MutableList<SongItem>? = ArrayList<SongItem>()
    private val arrIndex = ArrayList<Int>()
    private val arrIndexAnswer = ArrayList<Int?>()
    private var thread: Thread? = null
    private var rd: Random? = null
    private var mPlayer: MediaPlayer? = null
    private var isRunning = false
    private var isPause = false
    private val isUpdated = false
    private var flagStartGame = false
    private var flagGameOver = false
    private var timeProgress = 0
    private var score = 0
    private var correctAnswer: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mini_game)
        changeTheColorOfTheStatusBar()
        initView()
        queryAudioSongs()
        initPlayer()
        initIndexArr()
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

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorImageDark)
    }

    private fun initView() {
        isRunning = true
        flagStartGame = true
        flagGameOver = false
        timeProgress = 3
        rd = Random()
        llAnswer = findViewById<View>(R.id.ll_answer) as LinearLayout
        llAnswer!!.visibility = View.GONE
        tvScore = findViewById<View>(R.id.tvScore) as TextView
        tvTimeProgress = findViewById<View>(R.id.tv_time_progress) as TextView
        tvTimeProgress!!.visibility = View.GONE
        imvPlayGame = findViewById<View>(R.id.imv_start_mini_game) as ImageView
        btnAnswerA = findViewById<View>(R.id.btn_answer_a) as Button
        btnAnswerB = findViewById<View>(R.id.btn_answer_b) as Button
        btnAnswerC = findViewById<View>(R.id.btn_answer_c) as Button
        btnAnswerD = findViewById<View>(R.id.btn_answer_d) as Button
        btnAnswerA!!.setOnClickListener(this)
        btnAnswerB!!.setOnClickListener(this)
        btnAnswerC!!.setOnClickListener(this)
        btnAnswerD!!.setOnClickListener(this)
        imvPlayGame!!.setOnClickListener(this)
    }

    fun initPlayer() {
        mPlayer = MediaPlayer()
        mPlayer!!.setOnPreparedListener {
            Log.i(
                TAG,
                "onPrepared...."
            )
        }
        mPlayer!!.setOnErrorListener { mp, what, extra -> //Khi setDataSource k co
            Log.e(TAG, "error....")
            false
        }
        mPlayer!!.setOnCompletionListener {
            //Sau khi xong 1 bai hat
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
        thread!!.start()
    }

    private fun nextSong() {
        if (arrIndex.size == 0) {
            initIndexArr()
        }
        if (arrAllSong!!.size > 4) {
            arrIndex.shuffle()
            arrIndexAnswer.shuffle()
            arrAllSong?.shuffle()
            var position = arrIndex[0]
            var duration: Int = arrAllSong!![position].duration
            while (duration / 1000 < 50) {
                position++
                duration = arrAllSong!![position].duration
            }
            var answerA: String = arrAllSong!![position].title
            var answerB: String = arrAllSong!![arrIndexAnswer[1]!!].title
            var answerC: String = arrAllSong!![arrIndexAnswer[2]!!].title
            var answerD: String = arrAllSong!![arrIndexAnswer[3]!!].title
            //Cat chuoi de khi hien thi se khong bi tran` ky tu
            if (answerA.length >= 24) {
                answerA = answerA.substring(0, 23)
            }
            if (answerB.length >= 24) {
                answerB = answerB.substring(0, 23)
            }
            if (answerC.length >= 24) {
                answerC = answerC.substring(0, 23)
            }
            if (answerD.length >= 24) {
                answerD = answerD.substring(0, 23)
            }
            val answer = arrayOf(answerA, answerB, answerC, answerD)
            btnAnswerA!!.text = answer[arrIndexAnswer[0]!!]
            btnAnswerB!!.text = answer[arrIndexAnswer[1]!!]
            btnAnswerC!!.text = answer[arrIndexAnswer[2]!!]
            btnAnswerD!!.text = answer[arrIndexAnswer[3]!!]
            playSong(position)
            correctAnswer = answerA
        } else {
        }
    }

    private fun playSong(position: Int) {
        val duration: Int = arrAllSong!![position].duration - 30000
        play(position)
        seekTo(rd!!.nextInt(duration) + 10000)
        arrIndex.removeAt(0)
    }

    private fun stopSong() {
        mPlayer!!.reset()
        mPlayer!!.stop()
    }

    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            val what = msg.what
            val flagPlaySong = msg.arg2
            val progress = msg.arg1
            when (what) {
                UPDATE_TIME_PROGESS -> {
                    tvTimeProgress!!.text = progress.toString() + ""
                    if (flagPlaySong == FLAG_PLAY_SONG) {
                        llAnswer!!.visibility = View.VISIBLE
                        if (arrAllSong!!.size > 4) {
                            nextSong()
                        } else {
                            //SnackBar
                        }
                    }
                }
            }
        }
    }

    override fun run() {
        while (isRunning) {
            try {
                Thread.sleep(1000)
                timeProgress--
                if (timeProgress <= 0) {
                    if (flagStartGame) {
                        timeProgress = 10
                        flagGameOver = false
                        flagStartGame = false
                        val message = Message()
                        message.arg1 = timeProgress
                        message.arg2 = FLAG_PLAY_SONG
                        message.what = UPDATE_TIME_PROGESS
                        message.target = handler
                        message.sendToTarget()
                    } else {
                        isRunning = false
                        flagGameOver = true
                        timeProgress = 10
                        break
                    }
                } else {
                    val message = Message()
                    message.arg1 = timeProgress
                    message.arg2 = FLAG_TURN_OFF_SONG
                    message.what = UPDATE_TIME_PROGESS
                    message.target = handler
                    message.sendToTarget()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            while (isPause) {
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        if (flagGameOver) {
            stop()
            val hightScore = readHightScore()
            if (hightScore < score) {
                showGameOver(true)
            } else {
                showGameOver(false)
            }
        }
    }

    fun readHightScore(): Int {
        val pre = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE)
        return pre.getInt(Const.KEY_HIGHT_SCORE, -1)
    }

    fun writeHightScore(hightScore: Int) {
        val pref = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(Const.KEY_HIGHT_SCORE, hightScore)
        editor.apply()
    }

    fun showGameOver(isStatus: Boolean) {
        stopSong()
        val intentGameOver = Intent()
        intentGameOver.setClass(this@MiniGameSongAct, HightScoreActivity::class.java)
        intentGameOver.putExtra(Const.KEY_SCORE, tvScore!!.text.toString())
        intentGameOver.putExtra(Const.KEY_STATUS, isStatus)
        startActivityForResult(intentGameOver, Const.KEY_OVER)
        if (isStatus) {
            writeHightScore(score)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Const.KEY_OVER -> if (resultCode == RESULT_OK) {
                isRunning = true
                timeProgress = 10
                score = 0
                tvScore?.text = score.toString()
                startThread()
                if (arrAllSong!!.size > 4) {
                    nextSong()
                } else {
                    //SnackBar
                }
            } else if (resultCode == RESULT_CANCELED) {
                finish()
            }
            else -> {
            }
        }
    }

    override fun onClick(v: View) {
        var userAnswer = ""
        when (v.id) {
            R.id.imv_start_mini_game -> {
                imvPlayGame!!.visibility = View.GONE
                tvTimeProgress!!.visibility = View.VISIBLE
                startThread()
            }
            R.id.btn_answer_a -> {
                userAnswer = btnAnswerA!!.text.toString()
                handlerQuestion(userAnswer)
            }
            R.id.btn_answer_b -> {
                userAnswer = btnAnswerB!!.text.toString()
                handlerQuestion(userAnswer)
            }
            R.id.btn_answer_c -> {
                userAnswer = btnAnswerC!!.text.toString()
                handlerQuestion(userAnswer)
            }
            R.id.btn_answer_d -> {
                userAnswer = btnAnswerD!!.text.toString()
                handlerQuestion(userAnswer)
            }
            else -> {
            }
        }
    }

    private fun handlerQuestion(userAnswer: String) {
        if (userAnswer == correctAnswer) {
            score++
            tvScore?.text = score.toString()
            timeProgress = 11
            if (arrAllSong!!.size > 4) {
                nextSong()
            } else {
                //SnackBar
            }
        } else {
            isRunning = false
            flagGameOver = true
            timeProgress = 11
        }
    }

    fun pause() {
        isPause = true
        if (mPlayer!!.isPlaying) {
            mPlayer!!.pause()
        } else {
            mPlayer!!.start()
        }
    }

    override fun onPause() {
        super.onPause()
        pause()
    }

    override fun onResume() {
        super.onResume()
        mPlayer!!.start()
        isPause = false
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        stop()
    }

    companion object {
        private const val UPDATE_TIME_PROGESS = 0
        private const val FLAG_TURN_OFF_SONG = 0
        private const val FLAG_PLAY_SONG = 1
        protected const val TAG = "MiniGameSongAct"
    }
}
