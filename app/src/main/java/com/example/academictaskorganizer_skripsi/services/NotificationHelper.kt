package com.example.academictaskorganizer_skripsi.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.os.Build
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.view.AgendaActivity

class NotificationHelper(base: Context) : ContextWrapper(base) {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var notificationBuilder: Notification.Builder
    val channelId = "com.example.notificationscheduler_test"
    val channelNane = "Pengingat Tugas"

    init {
        createChannels()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }



    fun createChannels()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = NotificationChannel(channelId, channelNane, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel.enableLights(true)
            notificationChannel.enableVibration(true)
            notificationChannel.lightColor = R.color.colorPrimary
            notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
    }

    fun notif(title: String, subtitle: String)
    {
        val intent = Intent(applicationContext, AgendaActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationBuilder = Notification.Builder(this, channelId).setContentTitle(title).setContentText(subtitle).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
        }

        else
        {
            notificationBuilder = Notification.Builder(this).setContentTitle(title).setContentText(subtitle).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationManager.notify(0, notificationBuilder.build())
        }
    }

}

