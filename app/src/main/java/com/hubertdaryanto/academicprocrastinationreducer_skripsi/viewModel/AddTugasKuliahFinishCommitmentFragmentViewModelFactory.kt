package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahImageDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahToDoListDao

class AddTugasKuliahFinishCommitmentFragmentViewModelFactory(private val application: Application, private val tugasKuliahDataSource: tugasKuliahDao, private val tugasKuliahImageDataSource: tugasKuliahImageDao, private val tugasKuliahToDoListDataSource: tugasKuliahToDoListDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTugasKuliahFinishCommitmentFragmentViewModel::class.java)) {
            return AddTugasKuliahFinishCommitmentFragmentViewModel(application, tugasKuliahDataSource, tugasKuliahImageDataSource, tugasKuliahToDoListDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}