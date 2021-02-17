package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahToDoListDao

class TugasKuliahAdapterViewModelFactory(private val application: Application,
                                         private val tugasKuliahToDoListDataSource: tugasKuliahToDoListDao
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TugasKuliahAdapterViewModel::class.java))
        {
            return TugasKuliahAdapterViewModel(application, tugasKuliahToDoListDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}