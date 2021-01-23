package com.hubertdaryanto.academicprocrastinationreducer_skripsi

import android.app.Application
import android.app.NotificationManager
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.services.NotificationHelper

class AcademicProcrastinationReducerApp: Application() {
    companion object {
        lateinit var instance: AcademicProcrastinationReducerApp
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        NotificationHelper.createNotificationChannel(this, NotificationManager.IMPORTANCE_HIGH, true,
        getString(R.string.notification_channel_name), "Pengingat Tugas Kuliah")
    }
}