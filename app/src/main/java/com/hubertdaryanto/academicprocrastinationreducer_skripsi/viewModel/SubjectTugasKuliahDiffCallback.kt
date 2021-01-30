package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliahDataItem

class SubjectTugasKuliahDiffCallback : DiffUtil.ItemCallback<SubjectTugasKuliahDataItem>() {
    override fun areItemsTheSame(oldItemTugasKuliah: SubjectTugasKuliahDataItem, newItemTugasKuliah: SubjectTugasKuliahDataItem): Boolean {
        return oldItemTugasKuliah.id == newItemTugasKuliah.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItemTugasKuliah: SubjectTugasKuliahDataItem, newItemTugasKuliah: SubjectTugasKuliahDataItem): Boolean {
        return oldItemTugasKuliah == newItemTugasKuliah
    }

}