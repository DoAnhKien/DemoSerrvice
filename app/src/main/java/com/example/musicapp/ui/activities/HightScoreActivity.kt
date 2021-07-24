package com.example.musicapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.musicapp.R
import com.example.musicapp.util.Const

class HightScoreActivity : Activity(), View.OnClickListener {
    private var tvUsername: TextView? = null
    private var tvScore: TextView? = null
    private var tvStatusPlay: TextView? = null
    private var tvBestScore: TextView? = null
    private var imvStatusPlay: ImageView? = null
    private var btnPlayAgain: Button? = null
    private var llInfor: LinearLayout? = null
    private var llBestScore: LinearLayout? = null
    private var mAnimationLayoutInfor: Animation? = null
    private var mAnimationButton: Animation? = null
    private var isStatus = false
    private var score: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hight_score)
        changeTheColorOfTheStatusBar()
        initViews()
    }

    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorImageDark)
    }

    private fun initViews() {
        mAnimationLayoutInfor = AnimationUtils.loadAnimation(
            this,
            R.anim.animation_translate
        )
        llInfor = findViewById<View>(R.id.ll_infor) as LinearLayout
        llInfor!!.startAnimation(mAnimationLayoutInfor)
        btnPlayAgain = findViewById<View>(R.id.btn_play_again) as Button
        btnPlayAgain!!.setOnClickListener(this)
        btnPlayAgain!!.visibility = View.GONE
        mAnimationButton = AnimationUtils.loadAnimation(
            this,
            R.anim.animation_scale_button_game_over
        )
        mAnimationLayoutInfor!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                btnPlayAgain!!.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                btnPlayAgain!!.visibility = View.VISIBLE
                btnPlayAgain!!.animation = mAnimationButton
                mAnimationButton!!.start()
            }
        })
        tvUsername = findViewById<View>(R.id.tv_username_game_over) as TextView
        tvScore = findViewById<View>(R.id.tv_score_game_over) as TextView
        tvScore!!.text = score
        tvBestScore = findViewById<View>(R.id.tv_best_score) as TextView
        tvStatusPlay = findViewById<View>(R.id.tv_state_play) as TextView
        imvStatusPlay = findViewById<View>(R.id.imv_status_play) as ImageView
        llBestScore = findViewById<View>(R.id.ll_best_score) as LinearLayout
        if (isStatus) {
            llBestScore!!.visibility = View.GONE
            tvStatusPlay!!.text = "Hight Score"
            tvStatusPlay!!.setTextColor(Color.RED)
            tvUsername!!.setTextColor(Color.GREEN)
            tvUsername!!.textSize = 30f
            tvUsername!!.isSelected = true
            tvScore!!.setTextColor(Color.GREEN)
            tvScore!!.textSize = 40f
            llInfor!!.setBackgroundResource(R.drawable.boder_frame_hight_score)
            imvStatusPlay!!.setImageResource(R.drawable.ic_likee)
        } else {
            llBestScore!!.visibility = View.VISIBLE
            tvStatusPlay!!.text = "Game Over"
            tvStatusPlay!!.setTextColor(Color.WHITE)
            tvUsername!!.setTextColor(Color.GREEN)
            tvUsername!!.textSize = 20f
            tvUsername!!.isSelected = true
            tvScore!!.setTextColor(Color.GREEN)
            tvScore!!.textSize = 30f
            llInfor!!.setBackgroundResource(R.drawable.boder_frame_game_over)
            imvStatusPlay!!.setImageResource(R.drawable.ic_game_over)
            tvBestScore!!.text = readHightScore().toString() + ""
        }
    }

    val value: Unit
        get() {
            val intent = intent
            score = intent.getStringExtra(Const.KEY_SCORE)
            isStatus = intent.getBooleanExtra(Const.KEY_STATUS, false)
        }

    fun readHightScore(): Int {
        val pre = getSharedPreferences(Const.FILE_SAVE_HIGHT_SCORE, MODE_PRIVATE)
        return pre.getInt(Const.KEY_HIGHT_SCORE, -1)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_play_again -> {
                val dataReturn = Intent()
                Log.i(TAG, "Play Again")
                setResult(RESULT_OK, dataReturn)
                finish()
            }
            else -> {
            }
        }
    }

    override fun onBackPressed() {
//        Log.i(TAG, "CANCELED")
//        setResult(RESULT_CANCELED)
//        super.onBackPressed()
    }

    companion object {
        private const val TAG = "GameOverAct"
    }


}
