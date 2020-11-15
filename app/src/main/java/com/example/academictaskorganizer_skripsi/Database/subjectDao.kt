package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*

@Dao
interface subjectDao{
    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    suspend fun loadSubjectNameById(id: Int): String

    @Insert
    suspend fun insertSubject(vararg subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)
}