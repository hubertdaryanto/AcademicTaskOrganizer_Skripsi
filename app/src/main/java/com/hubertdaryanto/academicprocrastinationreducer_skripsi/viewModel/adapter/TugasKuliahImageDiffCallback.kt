package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImageDataItem

class TugasKuliahImageDiffCallback : DiffUtil.ItemCallback<TugasKuliahImageDataItem>() {
    override fun areItemsTheSame(oldItemForTugasKuliahImage: TugasKuliahImageDataItem, newItemForTugasKuliahImage: TugasKuliahImageDataItem): Boolean {
        return oldItemForTugasKuliahImage.id == newItemForTugasKuliahImage.id
    }
    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItemForTugasKuliahImage: TugasKuliahImageDataItem, newItemForTugasKuliahImage: TugasKuliahImageDataItem): Boolean {
        return oldItemForTugasKuliahImage == newItemForTugasKuliahImage
    }
}