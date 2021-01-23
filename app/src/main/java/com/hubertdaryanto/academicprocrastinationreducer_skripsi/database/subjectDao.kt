package com.hubertdaryanto.academicprocrastinationreducer_skripsi.database

import androidx.room.*

@Dao
interface subjectDao{
    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    suspend fun loadSubjectNameById(id: Long): String


    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    fun loadSubjectNameByIdForeground(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(vararg subject: Subject)

    @Delete
    suspend fun deleteSubject(subject: Subject)

    @Update
    suspend fun updateSubject(subject: Subject)
}