package com.wahkor.player.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.wahkor.player.ActionPlaying
import com.wahkor.player.MainActivity

class MusicService : Service() {
    private val actionNext = "next"
    private val actionPrev = "previous"
    private val actionPlay = "play"
    private var mBinder: IBinder = MyBinder()
    private lateinit var actionPlaying:MainActivity
    override fun onBind(intent: Intent?): IBinder? {
        Log.e("Bind", "Method")
        return mBinder
    }

    inner class MyBinder : Binder() {
        val getService: MusicService
            get() {
                return this@MusicService
            }

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action=intent?.getStringExtra("Action")
        if (action != null){
            Log.e("onStart",action)
            when(action){
                actionPlay -> actionPlaying?.playClicked()
                actionPrev -> actionPlaying?.prevClicked()
                actionNext ->actionPlaying?.nextClicked()
            }

        }
        return START_STICKY
    }
    fun setCallBack(actionPlaying:MainActivity){
        this.actionPlaying=actionPlaying

    }
}