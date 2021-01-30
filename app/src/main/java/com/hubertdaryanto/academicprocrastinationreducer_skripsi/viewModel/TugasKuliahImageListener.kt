package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImage

class TugasKuliahImageListener(val clickListener: (ImageForTugasId: Long) -> Unit)
{
    fun onClick(TugasKuliahImage: TugasKuliahImage) = clickListener(TugasKuliahImage.tugasKuliahImageId)
}