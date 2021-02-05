package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED"))
        {
            TugasKuliahUtils.scheduleAlarmsForTugasKuliah(context)
        }

    }
}