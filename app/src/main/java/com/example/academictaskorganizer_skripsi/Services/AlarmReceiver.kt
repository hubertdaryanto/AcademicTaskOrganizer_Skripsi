package com.example.academictaskorganizer_skripsi.Services

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class AlarmReceiver: BroadcastReceiver()
{
    lateinit var notifHelper: NotificationHelper
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            var alarmIntent: PendingIntent = Intent(context, AlarmReceiver::class.java).let { intent -> PendingIntent.getBroadcast(context,111,intent,0) }
            val seconds = 10000
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + seconds,
                alarmIntent
            )
        }
        notifHelper = NotificationHelper(context)
        notifHelper.notif("Ada tugas yang harus diselesaikan","Ayo segera dikerjakan, biar lebih santai ngerjainnya")
    }
}