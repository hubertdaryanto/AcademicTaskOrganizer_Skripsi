package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah

class SubjectTugasKuliahListener(val clickListener: (subjectId: Long) -> Unit)
{
    fun onClick(subjectTugasKuliah: SubjectTugasKuliah) = clickListener(subjectTugasKuliah.subjectTugasKuliahId)
}