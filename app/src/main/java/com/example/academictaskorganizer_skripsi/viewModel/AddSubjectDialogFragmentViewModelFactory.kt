package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.database.subjectDao

class AddSubjectDialogFragmentViewModelFactory(private val application: Application, private val dataSource: subjectDao): ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSubjectDialogFragmentViewModel::class.java))
        {
            return AddSubjectDialogFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
