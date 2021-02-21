package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*

class EditTugasKuliahFragmentViewModelFactory(private val application: Application,
                                              private val tugasKuliahDataSource: tugasKuliahDao,
                                              private val tugasKuliahToDoListDataSource: tugasKuliahToDoListDao,
                                              private val tugasKuliahImageDataSource: tugasKuliahImageDao,
                                              private val subjectTugasKuliahDataSource: subjectTugasKuliahDao,
                                              private val tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTugasKuliahFragmentViewModel::class.java))
        {
            return EditTugasKuliahFragmentViewModel(application, tugasKuliahDataSource, tugasKuliahToDoListDataSource, tugasKuliahImageDataSource, subjectTugasKuliahDataSource, tugasKuliahCompletionHistoryDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}