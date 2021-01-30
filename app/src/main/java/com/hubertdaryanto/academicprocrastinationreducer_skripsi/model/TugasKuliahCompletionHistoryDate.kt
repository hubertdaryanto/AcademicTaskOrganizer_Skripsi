package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.TugasKuliahCompletionHistoryListItemType

class TugasKuliahCompletionHistoryDate(d: String, c: String):
    TugasKuliahCompletionHistoryListItemType {
    var date = d
    var count = c
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }
}