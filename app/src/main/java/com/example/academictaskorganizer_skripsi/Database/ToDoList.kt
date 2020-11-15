package com.example.academictaskorganizer_skripsi.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "ToDoList")
data class ToDoList(
    @ColumnInfo(name = "ToDoListName") val ToDoListName: String?,
    @ColumnInfo(name = "Deadline") val deadline: Date?
): Serializable
{
    @PrimaryKey(autoGenerate = true)
    var ToDoListId: Int = 0
}