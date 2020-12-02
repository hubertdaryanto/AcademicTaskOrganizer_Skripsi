package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import com.example.academictaskorganizer_skripsi.database.tugasDatabaseDao

class HomeFragmentViewModel(dataSource: tugasDatabaseDao, application: Application): ViewModel() {
    val database = dataSource

//    private var tugasList = MutableLiveData<TugasKuliah?>()

    var tugas = database.getAllSortedByDeadlineForeground()

    private val _navigateToEditTugasKuliah = MutableLiveData<Long>()
    val navigateToEditTugasKuliah: LiveData<Long>
        get() = _navigateToEditTugasKuliah

    private val _navigateToAddTugasKuliah = MutableLiveData<Boolean>()
    val navigateToAddTugasKuliah: LiveData<Boolean>
        get() = _navigateToAddTugasKuliah

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackbarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar()
    {
        _showSnackbarEvent.value = null
    }

//    fun doneNavigating()
//    {
//        _navigateToEditTugasKuliah.value = null
//    }

    init {
//        initializeUpcoming()
        Log.i("AgendaActivityViewModel", "AgendaActivityViewModel created!")
    }

//    private fun initializeTugasList()
//    {
//        viewModelScope.launch {
//            tugasList.value = getTugasFromDatabase()
//        }
//    }
//
//    private suspend fun getTugasFromDatabase(): TugasKuliah?
//    {
//        var tugas = database.getAllSortedByDeadline()
//        return tugas
//    }

    fun onAddTugasKuliahClicked()
    {
        _navigateToAddTugasKuliah.value = true
    }

    fun onAddTugasKuliahNavigated()
    {
        _navigateToAddTugasKuliah.value = false
    }

    fun onTugasKuliahClicked(id: Long) {
        _navigateToEditTugasKuliah.value = id
    }

    fun onEditTugasKuliahNavigated()
    {
        _navigateToEditTugasKuliah.value = null
    }
    override fun onCleared() {
        super.onCleared()
        Log.i("AgendaActivityViewModel", "AgendaActivityViewModel destroyed!")
    }
}
