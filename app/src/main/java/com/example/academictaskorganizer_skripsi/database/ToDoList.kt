package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "ToDoList")
data class ToDoList(
    @ColumnInfo(name = "ToDoListName") var ToDoListName: String?,
    @ColumnInfo(name = "Deadline") var deadline: Date?
): Serializable
{
    @ColumnInfo(name = "ToDoListId")
    @PrimaryKey(autoGenerate = true)
    var ToDoListId: Int = 0
}