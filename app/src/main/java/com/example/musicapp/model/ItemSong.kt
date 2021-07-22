package com.example.musicapp.model

data class ItemSong(
    private var dataPath: String? = null,
    private var title: String? = null,
    private var displayName: String? = null,
    private var album: String? = null,
    private var albumID: String? = null,
    private var artist: String? = null,
    private val duration: Int = 0,
    private val isSelected: Boolean = false,
) {
}