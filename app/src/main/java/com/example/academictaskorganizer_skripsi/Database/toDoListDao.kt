package com.example.academictaskorganizer_skripsi.Database

import androidx.room.*

@Dao
interface toDoListDao{
    @Query("SELECT ToDoListName FROM ToDoList WHERE ToDoListId LIKE :id")
    suspend fun loadToDoListNameById(id: Int): String

    @Insert
    suspend fun insertToDoList(vararg toDoList: ToDoList)

    @Delete
    suspend fun deleteToDoList(toDoList: ToDoList)

    @Update
    suspend fun updateToDoList(toDoList: ToDoList)
}