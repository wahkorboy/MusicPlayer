package com.wahkor.player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class ApplicationClass: Application() {
    val CHANNEL_ID_1="MusicPlayerNotification1"
    val CHANNEL_ID_2="MusicPlayerNotification2"
    val ACTION_NEXT="MusicPlayerNotificationNEXT"
    val ACTION_PREV="MusicPlayerNotificationPREV"
    val ACTION_PLAY="MusicPlayerNotificationPLAY"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel1=  NotificationChannel(CHANNEL_ID_1,
                "Channel(1)",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel1.description="CH1 Description"
            val notificationChannel2=  NotificationChannel(CHANNEL_ID_2,
                "Channel(2)",NotificationManager.IMPORTANCE_HIGH)
            notificationChannel2.description="CH2 Description"
            val notificationManager=getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel1)
            notificationManager.createNotificationChannel(notificationChannel2)

        }
    }
}