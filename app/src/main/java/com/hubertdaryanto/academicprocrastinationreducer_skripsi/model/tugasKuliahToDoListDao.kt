package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*

@Dao
interface tugasKuliahToDoListDao{
    @Query("SELECT tugasKuliahToDoListName FROM TugasKuliahToDoList WHERE tugasKuliahToDoListId LIKE :id")
    suspend fun loadToDoListNameById(id: Long): String

    @Query("SELECT * FROM TugasKuliahToDoList WHERE bindToTugasKuliahId LIKE :id")
    suspend fun loadToDoListsByTugasKuliahId(id: Long): MutableList<TugasKuliahToDoList>

    @Query("DELETE FROM TugasKuliahToDoList WHERE tugasKuliahToDoListId LIKE :id")
    suspend fun deleteTugasKuliahToDoListById(id: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahToDoLists(tugasKuliahToDoList: List<TugasKuliahToDoList>)

    @Delete
    suspend fun deleteTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)



    @Update
    suspend fun updateTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Update
    suspend fun updateTugasKuliahToDoLists(tugasKuliahToDoList: List<TugasKuliahToDoList>)
}