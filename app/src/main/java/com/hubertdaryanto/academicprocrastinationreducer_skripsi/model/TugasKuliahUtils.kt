package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object TugasKuliahUtils {
    fun scheduleAlarmsForTugasKuliah(context: Context) {
        var job = Job()

        val uiScope = CoroutineScope(Dispatchers.Main + job)

        uiScope.launch {
            val tugasKuliahList = AppDatabase.getInstance(
                context
            ).getTugasKuliahDao.loadAllTugasKuliahUnfinished()
            for (tugasKuliah in tugasKuliahList) {
                AlarmScheduler.scheduleAlarmsForTugasKuliahReminder(
                    context,
                    tugasKuliah
                )
            }
        }
    }
}