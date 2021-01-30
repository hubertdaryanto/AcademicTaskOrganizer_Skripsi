package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val job = Job()
        val uiScope = CoroutineScope(Dispatchers.Main + job)
        if (context != null && intent != null && intent.action != null)
        {
            if (intent.action!!.equals(context.getString(R.string.action_notify_tugas_kuliah), ignoreCase = true))  {
                if (intent.extras != null)
                {
                    uiScope.launch {
                        val tugasKuliah = AppDatabase.getInstance(context).getAllQueryListDao.loadTugasKuliahById(
                            intent.extras!!.getLong(TugasKuliah.KEY_ID)
                        )
                        NotificationHelper.createNotificationForTugasKuliah(
                            context,
                            tugasKuliah
                        )
                    }

                }
            }
        }
    }
}