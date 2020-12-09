package com.example.academictaskorganizer_skripsi.database

import android.content.Context
import com.example.academictaskorganizer_skripsi.services.AlarmScheduler

object DataUtils {
    fun scheduleAlarmsForData(context: Context) {
        val tugasKuliahList = AppDatabase.getInstance(context).getTugasDao.loadAllTugasKuliahUnfinished()
        for (tugasKuliah in tugasKuliahList) {
            AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
        }
    }
}