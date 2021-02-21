package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah

class TugasKuliahListener(val clickListener: (TugasKuliahId: Long) -> Unit)
{
    fun onClick(tugasKuliah: TugasKuliah) = clickListener(tugasKuliah.tugasKuliahId)
}