package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Subject")
data class Subject(
    @PrimaryKey
    var subjectId: Long,
    @ColumnInfo(name = "subjectName")
    var subjectName: String
)