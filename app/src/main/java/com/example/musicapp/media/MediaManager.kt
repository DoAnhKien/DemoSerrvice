package com.example.musicapp.media

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.example.musicapp.model.SongItem
import java.io.IOException
import java.util.*

class MediaManager(private val context: Context) {
    private var playState = STATE_STOP
    private var mPlayer: MediaPlayer? = null
    private val arrSongs: MutableList<SongItem> = ArrayList<SongItem>()
    private var currentIndex = 0
    private var currentSong: SongItem? = null
    private var mediaManager: MediaManager? = null
    private var isLoop: Boolean = false


    fun isLoop(): Boolean {
        return isLoop
    }

    fun setLoop(isLoop: Boolean) {
        this.isLoop = isLoop
    }


    fun getInstance(context: Context): MediaManager? {
        if (mediaManager == null) {
            mediaManager = MediaManager(context)
        }
        return mediaManager
    }

    fun setCurrentIndex(position: Int) {
        currentIndex = position
    }

    init {
        Log.d(TAG, "kienda: ")
        getAllAudioFilesExternal()
        initPlayer()
        Log.d(TAG, "mmm ${arrSongs.size}")
    }

    fun getSongList(): MutableList<SongItem> {
        return arrSongs
    }

    companion object {
        private const val STATE_STOP = -1
        private const val STATE_PLAYING = 0
        private const val STATE_PAUSE = 1
        private const val TAG = "kienda"
    }

    private fun initPlayer() {
        mPlayer = MediaPlayer()
        mPlayer!!.setOnPreparedListener {
            mPlayer!!.start()
            Log.i(TAG, "onPrepared...")
        }
        mPlayer!!.setOnErrorListener { mp, what, extra ->
            Log.e(TAG, "onError...")
            playState = STATE_STOP
            false
        }
        mPlayer!!.setOnCompletionListener {
            if (isLoop) {
                Log.d(TAG, "initPlayer:  true")
                play()
            } else if (!isLoop) {
                Log.d(TAG, "initPlayer:  false")
                currentIndex++
                play()
            }

        }
    }

    fun playAndPause() {
        if (mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            return
        }
        mPlayer!!.start()
    }

    fun play() {
        try {
            mPlayer!!.reset()
            mPlayer!!.setDataSource(context, Uri.parse(arrSongs[currentIndex].dataPath))
            mPlayer!!.prepare()
            playState = STATE_PLAYING
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    operator fun next() {
        if (currentIndex >= arrSongs.size - 1) {
            currentIndex = 0
        } else {
            currentIndex++
        }
        play()
    }

    fun previous() {
        if (currentIndex <= 0) {
            currentIndex = arrSongs.size - 1
        } else {
            currentIndex--
        }
        play()
    }

    fun pause() {
        mPlayer!!.pause()
        playState = STATE_PAUSE
    }

    fun cancel() {
        mPlayer!!.stop();
        mPlayer!!.release()
    }

    fun seek(seekTo: Int) {
        mPlayer!!.seekTo(seekTo)
        //        try {
//            mPlayer.prepare();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    fun getMediaPlayer(): MediaPlayer? = mPlayer


    private fun getAllAudioFilesExternal() {
        val columnsName = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor = context.contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null, null
            )
        val indexData = cursor!!.getColumnIndex(columnsName[0])
        val indexTitle = cursor!!.getColumnIndex(columnsName[1])
        val indexDisPlay = cursor!!.getColumnIndex(columnsName[2])
        val indexDuration = cursor!!.getColumnIndex(columnsName[3])
        val indexAlbum = cursor!!.getColumnIndex(columnsName[4])
        val indexAlbumId = cursor!!.getColumnIndex(columnsName[5])
        val indexArtist = cursor!!.getColumnIndex(columnsName[6])
        var data: String?
        var title: String?
        var display: String
        var album: String?
        var albumID: String?
        var artist: String?
        var duration: Int
        cursor?.moveToFirst()
        arrSongs.clear()
        while (!cursor!!.isAfterLast) {
            data = cursor.getString(indexData)
            title = cursor.getString(indexTitle)
            display = cursor.getString(indexDisPlay)
            album = cursor.getString(indexAlbum)
            albumID = cursor.getString(indexAlbumId)
            artist = cursor.getString(indexArtist)
            duration = cursor.getInt(indexDuration)
            val extension = display.substring(display.lastIndexOf("."))
            if (extension.equals(".mp3", ignoreCase = true)) {
                arrSongs.add(
                    SongItem(
                        data,
                        title,
                        display,
                        album,
                        albumID,
                        artist,
                        duration,
                        false
                    )
                )
            }
            Log.i(TAG, "extension:= $extension")
            cursor.moveToNext()
        }
        cursor.close()
    }

    fun getAllSongItemFromStorage(): MutableList<SongItem>? {
        val mArrSongs: MutableList<SongItem> = ArrayList<SongItem>()
        val columnsName = arrayOf(
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.ARTIST
        )
        val cursor = context.contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, null, null
            )
        val indexData = cursor!!.getColumnIndex(columnsName[0])
        val indexTitle = cursor!!.getColumnIndex(columnsName[1])
        val indexDisPlay = cursor!!.getColumnIndex(columnsName[2])
        val indexDuration = cursor!!.getColumnIndex(columnsName[3])
        val indexAlbum = cursor!!.getColumnIndex(columnsName[4])
        val indexAlbumId = cursor!!.getColumnIndex(columnsName[5])
        val indexArtist = cursor!!.getColumnIndex(columnsName[6])
        var data: String?
        var title: String?
        var display: String
        var album: String?
        var albumID: String?
        var artist: String?
        var duration: Int
        cursor?.moveToFirst()
        mArrSongs.clear()
        while (!cursor!!.isAfterLast) {
            data = cursor.getString(indexData)
            title = cursor.getString(indexTitle)
            display = cursor.getString(indexDisPlay)
            album = cursor.getString(indexAlbum)
            albumID = cursor.getString(indexAlbumId)
            artist = cursor.getString(indexArtist)
            duration = cursor.getInt(indexDuration)
            val extension = display.substring(display.lastIndexOf("."))
            if (extension.equals(".mp3", ignoreCase = true)) {
                mArrSongs.add(
                    SongItem(
                        data,
                        title,
                        display,
                        album,
                        albumID,
                        artist,
                        duration,
                        false
                    )
                )
            }
            Log.i(TAG, "extension:= $extension")
            cursor.moveToNext()
        }
        cursor.close()
        return mArrSongs
    }

    fun getCurrentSong(): SongItem = arrSongs[currentIndex]


}
