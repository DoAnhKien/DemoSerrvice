package com.example.musicapp.model

data class ItemSong(
     var dataPath: String? = null,
     var title: String? = null,
     var displayName: String? = null,
     var album: String? = null,
     var albumID: String? = null,
     var artist: String? = null,
     val duration: Int = 0,
     val isSelected: Boolean = false,
) {
}