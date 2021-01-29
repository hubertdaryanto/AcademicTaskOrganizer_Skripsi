package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Subject")
data class SubjectTugasKuliah(
    @ColumnInfo(name = "subjectName")
    var subjectName: String
){
@PrimaryKey(autoGenerate = true)
@ColumnInfo(name = "subjectId")
var subjectId: Long = 0
}