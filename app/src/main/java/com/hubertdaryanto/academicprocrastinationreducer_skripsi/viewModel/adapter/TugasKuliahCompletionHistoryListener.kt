package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory

class TugasKuliahCompletionHistoryListener(val clickListener: (TugasKuliahCompletionHistoryId: Long) -> Unit)
{
    fun onClick(tugasKuliahCompletionHistory: TugasKuliahCompletionHistory) = clickListener(tugasKuliahCompletionHistory.bindToTugasKuliahId)
}