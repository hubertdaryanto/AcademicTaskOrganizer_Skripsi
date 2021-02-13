package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*

@Dao
interface tugasKuliahCompletionHistoryDao {
    @Query("SELECT * FROM TugasKuliahCompletionHistory ORDER BY tugasKuliahCompletionHistoryId DESC")
    suspend fun getAllTugasKuliahCompletionHistorySortedByMostRecent(): MutableList<TugasKuliahCompletionHistory>

    @Query("SELECT * FROM TugasKuliahCompletionHistory WHERE bindToTugasKuliahId LIKE :id")
    suspend fun getTugasKuliahCompletionHistoryByTugasKuliahId(id: Long): TugasKuliahCompletionHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Delete
    suspend fun deleteTugasKuliahCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Update
    suspend fun updateTugasKuliahCompletionHistory(tugasKuliahCompletionHistory: TugasKuliahCompletionHistory)
}