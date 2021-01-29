package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TugasKuliahAdapterViewModel(application: Application, dataSource: allQueryDao): ViewModel() {
    val database = dataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoList

    fun loadToDoList(id: Long)
    {
        uiScope.launch {
            _toDoList.value = database.loadToDoListsByTugasKuliahId(id)
        }
    }
}