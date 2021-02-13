package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*

@Dao
interface tugasKuliahDao {
    @Query("SELECT * FROM TugasKuliah ORDER BY tugasKuliahName ASC")
    suspend fun getAllSortedByName(): List<TugasKuliah>

    @Query("SELECT tugasKuliahName FROM TugasKuliah")
    suspend fun loadAllTugasKuliahName(): List<String>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline ASC")
    suspend fun getAllSortedByDeadline(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE isFinished LIKE 0")
    suspend fun loadAllTugasKuliahUnfinished(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId IN (:userIds)")
    suspend fun loadAllByIds(userIds: LongArray): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE isFinished LIKE 0 ORDER BY deadline ASC")
    suspend fun getAllTugasKuliahUnfinishedSortedByDeadline(): MutableList<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId LIKE :id")
    suspend fun loadTugasKuliahById(id: Long): TugasKuliah

    @Query("SELECT tugasKuliahName FROM TugasKuliah WHERE TugasKuliahId LIKE :id")
    suspend fun loadTugasKuliahNameById(id: Long): String

    @Query("SELECT * FROM TugasKuliah WHERE tugasKuliahName LIKE :name")
    suspend fun findByName(name: String): TugasKuliah

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliah(TugasKuliah: TugasKuliah): Long

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateTugasKuliah(TugasKuliah: TugasKuliah)
}