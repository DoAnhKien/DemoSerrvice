package com.example.musicapp.ui.fragments

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicapp.R
import com.example.musicapp.adapter.SongAdapter
import com.example.musicapp.adapter.TotalMusicAdapter
import com.example.musicapp.base.BaseFragment
import com.example.musicapp.callback.HandleMyOnclick
import com.example.musicapp.databinding.FragmentSongBinding
import com.example.musicapp.extension.showShortToast
import com.example.musicapp.media.MediaManager
import com.example.musicapp.model.SongItem
import com.example.musicapp.ui.activities.MainMusicActivity
import kotlinx.android.synthetic.main.fragment_song.*


class SongFragment : BaseFragment<FragmentSongBinding>(), HandleMyOnclick {

    companion object {
        fun newInstance() = SongFragment()
    }


    override fun initLayout(): Int = R.layout.fragment_song

    override fun init() {
        setDataForRecyclerView()

    }

    override fun initViews() {
        Log.d(
            "mmm",
            "initViews1: ${
                if (context != null)
                    MediaManager(requireActivity()!!.applicationContext).getInstance(requireActivity().applicationContext)!!
                        .getAllSongItemFromStorage()!!.size
                else return
            }"
        )
    }

    private fun setDataForRecyclerView() {
        val songAdapter = SongAdapter(this)
        layoutBinding.let { it ->
            it?.rvSong.let {
                it?.adapter = songAdapter
                it?.layoutManager = LinearLayoutManager(requireContext())
                it?.setHasFixedSize(true)
            }
        }
        songAdapter.submitList(
            MediaManager(requireActivity()!!.applicationContext).getInstance(requireActivity().applicationContext)!!
                .getAllSongItemFromStorage()
        )
    }

    override fun setOnClickForViews() {

    }

    override fun onClick(songItem: SongItem, position: Int) {
        activity?.showShortToast(songItem.path)
    }

    override fun onLongClick(songItem: SongItem, position: Int) {
        activity?.showShortToast(songItem.path)
    }
}