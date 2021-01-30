package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

interface TugasKuliahListItemType {
    val ITEM_VIEW_TYPE_HEADER: Int
        get() = 0
    val ITEM_VIEW_TYPE_ITEM: Int
        get() = 1
    val ITEM_VIEW_TYPE_ITEM_FINISHED: Int
        get() = 2

    fun getType(): Int
}