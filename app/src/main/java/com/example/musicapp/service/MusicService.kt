package com.example.musicapp.service

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.musicapp.ui.activities.MainActivity
import com.example.musicapp.R
import com.example.musicapp.media.MediaManager
import com.example.musicapp.util.Const
import java.util.*

class MusicService() : Service() {
    private var mediaManager: MediaManager? = null
    private val binder: MusicBinder = MusicBinder()
    private var musicReceiver: MusicReceiver? = null
    private var remoteViews: RemoteViews? = null
    private val TAG = "kienda"
    private var mBuilder: NotificationCompat.Builder? = null
    private var notification: Notification? = null
    private var activity: MainActivity? = null

    fun setActivity(activity: MainActivity?) {
        this.activity = activity
    }

    fun getMediaPlayer(): MediaPlayer? = mediaManager?.getMediaPlayer()


    @SuppressLint("RemoteViewLayout")
    override fun onCreate() {

        super.onCreate()
        remoteViews = RemoteViews(packageName, R.layout.layout_notification_music)
        Log.d(TAG, "onCreate")
        mediaManager = MediaManager(this).getInstance(this)
        musicReceiver = MusicReceiver()
        val filter = IntentFilter()
        filter.addAction(Const.ACTION_NEXT)
        filter.addAction(Const.ACTION_PAUSE)
        filter.addAction(Const.ACTION_PREVIOUS)
        filter.addAction(Const.ACTION_PLAY)
        filter.addAction(Const.ACTION_CANCEL)
        filter.addAction(Const.ACTION_UPDATE_SONG_TITLE)
        filter.addAction(Const.ACTION_PLAY_MUSIC_BY_POSITION)
        registerReceiver(musicReceiver, filter)

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        mediaManager?.play()
        updateTheSongTitle()
        pushNotification()
        Log.d(TAG, "onStartCommand: ")
        return START_STICKY
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun pushNotification() {
        Log.d(TAG, "pushNotification: ")
        val nextIntent = Intent(Const.ACTION_NEXT)
        val nextPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        remoteViews?.setOnClickPendingIntent(R.id.img_next, nextPendingIntent)

        val playIntent = Intent(Const.ACTION_PLAY)
        val playPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            playIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews?.setOnClickPendingIntent(R.id.img_play_paused, playPendingIntent)

        val previousIntent = Intent(Const.ACTION_PREVIOUS)
        val previousPendingIntent = PendingIntent.getBroadcast(
            this,
            2,
            previousIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        remoteViews?.setOnClickPendingIntent(R.id.img_previous, previousPendingIntent)


        val intent = Intent(this, MainActivity::class.java)
        val mainIntent = PendingIntent.getActivity(
            this,
            1, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        mBuilder = NotificationCompat.Builder(this)
//                builder.setContentTitle("Music");
//        builder.setContentText("Song name");
        mBuilder?.setContent(remoteViews)
        mBuilder?.setSmallIcon(R.mipmap.ic_launcher)
        mBuilder?.setContentIntent(mainIntent)

        val channel = NotificationChannel(
            "123", "123",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        mBuilder?.setChannelId(channel.id)

        notification = mBuilder?.build()

//        manager.notify(1, notification);
        startForeground(1, notification)
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        unregisterReceiver(musicReceiver)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class MusicBinder : Binder() {
        val musicService: MusicService
            get() = this@MusicService
    }

    fun getMediaManager(): MediaManager? {
        return mediaManager
    }

    private fun updateTheNotificationIcon() {
        if (mediaManager!!.getMediaPlayer()!!.isPlaying) {
            remoteViews!!.setImageViewResource(
                R.id.img_play_paused,
                R.drawable.ic_play
            )
        } else if (!mediaManager?.getMediaPlayer()!!.isPlaying) {
            remoteViews!!.setImageViewResource(
                R.id.img_play_paused,
                R.drawable.ic_pause
            )
        }
    }

    fun updateTheSongTitle() {
        remoteViews!!.setTextViewText(R.id.tvSongTitle, mediaManager?.getCurrentSong()?.title)
    }

    inner class MusicReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive: ${intent.action}")
            when (intent.action) {
                Const.ACTION_PLAY -> {
                    updateTheNotificationIcon()
                    mediaManager?.playAndPause()
                    updateTheSongTitle()
                    startForeground(1, mBuilder?.build())
                }
                Const.ACTION_PREVIOUS -> {
                    mediaManager?.previous()
                    updateTheNotificationIcon()
                    updateTheSongTitle()
                    startForeground(1, mBuilder?.build())
                }
                Const.ACTION_PAUSE -> {
                    mediaManager?.pause()
                    updateTheSongTitle()
                    startForeground(1, mBuilder?.build())
                }
                Const.ACTION_NEXT -> {
                    mediaManager?.next()
                    updateTheNotificationIcon()
                    updateTheSongTitle()
                    startForeground(1, mBuilder?.build())
                }
                Const.ACTION_CANCEL -> {
                    mediaManager?.cancel()
                    updateTheSongTitle()
                    startForeground(1, mBuilder?.build())
                }
                Const.ACTION_UPDATE_SONG_TITLE -> {

                }
                Const.ACTION_PLAY_MUSIC_BY_POSITION -> {
                    mediaManager?.play()
                    updateTheSongTitle()
                    updateTheNotificationIcon()
                    startForeground(1, mBuilder?.build())
                }
            }
        }
    }


}
