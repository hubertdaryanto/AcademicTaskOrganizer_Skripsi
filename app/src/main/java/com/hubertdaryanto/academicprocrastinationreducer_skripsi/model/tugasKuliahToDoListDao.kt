package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList

@Dao
interface tugasKuliahToDoListDao{
    @Query("SELECT tugasKuliahToDoListName FROM TugasKuliahToDoList WHERE tugasKuliahToDoListId LIKE :id")
    suspend fun loadToDoListNameById(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Delete
    suspend fun deleteTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Update
    suspend fun updateTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)
}