package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao

class EditSubjectTugasKuliahDialogFragmentViewModelFactory(private val application: Application, private val dataSource: subjectTugasKuliahDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditSubjectTugasKuliahDialogFragmentViewModel::class.java))
        {
            return EditSubjectTugasKuliahDialogFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
