package com.example.academictaskorganizer_skripsi.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.academictaskorganizer_skripsi.R
import com.example.academictaskorganizer_skripsi.database.TugasKuliah

object AlarmScheduler {

    fun scheduleAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
        // get the AlarmManager reference
        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = createPendingIntent(context, tugasKuliah)
        scheduleNotification(tugasKuliah, alarmIntent, alarmMgr)
    }

    private fun createPendingIntent(
        context: Context,
        tugasKuliah: TugasKuliah
    ): PendingIntent? {
        // create the intent using a unique type
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java).apply {
            action = context.getString(R.string.action_notify_tugas_kuliah)
//            type = "${tugasKuliah.tugasKuliahName}-${tugasKuliah.notes}"
            putExtra(context.getString(R.string.get_tugas_kuliah_id_key), tugasKuliah.tugasKuliahId)
        }
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
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
            tugasKuliah.deadline,
            alarmIntent
        )
    }

    fun removeAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_tugas_kuliah)
        intent.putExtra("tugasKuliahId", tugasKuliah.tugasKuliahId)

        val alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(alarmIntent)
    }

    fun updateAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
        if (!tugasKuliah.isFinished) {
            AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
        } else {
            AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah)
        }
    }
}