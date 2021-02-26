package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListItemType

class TugasKuliahDate(d: String, c: String):
    TugasKuliahListItemType {
    var date = d
    var count = c
    override fun getType(): Int {
        return ITEM_VIEW_TYPE_HEADER
    }
}