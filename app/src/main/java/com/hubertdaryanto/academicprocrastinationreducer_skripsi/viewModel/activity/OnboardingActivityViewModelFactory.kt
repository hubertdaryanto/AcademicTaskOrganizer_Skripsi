package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.activity

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.subjectTugasKuliahDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahDao

class OnboardingActivityViewModelFactory(private val tugasKuliahDataSource: tugasKuliahDao,
                                         private val subjectTugasKuliahDataSource: subjectTugasKuliahDao,
                                         private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingActivityViewModel::class.java))
        {
            return OnboardingActivityViewModel(tugasKuliahDataSource, subjectTugasKuliahDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}