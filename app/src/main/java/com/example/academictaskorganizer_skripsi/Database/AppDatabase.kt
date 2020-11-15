package com.example.academictaskorganizer_skripsi.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(TugasKuliah::class, ToDoList::class, Subject::class), version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTugasDao(): tugasDatabaseDao
    abstract fun getToDoListDao(): toDoListDao
    abstract fun getSubjectDao(): subjectDao

    companion object{
        @Volatile private var instance : AppDatabase ? = null
        private val LOCK = Any ()
        operator fun invoke (context: Context) = instance ?: synchronized(LOCK)
        {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }
        private fun buildDatabase (context:Context) = Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
             " appDatabase " ).build()
    }
}

//val db = Room.databaseBuilder(
//    applicationContext,
//    AppDatabase::class.java, "database-name"
//).build()