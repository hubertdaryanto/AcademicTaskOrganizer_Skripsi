package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList

@Dao
interface tugasKuliahToDoListDao{
    @Query("SELECT ToDoListName FROM TugasKuliahToDoList WHERE ToDoListId LIKE :id")
    suspend fun loadToDoListNameById(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(vararg tugasKuliahToDoList: TugasKuliahToDoList)

    @Delete
    suspend fun deleteToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Update
    suspend fun updateToDoList(tugasKuliahToDoList: TugasKuliahToDoList)
}