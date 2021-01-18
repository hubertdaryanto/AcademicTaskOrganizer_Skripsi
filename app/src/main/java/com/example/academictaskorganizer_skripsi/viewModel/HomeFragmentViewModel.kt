package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academictaskorganizer_skripsi.database.ToDoList
import com.example.academictaskorganizer_skripsi.database.allQueryDao
import com.example.academictaskorganizer_skripsi.view.TugasKuliahDate
import com.example.academictaskorganizer_skripsi.view.TugasKuliahListItemType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragmentViewModel(dataSource: allQueryDao, application: Application): ViewModel() {
    val database = dataSource
    var tugas = database.getAllTugasKuliahSortedByDeadlineForeground()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    val _toDoList = MutableLiveData<MutableList<ToDoList>>()
    val toDoList: LiveData<MutableList<ToDoList>>
        get() = _toDoList

    fun loadToDoList(id: Long)
    {
        uiScope.launch {
            _toDoList.value = database.loadToDoListsByTugasKuliahId(id)
        }
    }

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

    fun getTugasKuliahDate(): List<TugasKuliahListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahListItemType>()
        var date: String = ""
        for (i in tugas.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.deadline)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
            }
            else
            {
                date = dateCursor
                var tugasKuliahDate = TugasKuliahDate(date)
                arrayList.add(tugasKuliahDate)
                arrayList.add(i)
            }
        }
        return arrayList
    }

    init {
        Log.i("AgendaActivityViewModel", "AgendaActivityViewModel created!")
    }

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
