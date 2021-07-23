package com.example.musicapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongItem(
    val dataPath: String,
    val title: String,
    val displayName: String,
    val album: String,
    val albumID: String,
    val artist: String,
    val duration: Int = 0,
    val isSelected: Boolean = false,
) : Parcelable {
}