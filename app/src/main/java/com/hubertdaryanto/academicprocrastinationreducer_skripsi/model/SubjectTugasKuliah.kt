package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SubjectTugasKuliah")
data class SubjectTugasKuliah(
    @ColumnInfo(name = "subjectName")
    var subjectTugasKuliahName: String
){
@PrimaryKey(autoGenerate = true)
@ColumnInfo(name = "subjectTugasKuliahId")
var subjectTugasKuliahId: Long = 0
}