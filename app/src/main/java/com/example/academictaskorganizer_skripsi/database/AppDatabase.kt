package com.example.academictaskorganizer_skripsi.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(TugasKuliah::class, ToDoList::class, Subject::class, ImageForTugas::class), version = 15, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract val getTugasDao: tugasDatabaseDao
    abstract val getToDoListDao: toDoListDao
    abstract val getSubjectDao: subjectDao

    companion object{
        @Volatile private var INSTANCE : AppDatabase ? = null
//        private val LOCK = Any ()
//        operator fun invoke (context: Context) = instance ?: synchronized(LOCK)
//        {
//            instance ?: buildDatabase(context).also {
//                instance = it
//            }
//        }
//        private fun buildDatabase (context:Context) = Room.databaseBuilder(
//            context.applicationContext,
//            AppDatabase::class.java,
//             " appDatabase " ).fallbackToDestructiveMigration().build()

        fun getInstance(context: Context): AppDatabase {
            synchronized(this){
                var instance = INSTANCE

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



//val db = Room.databaseBuilder(
//    applicationContext,
//    AppDatabase::class.java, "database-name"
//).build()