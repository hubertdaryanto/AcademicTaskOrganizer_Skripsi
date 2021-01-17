package com.example.academictaskorganizer_skripsi.database

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.example.academictaskorganizer_skripsi.services.AlarmScheduler
import kotlinx.coroutines.*


object DataUtils {

    fun scheduleAlarmsForData(context: Context) {
        var job = Job()

        val uiScope = CoroutineScope(Dispatchers.Main + job)

        uiScope.launch {
            val tugasKuliahList = AppDatabase.getInstance(context).getAllQueryListDao.loadAllTugasKuliahUnfinished()
            for (tugasKuliah in tugasKuliahList) {
                AlarmScheduler.scheduleAlarmsForReminder(context, tugasKuliah)
            }
        }

    }
}

object ImageUtils {
    fun openInGallery(context: Context, uri: Uri)
    {

        if (Build.VERSION.SDK_INT in 24..28)
        {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")
            context.startActivity(intent)
        }
        else
        {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            intent.setDataAndType(uri, "image/*")
            context.startActivity(intent)
        }

    }
}