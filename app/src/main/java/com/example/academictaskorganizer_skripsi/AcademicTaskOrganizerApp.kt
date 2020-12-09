package com.example.academictaskorganizer_skripsi

import android.app.Application
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

        NotificationHelper.createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
        "Reminder for Tugas Kuliah", "Tugas Kuliah Reminder")
    }
}