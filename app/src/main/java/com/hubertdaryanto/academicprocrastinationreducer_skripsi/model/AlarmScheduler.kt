package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R

object AlarmScheduler {



    fun scheduleAlarmsForTugasKuliahReminder(context: Context, tugasKuliah: TugasKuliah) {
        // get the AlarmManager reference
        if (!tugasKuliah.isFinished)
        {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent =
                createPendingIntent(
                    context,
                    tugasKuliah
                )
            scheduleNotification(
                tugasKuliah,
                alarmIntent,
                alarmMgr
            )
        }
    }

    private fun createPendingIntent(
        context: Context,
        tugasKuliah: TugasKuliah
    ): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_notify_tugas_kuliah)
//            type = "${tugasKuliah.tugasKuliahName}-${tugasKuliah.notes}"
            putExtra(TugasKuliah.KEY_ID, tugasKuliah.tugasKuliahId)
        }
        return PendingIntent.getBroadcast(context, tugasKuliah.updatedAt.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    /**
     * Schedules a single alarm
     */
    private fun scheduleNotification(
        tugasKuliah: TugasKuliah,
        alarmIntent: PendingIntent?,
        alarmMgr: AlarmManager
    ) {
        alarmMgr.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            tugasKuliah.finishCommitment,
            alarmIntent
        )
    }

    fun removeAlarmsForTugasKuliahReminder(context: Context, tugasKuliah: TugasKuliah) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_tugas_kuliah)
        intent.putExtra(TugasKuliah.KEY_ID, tugasKuliah.tugasKuliahId)

        val alarmIntent = PendingIntent.getBroadcast(context, tugasKuliah.updatedAt.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(alarmIntent)
    }
}