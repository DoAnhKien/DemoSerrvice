package com.example.musicapp.callback

import com.example.musicapp.model.SongItem

interface HandleMyOnclick {
    fun onClick(songItem: SongItem, position: Int)
    fun onLongClick(songItem: SongItem, position: Int)
}