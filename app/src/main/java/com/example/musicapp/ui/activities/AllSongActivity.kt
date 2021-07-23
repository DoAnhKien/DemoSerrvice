package com.example.musicapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.adapter.SongAdapter
import com.example.musicapp.base.BaseActivity
import com.example.musicapp.callback.HandleMyOnclick
import com.example.musicapp.databinding.ActivityAllSongBinding
import com.example.musicapp.extension.showShortToast
import com.example.musicapp.media.MediaManager
import com.example.musicapp.model.SongItem

class AllSongActivity : BaseActivity<ActivityAllSongBinding>(), HandleMyOnclick {

    override fun initLayout(): Int = R.layout.activity_all_song

    override fun init() {
        setDataForRecyclerView()

    }

    override fun setOnClickForViews() {

    }

    override fun initViews() {
        changeTheColorOfTheStatusBar()
    }

    private fun getDataFromMainActivity(position: Int) {
        val intent = Intent()
        intent.putExtra("kienda", position)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun setDataForRecyclerView() {
        val songAdapter = SongAdapter(this)
        binding.let { it ->
            it?.rvAllSong.let {
                it?.adapter = songAdapter
                it?.layoutManager = LinearLayoutManager(this)
                it?.setHasFixedSize(true)
            }
        }
        songAdapter.submitList(
            MediaManager(this).getInstance(this)!!
                .getAllSongItemFromStorage()
        )
    }


    private fun changeTheColorOfTheStatusBar() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorImageDark)
    }

    override fun onClick(songItem: SongItem, position: Int) {
        getDataFromMainActivity(position)
    }

    override fun onLongClick(songItem: SongItem, position: Int) {
        showShortToast(songItem.dataPath)
    }

}