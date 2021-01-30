package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.annotation.SuppressLint
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahDataItem
import org.stephenbrewer.arch.recyclerview.DiffUtil

class TugasKuliahDiffCallback : DiffUtil.ItemCallback<TugasKuliahDataItem>() {
    override fun areItemsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem.id == newItem.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TugasKuliahDataItem, newItem: TugasKuliahDataItem): Boolean {
        return oldItem == newItem
    }
}