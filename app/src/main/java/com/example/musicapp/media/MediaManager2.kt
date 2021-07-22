//package com.example.musicapp.media
//
//import android.Manifest
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.media.MediaPlayer
//import android.provider.MediaStore
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import com.example.musicapp.model.ItemSong
//import com.example.musicapp.util.Const
//import java.io.IOException
//import java.util.*
//
//class MediaManager2 {
//    private val PERMISION_ALL = 265
//    private val TAG = "MediaManager"
//    private var mediaManager: MediaManager? = null
//    private var context: Context? = null
//
//    //media player object
//    private var mPlayer: MediaPlayer? = null
//    private var arrItemSongPlay: ArrayList<ItemSong> = ArrayList<ItemSong>()
//
//    //this is current song play.
//    private var currentIndex = 0
//
//    //this is state of mediaplayer
//    private var mediaState: Int = Const.MEDIA_IDLE
//    private var loop: Int = Const.MEDIA_STATE_LOOP_ALL
//    private val random = Random()
//    private var shuffle: Boolean = Const.MEDIA_SHUFFLE_TRUE
//
//    private fun MediaManager(context: Context) {
//        this.context = context
//        initPlayer()
//    }
//
//    fun getInstance(context: Context): MediaManager? {
//        if (mediaManager == null) {
//            mediaManager = MediaManager(context)
//        }
//        return mediaManager
//    }
//
//    private fun initPlayer() {
//        mPlayer = MediaPlayer()
//        loop = SharePreferencesController.getInstance(context)
//            .getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_LOOP_ALL)
//        shuffle = SharePreferencesController.getInstance(context)
//            .getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
//        mPlayer!!.setOnCompletionListener { next() }
//    }
//
//    fun play(isPlayAgain: Boolean) {
//        if (mediaState == Const.MEDIA_IDLE || mediaState == Const.MEDIA_STOP || isPlayAgain) {
//            try {
//                mPlayer!!.reset()
//                val song: ItemSong = arrItemSongPlay[currentIndex]
//                mPlayer.setDataSource(song.getDataPath())
//                mPlayer!!.prepare()
//                mPlayer!!.start()
//                val intent = Intent(Const.ACTION_SEND_DATA)
//                intent.putExtra(Const.KEY_TITLE_SONG, song.getDisplayName())
//                intent.putExtra(Const.KEY_NAME_ARTIST, song.getArtist())
//                context!!.sendBroadcast(intent)
//                mediaState = Const.MEDIA_PLAYING
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        } else if (mediaState == Const.MEDIA_PAUSE) {
//            mPlayer!!.start()
//            mediaState = Const.MEDIA_PLAYING
//        } else if (mediaState == Const.MEDIA_PLAYING) {
//            mPlayer!!.pause()
//            mediaState = Const.MEDIA_PAUSE
//        }
//    }
//
//    operator fun next() {
//        val sharePreference: SharePreferencesController =
//            SharePreferencesController.getInstance(context)
//        loop = sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)
//        shuffle = sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
//        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
//            if (shuffle) {
//                currentIndex = random.nextInt(arrItemSongPlay.size)
//            } else {
//                if (currentIndex > arrItemSongPlay.size - 2) {
//                    currentIndex = 0
//                } else {
//                    currentIndex++
//                }
//            }
//        }
//        play(true)
//    }
//
//    fun previous() {
//        val sharePreference: SharePreferencesController =
//            SharePreferencesController.getInstance(context)
//        loop = sharePreference.getInt(Const.MEDIA_CURRENT_STATE_LOOP, Const.MEDIA_STATE_NO_LOOP)
//        shuffle = sharePreference.getBoolean(Const.MEDIA_SHUFFLE, Const.MEDIA_SHUFFLE_TRUE)
//        if (loop != Const.MEDIA_STATE_LOOP_ONE) {
//            if (shuffle) {
//                currentIndex = random.nextInt(arrItemSongPlay.size)
//            } else {
//                if (currentIndex <= 0) {
//                    currentIndex = arrItemSongPlay.size - 1
//                } else {
//                    currentIndex--
//                }
//            }
//        }
//        play(true)
//    }
//
//    fun stop() {
//        if (mediaState == Const.MEDIA_IDLE) {
//            return
//        }
//        mPlayer!!.stop()
//        mediaState = Const.MEDIA_STOP
//    }
//
//    fun getArrItemSong(): ArrayList<ItemSong>? {
//        return arrItemSongPlay
//    }
//
//    fun setArrItemSong(arrItemSong: ArrayList<ItemSong>) {
//        arrItemSongPlay = arrItemSong
//    }
//
//    fun setCurrentIndex(currentIndex: Int) {
//        this.currentIndex = currentIndex
//    }
//
//    fun getCurrentIndex(): Int {
//        return currentIndex
//    }
//
//    fun isShuffle(): Boolean {
//        return shuffle
//    }
//
//    fun getLoop(): Int {
//        return loop
//    }
//
//    fun getmPlayer(): MediaPlayer? {
//        return mPlayer
//    }
//
//    fun getSongList(filed: String?, value: Array<String?>?): ArrayList<ItemSong>? {
//        val arrItemSong: ArrayList<ItemSong> = ArrayList<ItemSong>()
//        val columsName = arrayOf(
//            MediaStore.Audio.Media.DATA,
//            MediaStore.Audio.Media.TITLE,
//            MediaStore.Audio.Media.DISPLAY_NAME,
//            MediaStore.Audio.Media.DURATION,
//            MediaStore.Audio.Media.ALBUM,
//            MediaStore.Audio.Media.ALBUM_ID,
//            MediaStore.Audio.Media.ARTIST
//        )
//        if (ActivityCompat.checkSelfPermission(
//                context!!,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            val PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//            ActivityCompat.requestPermissions((context as Activity?)!!, PERMISSION, PERMISION_ALL)
//        } else {
//            val cursor = context!!.contentResolver
//                .query(
//                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                    columsName, filed, value, null, null
//                ) ?: return null
//            val indexData = cursor.getColumnIndex(columsName[0])
//            val indexTitle = cursor.getColumnIndex(columsName[1])
//            val indexDisPlay = cursor.getColumnIndex(columsName[2])
//            val indexDuration = cursor.getColumnIndex(columsName[3])
//            val indexAlbum = cursor.getColumnIndex(columsName[4])
//            val indexAlbumId = cursor.getColumnIndex(columsName[5])
//            val indexArtist = cursor.getColumnIndex(columsName[6])
//            var data: String?
//            var title: String?
//            var display: String
//            var album: String?
//            var albumID: String?
//            var artist: String?
//            var duration: Int
//            cursor.moveToFirst()
//            arrItemSong.clear()
//            while (!cursor.isAfterLast) {
//                data = cursor.getString(indexData)
//                title = cursor.getString(indexTitle)
//                display = cursor.getString(indexDisPlay)
//                album = cursor.getString(indexAlbum)
//                albumID = cursor.getString(indexAlbumId)
//                artist = cursor.getString(indexArtist)
//                duration = cursor.getInt(indexDuration)
//                val extension = display.substring(display.lastIndexOf("."))
//                if (extension.equals(".mp3", ignoreCase = true)) {
//                    arrItemSong.add(
//                        ItemSong(
//                            data,
//                            title,
//                            display,
//                            album,
//                            albumID,
//                            artist,
//                            duration,
//                            false
//                        )
//                    )
//                }
//                Log.i(TAG, "extension:= $extension")
//                cursor.moveToNext()
//            }
//            cursor.close()
//        }
//        return arrItemSong
//    }
//
//    fun getAllAlbums(filed: String?, value: Array<String?>?): ArrayList<ItemAlbums>? {
//        val arrItemAlbums: ArrayList<ItemAlbums> = ArrayList<ItemAlbums>()
//        val columsName = arrayOf(
//            MediaStore.Audio.Albums._ID,
//            MediaStore.Audio.Albums.ALBUM,
//            MediaStore.Audio.Albums.ARTIST,
//            MediaStore.Audio.Albums.ALBUM_ART,
//            MediaStore.Audio.Albums.NUMBER_OF_SONGS
//        )
//        if (ActivityCompat.checkSelfPermission(
//                context!!,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            val PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//            ActivityCompat.requestPermissions((context as Activity?)!!, PERMISSION, PERMISION_ALL)
//        } else {
//            val cursor = context!!.contentResolver
//                .query(
//                    MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                    columsName, filed, value, null
//                ) ?: return null
//            val indexAlbumID = cursor.getColumnIndex(columsName[0])
//            val indexAlbum = cursor.getColumnIndex(columsName[1])
//            val indexAlbumArtist = cursor.getColumnIndex(columsName[2])
//            val indexAlbumArt = cursor.getColumnIndex(columsName[3])
//            val indexNumofSongs = cursor.getColumnIndex(columsName[4])
//            var albumName: String?
//            var albumArtist: String?
//            var albumArt: String?
//            var numofSongs: Int
//            var albumID: Int
//            cursor.moveToFirst()
//            arrItemAlbums.clear()
//            while (!cursor.isAfterLast) {
//                albumID = cursor.getInt(indexAlbumID)
//                albumName = cursor.getString(indexAlbum)
//                albumArtist = cursor.getString(indexAlbumArtist)
//                albumArt = cursor.getString(indexAlbumArt)
//                numofSongs = cursor.getInt(indexNumofSongs)
//                arrItemAlbums.add(
//                    ItemAlbums(
//                        albumID, albumName,
//                        albumArtist, albumArt, numofSongs
//                    )
//                )
//                cursor.moveToNext()
//            }
//            cursor.close()
//        }
//        return arrItemAlbums
//    }
//
//    fun getAllArtist(): ArrayList<ItemArtist>? {
//        val arrItemArtist: ArrayList<ItemArtist> = ArrayList<ItemArtist>()
//        val columsName = arrayOf(
//            MediaStore.Audio.Artists._ID,
//            MediaStore.Audio.Artists.ARTIST,
//            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
//            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
//        )
//        if (ActivityCompat.checkSelfPermission(
//                context!!,
//                Manifest.permission.READ_EXTERNAL_STORAGE
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            val PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
//            ActivityCompat.requestPermissions((context as Activity?)!!, PERMISSION, PERMISION_ALL)
//        } else {
//            val cursor = context!!.contentResolver
//                .query(
//                    MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
//                    columsName, null, null, null
//                ) ?: return null
//            val indexArtistID = cursor.getColumnIndex(columsName[0])
//            val indexArtist = cursor.getColumnIndex(columsName[1])
//            val indexNumOfAlbums = cursor.getColumnIndex(columsName[2])
//            val indexNumOfTracks = cursor.getColumnIndex(columsName[3])
//            var artistName: String?
//            var idArtist: Int
//            var numOfAlbums: Int
//            var numofTracks: Int
//            cursor.moveToFirst()
//            arrItemArtist.clear()
//            while (!cursor.isAfterLast) {
//                idArtist = cursor.getInt(indexArtistID)
//                artistName = cursor.getString(indexArtist)
//                numOfAlbums = cursor.getInt(indexNumOfAlbums)
//                numofTracks = cursor.getInt(indexNumOfTracks)
//                arrItemArtist.add(ItemArtist(idArtist, artistName, numOfAlbums, numofTracks))
//                cursor.moveToNext()
//            }
//            cursor.close()
//        }
//        return arrItemArtist
//    }
//}