package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "ToDoList", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = CASCADE, onUpdate = CASCADE)
]
)
data class ToDoList(
    @ColumnInfo(name = "toDoListId")
    @PrimaryKey(autoGenerate = true)
    var toDoListId: Int = 0,
    @ColumnInfo(name = "bindToTugasKuliahId")
    val bindToTugasKuliahId: Int,
    @ColumnInfo(name = "toDoListName")
    var toDoListName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean
)