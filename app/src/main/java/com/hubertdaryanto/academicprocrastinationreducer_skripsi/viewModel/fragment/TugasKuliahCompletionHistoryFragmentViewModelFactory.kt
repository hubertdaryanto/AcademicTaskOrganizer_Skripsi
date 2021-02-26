package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahCompletionHistoryDao

class TugasKuliahCompletionHistoryFragmentViewModelFactory(private val tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao,
                                                           private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TugasKuliahCompletionHistoryFragmentViewModel::class.java))
        {
            return TugasKuliahCompletionHistoryFragmentViewModel(tugasKuliahCompletionHistoryDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}