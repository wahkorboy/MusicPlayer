package com.wahkor.player

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.MediaPlayer
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.NotificationCompat
import com.wahkor.player.model.TrackFile
import com.wahkor.player.receiver.NotificationReceiver
import com.wahkor.player.service.MusicService

class MainActivity : AppCompatActivity(), ActionPlaying, ServiceConnection {
    val CHANNEL_ID_1 = "MusicPlayerNotification1"
    val CHANNEL_ID_2 = "MusicPlayerNotification2"
    val ACTION_NEXT = "MusicPlayerNotificationNEXT"
    val ACTION_PREV = "MusicPlayerNotificationPREV"
    val ACTION_PLAY = "MusicPlayerNotificationPLAY"
    private lateinit var nextIMG: ImageView
    private lateinit var playIMG: ImageView
    private lateinit var prevIMG: ImageView;
    private lateinit var titleView: TextView
    private var listTrackFile = ArrayList<TrackFile>()
    var position = 0
    var isPlaying = false
    private lateinit var mediaPlayer: MediaPlayer
    private var musicService: MusicService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mediaPlayer=MediaPlayer.create(this,R.raw.touch_my_body_sistar)
        listTrackFile.add(TrackFile("hello0", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello1", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello2", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello3", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello4", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello5", "Taylor", R.drawable.thumbnail))
        listTrackFile.add(TrackFile("hello6", "Taylor", R.drawable.thumbnail))

        nextIMG = findViewById(R.id.mainNextImageView)
        playIMG = findViewById(R.id.mainPlayImageView)
        prevIMG = findViewById(R.id.mainPreviousImageView)
        titleView = findViewById(R.id.mainTitleTextview)
        titleView.text = listTrackFile[position].title
        nextIMG.setOnClickListener {
            nextClicked()
            Log.e("Play ${listTrackFile[position].title}", "$isPlaying")
        }
        playIMG.setOnClickListener {
            playClicked()
            Log.e("Play ${listTrackFile[position].title}", "$isPlaying")
        }
        prevIMG.setOnClickListener {
            prevClicked()
            Log.e("Play ${listTrackFile[position].title}", "$isPlaying")
        }

        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)


    }

    fun showNotification(playButton: Int) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val prevIntent = Intent(this, NotificationReceiver::class.java).setAction(ACTION_PREV)
        val prevPendingIntent =
            PendingIntent.getBroadcast(this, 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val playIntent = Intent(this, NotificationReceiver::class.java).setAction(ACTION_PLAY)
        val playPendingIntent =
            PendingIntent.getBroadcast(this, 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val nextIntent = Intent(this, NotificationReceiver::class.java).setAction(ACTION_NEXT)
        val nextPendingIntent =
            PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val picture = BitmapFactory.decodeResource(resources, listTrackFile[position].thumbnail!!)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID_2)
            .setSmallIcon(listTrackFile[position].thumbnail!!)
            .setLargeIcon(picture)
            .setContentTitle(listTrackFile[position].title)
            .setContentText(listTrackFile[position].singer)
            .addAction(R.drawable.ic_baseline_skip_previous_24, "Prev", prevPendingIntent)
            .addAction(playButton, "Play", playPendingIntent)
            .addAction(R.drawable.ic_baseline_skip_next_24, "Next", nextPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .setOnlyAlertOnce(true)
            .build();
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notification)
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, this, BIND_AUTO_CREATE)

    }

    override fun onPause() {
        super.onPause()
        unbindService(this)
    }

    override fun nextClicked() {
        position = if (position == listTrackFile.size - 1) 0 else position + 1
        titleView.text = listTrackFile[position].title
        showNotification(R.drawable.ic_baseline_pause)
    }

    override fun prevClicked() {
        position = if (position == 0) listTrackFile.size - 1 else position - 1
        titleView.text = listTrackFile[position].title
        showNotification(R.drawable.ic_baseline_pause)
    }

    override fun playClicked() {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            playIMG.setImageResource(R.drawable.ic_baseline_play)
            showNotification(R.drawable.ic_baseline_play)
        } else {
            mediaPlayer.start()
            isPlaying = true
            playIMG.setImageResource(R.drawable.ic_baseline_pause)
            showNotification(R.drawable.ic_baseline_pause)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        val binder = service as MusicService.MyBinder
        musicService = binder.getService
        musicService!!.setCallBack(this)
        Log.e("Connected", "musicService")

    }

    override fun onServiceDisconnected(name: ComponentName?) {
        musicService = null
        Log.e("Disconnected", "musicService")
    }
}