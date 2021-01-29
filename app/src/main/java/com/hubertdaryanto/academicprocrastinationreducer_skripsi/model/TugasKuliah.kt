package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TugasKuliahListItemType
import java.io.Serializable

@Entity(tableName = "TugasKuliah"
    ,foreignKeys = [
        ForeignKey(entity = SubjectTugasKuliah::class, parentColumns = arrayOf("subjectId"), childColumns = arrayOf("tugasSubjectId"), onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
    ]
)
data class TugasKuliah(
    @ColumnInfo(name = "tugasSubjectId")
    var tugasSubjectId: Long,
    @ColumnInfo(name = "tugasKuliahName")
    var tugasKuliahName: String,
    @ColumnInfo(name = "deadline")
    var deadline: Long,
    @ColumnInfo(name = "isFinished")
    var isFinished: Boolean,
    @ColumnInfo(name = "notes")
    var notes: String,
    var finishCommitment: Long,
    var updatedAt: Long
): TugasKuliahListItemType, Serializable
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tugasKuliahId")
    var tugasKuliahId: Long = 0
    override fun getType(): Int {
        if (isFinished)
        {
            return ITEM_VIEW_TYPE_ITEM_FINISHED
        }
        else
        {
            return ITEM_VIEW_TYPE_ITEM
        }
    }

    companion object{
        const val KEY_ID = "id"
    }
}