package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "ToDoList")
data class ToDoList(
    @PrimaryKey(autoGenerate = true)
    var toDoListId: Long = 0L,
    @ColumnInfo(name = "toDoListName")
    var toDoListName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long
)