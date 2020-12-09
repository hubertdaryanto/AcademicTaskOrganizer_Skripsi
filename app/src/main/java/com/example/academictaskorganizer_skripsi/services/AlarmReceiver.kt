package com.example.academictaskorganizer_skripsi.services

import com.example.academictaskorganizer_skripsi.R
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.DataUtils
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import java.util.*


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
//        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
        if (context != null && intent != null && intent.action != null)
        {
            if (intent.action == context.getString(R.string.action_notify_tugas_kuliah)) {
                if (intent.extras != null)
                {
                    val tugasKuliah = AppDatabase.getInstance(context).getTugasDao.loadTugasKuliahById(
                        intent.extras!!.getLong("tugasKuliahId")
                    )
                    if (tugasKuliah != null) {
                        NotificationHelper.createNotificationForTugasKuliah(context, tugasKuliah)
                    }
                }

            }
        }


//
//    fun setAlarm(context: Context, time: Long, uri: Uri)
//    {
//
//    }
    }
}

class BootReceiver: BroadcastReceiver()
{
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent?.action.equals("android.intent.action.BOOT_COMPLETED"))
            DataUtils.scheduleAlarmsForData(context)
    }

}
//
//object AlarmManagerProvider {
//    private val TAG = AlarmManagerProvider::class.java.simpleName
//    private var sAlarmManager: AlarmManager? = null
//    @Synchronized
//    fun injectAlarmManager(alarmManager: AlarmManager?) {
//        check(sAlarmManager == null) { "Alarm Manager Already Set" }
//        sAlarmManager = alarmManager
//    }
//
//    /*package*/
//    @Synchronized
//    fun getAlarmManager(context: Context): AlarmManager? {
//        if (sAlarmManager == null) {
//            sAlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        }
//        return sAlarmManager
//    }
//}
//
//class ReminderAlarmService : JobIntentService() {
//    var cursor: Cursor? = null
//
//    private val jobScope = CoroutineScope(Dispatchers.Default)
//
//
//    override fun onHandleWork(intent: Intent) {
//        jobScope.launch {
//            onHandleIntent(intent)
//        }
//    }
//
//    private suspend fun onHandleIntent(intent: Intent?) {
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val uri = intent!!.data
//
//        //Display a notification to view the task details
//        val action = Intent(this, AgendaActivity::class.java)
//        action.data = uri
//        val operation: PendingIntent = TaskStackBuilder.create(this)
//            .addNextIntentWithParentStack(action)
//            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
//
//        //Grab the task description
//        if (uri != null) {
//            cursor = contentResolver.query(uri, null, null, null, null)
//        }
//        var description = ""
//        try {
//            if (cursor != null && cursor!!.moveToFirst()) {
////                description = AlarmReminderContract.getColumnString(
////                    cursor,
////                    AlarmReminderContract.AlarmReminderEntry.KEY_TITLE
////                )
//
//                var title = AppDatabase.getInstance(applicationContext).getTugasDao.loadAllTugasKuliahName()
//            }
//        } finally {
//            cursor?.close()
//        }
//        val note = buildNotification("Test", "123")
//        manager.notify(NOTIFICATION_ID, note)
//    }
//
//    private fun buildNotification(title: String, subtitle: String): android.app.Notification
//    {
//        val note: Notification = Notification.Builder(this)
//            .setContentTitle(getString(R.string.reminder_title))
//            .setContentText(description)
//            .setSmallIcon(R.drawable.ic_add_alert_black_24dp)
//            .setContentIntent(operation)
//            .setAutoCancel(true)
//            .build()
//        return note
//    }
//
//    companion object {
////        private val TAG = ReminderAlarmService::class.java.simpleName
//        private const val NOTIFICATION_ID = 42
//
//        //This is a deep link intent, and needs the task stack
//        fun getReminderPendingIntent(context: Context?, uri: Uri?): PendingIntent {
//            val action = Intent(context, ReminderAlarmService::class.java)
//            action.data = uri
//            return PendingIntent.getService(context, 0, action, PendingIntent.FLAG_UPDATE_CURRENT)
//        }
//
//        fun enqueueWork(context: Context, intent: Intent) {
//            enqueueWork(context, ReminderAlarmService::class.java, 1, intent)
//        }
//    }
//}

//class AlarmScheduler {
//    /**
//     * Schedule a reminder alarm at the specified time for the given task.
//     *
//     * @param context Local application or activity context
//     * @param reminderTask Uri referencing the task in the content provider
//     */
//    fun setAlarm(context: Context?, alarmTime: Long, reminderTask: Uri?) {
//        val manager = getAlarmManager(context!!)
//        val operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask)
//        if (Build.VERSION.SDK_INT >= 23) {
//            manager!!.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, operation)
//        } else if (Build.VERSION.SDK_INT >= 19) {
//            manager!!.setExact(AlarmManager.RTC_WAKEUP, alarmTime, operation)
//        } else {
//            manager!![AlarmManager.RTC_WAKEUP, alarmTime] = operation
//        }
//    }
//
//    fun setRepeatAlarm(context: Context?, alarmTime: Long, reminderTask: Uri?, RepeatTime: Long) {
//        val manager = getAlarmManager(context!!)
//        val operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask)
//        manager!!.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime, RepeatTime, operation)
//    }
//
//    fun cancelAlarm(context: Context?, reminderTask: Uri?) {
//        val manager = getAlarmManager(context!!)
//        val operation = ReminderAlarmService.getReminderPendingIntent(context, reminderTask)
//        manager!!.cancel(operation)
//    }
//}

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
                action = context.getString(R.string.status_bar_notification_info_overflow)
                type = "${tugasKuliah.tugasKuliahName}-${tugasKuliah.notes}"
                putExtra("tugasKuliahId", tugasKuliah.tugasKuliahId)
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
    }