package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahCompletionHistoryListItemType

sealed class TugasKuliahCompletionHistoryDataItem {
    abstract val id: Long
    open var count: Long = 0
    open var counter: Long = 0
    abstract val type: Int
    object Header: TugasKuliahCompletionHistoryDataItem(){
        override val id = Long.MIN_VALUE
        override val type = 0
    }
    data class TugasKuliahCompletionHistoryList(val tugasKuliahCompletionHistoryListItemType: TugasKuliahCompletionHistoryListItemType): TugasKuliahCompletionHistoryDataItem()
    {
        fun getLong(): Long{
            if (tugasKuliahCompletionHistoryListItemType is TugasKuliahCompletionHistory)
            {
                count = tugasKuliahCompletionHistoryListItemType.tugasKuliahCompletionHistoryId
                return count + counter
            }
            else
            {
                counter = counter + 1
                return counter
            }
        }
        override val id: Long = getLong()
        override val type: Int = tugasKuliahCompletionHistoryListItemType.getType()
    }
}