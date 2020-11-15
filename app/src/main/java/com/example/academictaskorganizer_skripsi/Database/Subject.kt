package com.example.academictaskorganizer_skripsi.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Subject")
data class Subject(
    @ColumnInfo(name = "SubjectName") val SubjectName: String?
): Serializable
{
    @PrimaryKey(autoGenerate = true)
    var SubjectId: Int = 0

    fun isSubjectAlreadyAvailable()
    {

    }
}