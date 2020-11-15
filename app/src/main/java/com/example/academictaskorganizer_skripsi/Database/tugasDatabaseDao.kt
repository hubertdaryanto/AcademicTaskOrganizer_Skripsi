package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*

@Dao
interface tugasDatabaseDao{
    @Query("SELECT * FROM tugas ORDER BY name ASC")
    suspend fun getAllSortedByName(): List<tugas>

    @Query("SELECT * FROM tugas WHERE id IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<tugas>

    @Query("SELECT * FROM tugas WHERE name LIKE :name")
    suspend fun findByName(name: String): tugas

    @Insert
    suspend fun insertTugas(vararg tugas: tugas)

    @Delete
    suspend fun deleteTugas(tugas: tugas)

    @Update
    suspend fun updateTugas(tugas: tugas)
}