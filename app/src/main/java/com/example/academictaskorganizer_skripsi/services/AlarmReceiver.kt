package com.example.academictaskorganizer_skripsi.services

import com.example.academictaskorganizer_skripsi.R
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import kotlinx.coroutines.*


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
                        if (tugasKuliah != null) {
                            NotificationHelper.createNotificationForTugasKuliah(context, tugasKuliah)
                        }
                    }

                }
            }
        }
    }
}