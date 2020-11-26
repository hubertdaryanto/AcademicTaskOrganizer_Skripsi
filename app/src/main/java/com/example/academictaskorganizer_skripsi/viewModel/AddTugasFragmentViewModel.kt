package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao
import kotlinx.coroutines.*

class AddTugasFragmentViewModel(application: Application, dataSource: tugasDatabaseDao): ViewModel() {
    val database = dataSource

    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _addTugasKuliahNavigation = MutableLiveData<Boolean?>()
    val addTugasKuliahNavigation: LiveData<Boolean?>
        get() = _addTugasKuliahNavigation


    fun onAddTugasKuliahClicked2()
    {
//        uiScope.launch {
//            insert(tugasKuliah)
            _addTugasKuliahNavigation.value = true
//        }

    }

    fun addTugasKuliah(tugasKuliah: TugasKuliah)
    {
        viewModelScope.launch {
            database.insertTugas(tugasKuliah)
        }
    }

    private suspend fun insert(tugasKuliah: TugasKuliah) {
        withContext(Dispatchers.IO) {
            database.insertTugas(tugasKuliah)
        }
    }

    fun doneNavigating() {
        _addTugasKuliahNavigation.value = null
    }
}