package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistoryDataItem

class TugasKuliahCompletionHistoryDiffCallback : DiffUtil.ItemCallback<TugasKuliahCompletionHistoryDataItem>() {
    override fun areItemsTheSame(oldItem: TugasKuliahCompletionHistoryDataItem, newItem: TugasKuliahCompletionHistoryDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TugasKuliahCompletionHistoryDataItem, newItem: TugasKuliahCompletionHistoryDataItem): Boolean {
        return oldItem == newItem
    }
}