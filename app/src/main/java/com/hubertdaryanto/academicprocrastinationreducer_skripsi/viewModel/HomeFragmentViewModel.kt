package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeFragmentViewModel(dataSource: allQueryDao, application: Application): ViewModel() {
    val database = dataSource

    //todo : rename variable same as class diagram
    private val _tugas = MutableLiveData<MutableList<TugasKuliah>>()
    val tugas: LiveData<MutableList<TugasKuliah>>
        get() = _tugas
//    var tugas = database.getAllTugasKuliahSortedByDeadlineForeground()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _toDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _toDoList

    private val _navigateToEditTugasKuliah = MutableLiveData<Long>()
    val navigateToEditTugasKuliah: LiveData<Long>
        get() = _navigateToEditTugasKuliah

    private val _navigateToAddTugasKuliah = MutableLiveData<Boolean>()
    val navigateToAddTugasKuliah: LiveData<Boolean>
        get() = _navigateToAddTugasKuliah

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackbarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun loadTugasKuliah()
    {
        uiScope.launch {
            _tugas.value = database.getAllTugasKuliahUnfinishedSortedByDeadline()
        }
    }

    fun loadToDoList(id: Long)
    {
        uiScope.launch {
            _toDoList.value = database.loadToDoListsByTugasKuliahId(id)
        }
    }

    fun doneShowingSnackbar()
    {
        _showSnackbarEvent.value = null
    }

    fun getTugasKuliahDate(): List<TugasKuliahListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
        for (i in tugas.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.deadline)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
                count++
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        count.toString()
                    )
            }
            else
            {
                date = dateCursor
                tempdate = date
                var tugasKuliahDate =
                    TugasKuliahDate(
                        date,
                        "0"
                    )
                arrayList.add(tugasKuliahDate)
                temp = arrayList.count() - 1
                arrayList.add(i)
                count = 1
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        count.toString()
                    )
                //kalau misalkan i++ disini, maka jumlah nya belum ketahuan ada berapa pas munculin header nya
                //bisa sih pakai query load data from deadline xxxx ke xxxx pakai modulus per hari buat nentuin parameter query nya
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

    fun onTugasKuliahClicked(id: Long)
    {
        _navigateToEditTugasKuliah.value = id
    }

    fun onEditTugasKuliahNavigated()
    {
        _navigateToEditTugasKuliah.value = null
    }
}
