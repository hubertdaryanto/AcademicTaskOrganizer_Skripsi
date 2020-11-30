package com.example.academictaskorganizer_skripsi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface subjectDao{
    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    suspend fun loadSubjectNameById(id: Long): String


    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    fun loadSubjectNameByIdForeground(id: Long): String

    @Query("SELECT * FROM Subject ORDER BY SubjectName ASC")
    fun getSubjectByNameForeground(): LiveData<List<Subject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(vararg subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)
}