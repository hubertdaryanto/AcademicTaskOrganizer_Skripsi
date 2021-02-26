package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.activity.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object NotificationHelper{
    private val job = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + job)
    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            // 2
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)

            // 3
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun createNotificationForTugasKuliah(context: Context, tugasKuliah: TugasKuliah)
    {
        // create a group notification
        val groupBuilder =
            buildGroupNotification(
                context,
                tugasKuliah
            )

        val notificationBuilder =
            buildNotificationForTugasKuliah(
                context,
                tugasKuliah
            )

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(0, groupBuilder.build())
        notificationManager.notify(tugasKuliah.tugasKuliahId.toInt(), notificationBuilder.build())
    }

    private fun buildGroupNotification(context: Context, tugasKuliah: TugasKuliah): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${context.getString(R.string.notification_channel_name)}"
        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_baseline_access_time_24)
            setContentTitle(tugasKuliah.tugasKuliahName)
            setContentText(context.getString(R.string.group_notification_for))
            setStyle(NotificationCompat.BigTextStyle().bigText(context.getString(R.string.group_notification_for)))
            setAutoCancel(true)
            setGroupSummary(true)
            setGroup(context.getString(R.string.notification_group))
        }
    }

    private fun buildNotificationForTugasKuliah(context: Context, tugasKuliah: TugasKuliah): NotificationCompat.Builder {
        val channelId = "${context.packageName}-${context.getString(R.string.notification_channel_name)}"

        return NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(R.drawable.ic_baseline_access_time_24)
            setContentTitle("Anda menunda untuk menyelesaikan " + tugasKuliah.tugasKuliahName)
            setAutoCancel(false)
            setDefaults(Notification.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_MAX

            // get a drawable reference for the LargeIcon
            val drawable = R.drawable.ic_baseline_access_time_24
            setLargeIcon(BitmapFactory.decodeResource(context.resources, drawable))
            if ((tugasKuliah.deadline - System.currentTimeMillis()) > 0)
            {
                setContentText( ((tugasKuliah.deadline - System.currentTimeMillis()) / 3600000).toString() + " jam tersisa untuk menyelesaikan tugas kuliah ini")
            }
            else
            {
                setContentText(((System.currentTimeMillis() - tugasKuliah.deadline) / 3600000).toString() + " jam terlewati untuk menyelesaikan tugas kuliah ini")
            }

            setGroup(context.getString(R.string.notification_group))



//            uiScope.launch {
//                var tugasKuliahCompletionHistoryDatabase = AppDatabase.getInstance(context).getTugasKuliahCompletionHistoryDao
//                var mTaskCompletionHistory = tugasKuliahCompletionHistoryDatabase.getTugasKuliahCompletionHistoryByTugasKuliahId(tugasKuliah.tugasKuliahId)
//                if (mTaskCompletionHistory == null) {
//                    mTaskCompletionHistory =
//                        TugasKuliahCompletionHistory(
//                            bindToTugasKuliahId = tugasKuliah.tugasKuliahId,
//                            activityType = "Tugas Kuliah Ditunda"
//                        )
//                }
//                tugasKuliahCompletionHistoryDatabase.insertTugasKuliahCompletionHistory(mTaskCompletionHistory)
//            }


            // Launches the app to open the reminder edit screen when tapping the whole notification
            val intent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(TugasKuliah.KEY_ID, tugasKuliah.tugasKuliahId)
            }

            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            setContentIntent(pendingIntent)
        }
    }

}



