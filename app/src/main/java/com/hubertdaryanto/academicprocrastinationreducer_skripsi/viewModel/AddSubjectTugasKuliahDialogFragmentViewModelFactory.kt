package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao

class AddSubjectTugasKuliahDialogFragmentViewModelFactory(private val application: Application, private val dataSource: subjectTugasKuliahDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSubjectTugasKuliahDialogFragmentViewModel::class.java))
        {
            return AddSubjectTugasKuliahDialogFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
