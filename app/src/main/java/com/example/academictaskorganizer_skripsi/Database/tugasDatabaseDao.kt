package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*

@Dao
interface tugasDatabaseDao{
    @Query("SELECT * FROM TugasKuliah ORDER BY TugasKuliahName ASC")
    suspend fun getAllSortedByName(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah ORDER BY Deadline DESC")
    suspend fun getAllSortedByDeadline(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahName LIKE :name")
    suspend fun findByName(name: String): TugasKuliah

    @Insert
    suspend fun insertTugas(vararg TugasKuliah: TugasKuliah)

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateTugas(TugasKuliah: TugasKuliah)

    //one to one relationship
    @Transaction
    @Query("SELECT * FROM TugasKuliah")
    suspend fun getTugasKuliahAndSubject(): List<TugasKuliahAndSubject>

    //one to many relationship
    @Transaction
    @Query("SELECT * FROM TugasKuliah")
    suspend fun getTugasKuliahWithToDoList(): List<TugasKuliahWithToDoList>
}