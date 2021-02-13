package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahToDoListDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TugasKuliahAdapterViewModel(application: Application, tugasKuliahToDoListDataSource: tugasKuliahToDoListDao): ViewModel() {
    val tugasKuliahToDoListDatabase = tugasKuliahToDoListDataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList

    fun loadTugasKuliahToDoList(id: Long)
    {
        uiScope.launch {
            _tugasKuliahToDoList.value = tugasKuliahToDoListDatabase.loadToDoListsByTugasKuliahId(id)
        }
    }
}