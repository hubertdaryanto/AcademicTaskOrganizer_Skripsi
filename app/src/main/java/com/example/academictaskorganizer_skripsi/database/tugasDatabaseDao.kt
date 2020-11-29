package com.example.academictaskorganizer_skripsi.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface tugasDatabaseDao{
    @Query("SELECT * FROM TugasKuliah ORDER BY tugasKuliahName ASC")
    suspend fun getAllSortedByName(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline DESC")
    suspend fun getAllSortedByDeadline(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline DESC")
    fun getAllSortedByDeadlineForeground(): LiveData<List<TugasKuliah>>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId IN (:userIds)")
    suspend fun loadAllByIds(userIds: LongArray): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE tugasKuliahName LIKE :name")
    suspend fun findByName(name: String): TugasKuliah

    @Insert
    suspend fun insertTugas(vararg TugasKuliah: TugasKuliah)

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateTugas(TugasKuliah: TugasKuliah)

    //one to one relationship
    @Transaction
    @Query("SELECT * FROM Subject WHERE subjectId  IN (SELECT DISTINCT(tugasSubjectId) FROM TugasKuliah)")
    fun getTugasKuliahAndSubject(): LiveData<List<SubjectAndTugasKuliah>>

    //one to many relationship
    @Transaction
    @Query("SELECT * FROM TugasKuliah WHERE tugasToDoListId IN (SELECT DISTINCT(toDoListId) FROM ToDoList)")
    fun getTugasKuliahWithToDoList(): LiveData<List<TugasKuliahWithToDoList>>

    @Query("SELECT * FROM Subject WHERE subjectId LIKE :id")
    suspend fun loadSubjectName(id: Long): Subject
}