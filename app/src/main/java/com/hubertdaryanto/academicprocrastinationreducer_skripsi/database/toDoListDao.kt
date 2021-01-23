package com.hubertdaryanto.academicprocrastinationreducer_skripsi.database

import androidx.room.*

@Dao
interface toDoListDao{
    @Query("SELECT ToDoListName FROM ToDoList WHERE ToDoListId LIKE :id")
    suspend fun loadToDoListNameById(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(vararg toDoList: ToDoList)

    @Delete
    suspend fun deleteToDoList(toDoList: ToDoList)

    @Update
    suspend fun updateToDoList(toDoList: ToDoList)
}