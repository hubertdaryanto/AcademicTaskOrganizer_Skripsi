package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao


class MataKuliahListFragmentViewModelFactory(private val application: Application,
                                                       private val subjectTugasKuliahDataSource: subjectTugasKuliahDao
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MataKuliahListFragmentViewModel::class.java))
        {
            return MataKuliahListFragmentViewModel(application, subjectTugasKuliahDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}