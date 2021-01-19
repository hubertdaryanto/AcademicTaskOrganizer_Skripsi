package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.database.allQueryDao
import com.example.academictaskorganizer_skripsi.database.subjectDao

class AddTugasCommitmentFragmentViewModelFactory(private val application: Application, private val dataSource: allQueryDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTugasCommitmentFragmentViewModel::class.java)) {
            return AddTugasCommitmentFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}