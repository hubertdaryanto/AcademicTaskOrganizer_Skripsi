package com.example.academictaskorganizer_skripsi

import android.app.Application
import android.app.NotificationManager
import androidx.core.app.NotificationManagerCompat
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.services.NotificationHelper

class AcademicTaskOrganizerApp: Application() {
    companion object {
        lateinit var instance: AcademicTaskOrganizerApp
        private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        NotificationHelper.createNotificationChannel(this, NotificationManager.IMPORTANCE_HIGH, true,
        getString(R.string.notification_channel_name), "Pengingat Tugas Kuliah")
    }
}