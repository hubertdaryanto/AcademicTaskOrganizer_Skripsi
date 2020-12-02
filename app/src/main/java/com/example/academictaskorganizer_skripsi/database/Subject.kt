package com.example.academictaskorganizer_skripsi.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Subject")
data class Subject(
    @ColumnInfo(name = "subjectName")
    var subjectName: String
){
//     constructor(subjectAndTugasKuliah: SubjectAndTugasKuliah): this {
//         this.subjectId = subjectAndTugasKuliah.subject.subjectId
//         this.subjectName = subjectAndTugasKuliah.subject.subjectName
//     }
@PrimaryKey(autoGenerate = true)
@ColumnInfo(name = "subjectId")
var subjectId: Long = 0
}