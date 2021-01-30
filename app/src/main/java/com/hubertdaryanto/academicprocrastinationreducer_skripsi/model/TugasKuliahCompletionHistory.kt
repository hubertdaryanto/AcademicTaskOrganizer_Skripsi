package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TugasKuliahCompletionHistoryListItemType

@Entity(tableName = "TugasKuliahCompletionHistory", foreignKeys = [
    ForeignKey(entity = TugasKuliah::class, parentColumns = ["tugasKuliahId"], childColumns = ["bindToTugasKuliahId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
])
data class TugasKuliahCompletionHistory(
    @ColumnInfo(name = "bindToTugasKuliahId")
    var bindToTugasKuliahId: Long,
    @ColumnInfo(name = "type")
    var activityType: String
): TugasKuliahCompletionHistoryListItemType
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "tugasKuliahCompletionHistoryId")
    var tugasKuliahCompletionHistoryId: Long = System.currentTimeMillis()
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_ITEM
    }
}