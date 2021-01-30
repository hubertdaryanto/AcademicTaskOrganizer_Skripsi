package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoListDataItem

class TugasKuliahToDoListDiffCallback : DiffUtil.ItemCallback<TugasKuliahToDoListDataItem>() {
    override fun areItemsTheSame(oldItemTugasKuliah: TugasKuliahToDoListDataItem, newItemTugasKuliah: TugasKuliahToDoListDataItem): Boolean {
        return oldItemTugasKuliah.id == newItemTugasKuliah.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItemTugasKuliah: TugasKuliahToDoListDataItem, newItemTugasKuliah: TugasKuliahToDoListDataItem): Boolean {
        return oldItemTugasKuliah == newItemTugasKuliah
    }

}