package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahToDoListDao

class TugasMataKuliahListFragmentViewModelFactory(private val tugasKuliahDataSource: tugasKuliahDao,
                                                  private val tugasKuliahToDoListDataSource: tugasKuliahToDoListDao,
                                                  private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unckecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TugasMataKuliahListFragmentViewModel::class.java))
        {
            return TugasMataKuliahListFragmentViewModel(tugasKuliahDataSource, tugasKuliahToDoListDataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}