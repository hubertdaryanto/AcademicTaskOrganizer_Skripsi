package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListItemType

sealed class TugasKuliahDataItem {
    abstract val id: Long
    open var count: Long = 0
    abstract val type: Int
    object Header: TugasKuliahDataItem(){
        override val id = Long.MIN_VALUE
         override val type = 0
}
    data class TugasKuliahList(val tugasKuliahListItemType: TugasKuliahListItemType): TugasKuliahDataItem()
    {
        fun getLong(): Long{
            if (tugasKuliahListItemType is TugasKuliah)
            {
                return tugasKuliahListItemType.tugasKuliahId + count
            }
            else
            {
                count = count + 1
                return count
            }
        }
        override val id: Long = getLong()
        override val type: Int = tugasKuliahListItemType.getType()
    }
}