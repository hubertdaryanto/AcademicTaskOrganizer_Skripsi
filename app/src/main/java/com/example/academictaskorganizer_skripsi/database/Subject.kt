package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Subject")
data class Subject(
    @ColumnInfo(name = "subjectId")
    @PrimaryKey(autoGenerate = true)
    var subjectId: Long = 0L,
    @ColumnInfo(name = "subjectName")
    var subjectName: String
)