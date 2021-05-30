package com.wahkor.player.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.wahkor.player.service.MusicService

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != null) {
            val Action = when (intent.action) {
                "MusicPlayerNotificationNEXT" -> "next"
                "MusicPlayerNotificationPREV" -> "previous"
                "MusicPlayerNotificationPLAY" -> "play"
                else -> "nothing"
            }
            val intentMusic=Intent(context,MusicService::class.java)
            intentMusic.putExtra("Action",Action)
            context?.startService(intentMusic)
        }
    }
}