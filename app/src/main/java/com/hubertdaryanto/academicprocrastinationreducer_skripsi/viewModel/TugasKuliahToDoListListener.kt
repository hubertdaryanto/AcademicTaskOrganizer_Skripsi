package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList

class TugasKuliahToDoListListener(val clickListener: (TugasKuliahToDoListId: Long) -> Unit)
{
    fun onClick(tugasKuliahToDoList: TugasKuliahToDoList) = clickListener(tugasKuliahToDoList.tugasKuliahToDoListId)
}