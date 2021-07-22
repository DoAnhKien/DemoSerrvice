package com.example.musicapp.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SongItem(
    val songName: String,
    val path: String,
    val duration: String,
    val author: String,
    val fullName: String
) : Parcelable {
}