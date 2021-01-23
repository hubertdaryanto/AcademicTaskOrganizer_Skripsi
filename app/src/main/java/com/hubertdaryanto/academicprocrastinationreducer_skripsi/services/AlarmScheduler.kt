package com.hubertdaryanto.academicprocrastinationreducer_skripsi.services

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TaskCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TugasKuliah
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

object AlarmScheduler {



    fun scheduleAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
        // get the AlarmManager reference
        if (!tugasKuliah.isFinished)
        {
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent = createPendingIntent(context, tugasKuliah)
            scheduleNotification(tugasKuliah, alarmIntent, alarmMgr)
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

    fun removeAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
        val intent = Intent(context.applicationContext, AlarmReceiver::class.java)
        intent.action = context.getString(R.string.action_notify_tugas_kuliah)
        intent.putExtra(TugasKuliah.KEY_ID, tugasKuliah.tugasKuliahId)

        val alarmIntent = PendingIntent.getBroadcast(context, tugasKuliah.updatedAt.toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmMgr.cancel(alarmIntent)
    }

//    fun updateAlarmsForReminder(context: Context, tugasKuliah: TugasKuliah) {
//        if (!tugasKuliah.isFinished) {
//            AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah)
//            AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
//        } else {
//            AlarmScheduler.removeAlarmsForReminder(context, tugasKuliah)
//        }
//    }
}