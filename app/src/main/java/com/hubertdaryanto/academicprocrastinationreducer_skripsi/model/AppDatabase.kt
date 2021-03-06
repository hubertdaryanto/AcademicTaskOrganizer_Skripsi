package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TugasKuliah::class, TugasKuliahToDoList::class, SubjectTugasKuliah::class, TugasKuliahImage::class, TugasKuliahCompletionHistory::class), version = 24, exportSchema = false)
abstract class
AppDatabase : RoomDatabase() {
//    abstract val getAllQueryListDao: allQueryDao
    abstract val getTugasKuliahDao: tugasKuliahDao
    abstract val getTugasKuliahToDoListDao: tugasKuliahToDoListDao
    abstract val getTugasKuliahImageDao: tugasKuliahImageDao
    abstract val getSubjectTugasKuliahDao: subjectTugasKuliahDao
    abstract val getTugasKuliahCompletionHistoryDao: tugasKuliahCompletionHistoryDao

    companion object{
        @Volatile private var INSTANCE : AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            synchronized(this){
                var instance =
                    INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "appDatabase"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}