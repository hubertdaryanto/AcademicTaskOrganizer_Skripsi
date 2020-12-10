package com.example.academictaskorganizer_skripsi.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.academictaskorganizer_skripsi.database.DataUtils

class BootReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED"))
            DataUtils.scheduleAlarmsForData(context)
    }
}