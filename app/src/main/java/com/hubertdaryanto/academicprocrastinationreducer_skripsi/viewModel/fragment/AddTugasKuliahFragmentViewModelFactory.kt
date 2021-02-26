package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahDao

class AddTugasKuliahFragmentViewModelFactory(private val application: Application,
                                             private val tugasKuliahDataSource: tugasKuliahDao,
                                             private val subjectTugasKuliahDataSource: subjectTugasKuliahDao
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTugasKuliahFragmentViewModel::class.java))
        {
            return AddTugasKuliahFragmentViewModel(application, tugasKuliahDataSource, subjectTugasKuliahDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}