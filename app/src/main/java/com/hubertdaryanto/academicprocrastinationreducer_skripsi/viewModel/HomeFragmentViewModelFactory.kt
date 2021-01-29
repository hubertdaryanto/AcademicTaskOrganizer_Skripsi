package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao

class HomeFragmentViewModelFactory(private val dataSource: allQueryDao,
                                   private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeFragmentViewModel::class.java))
        {
            return HomeFragmentViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}