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

    init {
        Log.d(TAG, "kienda: ")
        getAllAudioFilesExternal()
        initPlayer()
        Log.d(TAG, "mmm ${arrSongs.size}")
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
            mPlayer!!.setDataSource(context, Uri.parse(arrSongs[currentIndex].path))
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
        playState = if (mPlayer!!.isPlaying) {
            mPlayer!!.pause()
            STATE_PAUSE
        } else {
            mPlayer!!.start()
            STATE_PLAYING
        }
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
            MediaStore.Audio.AudioColumns.DATA,
            MediaStore.Audio.AudioColumns.DISPLAY_NAME,
            MediaStore.Audio.AudioColumns.DURATION,
            MediaStore.Audio.AudioColumns.ARTIST,
            MediaStore.Audio.AudioColumns.TITLE
        )
        val c = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            columnsName,
            null,
            null,
            null,
            null
        )
        c!!.moveToFirst()

        arrSongs.clear()
        val pathIndex = c!!.getColumnIndex(columnsName[0])
        val fullName = c!!.getColumnIndex(columnsName[1])
        val durationIndex = c!!.getColumnIndex(columnsName[2])
        val authorIndex = c!!.getColumnIndex(columnsName[3])
        val songNameIndex = c!!.getColumnIndex(columnsName[4])

        while (!c!!.isAfterLast) {
            val item = SongItem(
                c!!.getString(songNameIndex),
                c!!.getString(pathIndex),
                c!!.getString(durationIndex),
                c!!.getString(authorIndex),
                c!!.getString(fullName)
            )
            arrSongs.add(item)
            c!!.moveToNext()
        }
        c!!.close()
    }

    fun getCurrentSong(): SongItem = arrSongs[currentIndex]


}
