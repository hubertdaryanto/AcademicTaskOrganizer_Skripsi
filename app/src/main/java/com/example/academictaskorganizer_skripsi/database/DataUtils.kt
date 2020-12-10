package com.example.academictaskorganizer_skripsi.database

import android.content.Context
import com.example.academictaskorganizer_skripsi.services.AlarmScheduler
import kotlinx.coroutines.*
import java.util.concurrent.Executor

object DataUtils {

    fun scheduleAlarmsForData(context: Context) {
        GlobalScope.launch(Dispatchers.IO) {
            val tugasKuliahList = AppDatabase.getInstance(context).getTugasDao.loadAllTugasKuliahUnfinished()
            for (tugasKuliah in tugasKuliahList) {
                AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
            }
        }

    }
}