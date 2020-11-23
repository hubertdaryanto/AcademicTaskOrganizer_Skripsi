package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao

class HomeFragmentViewModelFactory(private val dataSource: tugasDatabaseDao,
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