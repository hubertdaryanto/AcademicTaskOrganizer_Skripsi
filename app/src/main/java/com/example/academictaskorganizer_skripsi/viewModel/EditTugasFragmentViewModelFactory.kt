package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao

class EditTugasFragmentViewModelFactory(private val application: Application,
                                       private val dataSource: tugasDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditTugasFragmentViewModel::class.java))
        {
            return EditTugasFragmentViewModel(application, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}