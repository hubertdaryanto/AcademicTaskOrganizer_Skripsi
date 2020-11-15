package com.example.academictaskorganizer_skripsi.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(tugas::class), version = 1, exportSchema = true)
abstract class TugasDatabase : RoomDatabase() {
    abstract fun getTugasDao(): tugasDatabaseDao

    companion object{
        @Volatile private var instance : TugasDatabase ? = null
        private val LOCK = Any ()
        operator fun invoke (context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase (context:Context) = Room.databaseBuilder(
            context.applicationContext,
            TugasDatabase::class.java,
             " tugasdatabase " ).build()
    }
}

//val db = Room.databaseBuilder(
//    applicationContext,
//    AppDatabase::class.java, "database-name"
//).build()