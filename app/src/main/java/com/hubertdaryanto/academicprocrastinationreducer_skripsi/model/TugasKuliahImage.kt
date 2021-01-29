package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah

@Entity(tableName = "TugasKuliahImage", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
])
data class TugasKuliahImage(
    @ColumnInfo(name = "bindToTugasKuliahId")
    var bindToTugasKuliahId: Long,
    @ColumnInfo(name = "imageName")
    var imageName: String
)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "imageId")
    var imageId: Long = 0
}