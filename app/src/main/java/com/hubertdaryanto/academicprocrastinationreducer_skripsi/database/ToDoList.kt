package com.hubertdaryanto.academicprocrastinationreducer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey


@Entity(tableName = "ToDoList", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = CASCADE, onUpdate = CASCADE)
]
)
data class ToDoList(
    @ColumnInfo(name = "bindToTugasKuliahId")
    var bindToTugasKuliahId: Long,
    @ColumnInfo(name = "toDoListName")
    var toDoListName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean
)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "toDoListId")
    var toDoListId: Long = 0
}