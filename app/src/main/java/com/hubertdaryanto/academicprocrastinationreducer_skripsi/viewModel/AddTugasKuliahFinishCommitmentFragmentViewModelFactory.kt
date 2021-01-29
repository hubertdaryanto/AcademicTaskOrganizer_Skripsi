package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao

class AddTugasKuliahFinishCommitmentFragmentViewModelFactory(private val application: Application, private val dataSource: allQueryDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTugasKuliahFinishCommitmentFragmentViewModel::class.java)) {
            return AddTugasKuliahFinishCommitmentFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}