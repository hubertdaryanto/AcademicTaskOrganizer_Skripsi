package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahListItemType
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertDeadlineToDateFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HomeFragmentViewModel(tugasKuliahDataSource: tugasKuliahDao, tugasKuliahToDoListDataSource: tugasKuliahToDoListDao, tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao, application: Application): ViewModel() {
    val tugasKuliahDatabase = tugasKuliahDataSource
    val tugasKuliahToDoListDatabase = tugasKuliahToDoListDataSource
    val tugasKuliahCompletionHistoryDatabase = tugasKuliahCompletionHistoryDataSource

    private val _tugasKuliah = MutableLiveData<TugasKuliah?>()
    val tugasKuliah: LiveData<TugasKuliah?>
        get() = _tugasKuliah
//    var tugas = database.getAllTugasKuliahSortedByDeadlineForeground()
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _tugasKuliahToDoList = MutableLiveData<MutableList<TugasKuliahToDoList>>()
    val tugasKuliahToDoList: LiveData<MutableList<TugasKuliahToDoList>>
        get() = _tugasKuliahToDoList

    private val _navigateToEditTugasKuliah = MutableLiveData<Long>()
    val navigateToEditTugasKuliah: LiveData<Long>
        get() = _navigateToEditTugasKuliah

    private val _navigateToAddTugasKuliah = MutableLiveData<Boolean>()
    val navigateToAddTugasKuliah: LiveData<Boolean>
        get() = _navigateToAddTugasKuliah

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()
    val showSnackbarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    private val _tugasKuliahCompletionHistory_total = MutableLiveData<Int>()
    val tugasKuliahCompletionHistory_total: LiveData<Int>
        get() = _tugasKuliahCompletionHistory_total

    private val _tugasKuliahCompletionHistory_beforeTarget = MutableLiveData<Int>()
    val tugasKuliahCompletionHistory_beforeTarget: LiveData<Int>
        get() = _tugasKuliahCompletionHistory_beforeTarget

    private val _tugasKuliahCompletionHistory_afterTarget = MutableLiveData<Int>()
    val tugasKuliahCompletionHistory_afterTarget: LiveData<Int>
        get() = _tugasKuliahCompletionHistory_afterTarget

    private val _tugasKuliahCompletionHistory_afterDeadline = MutableLiveData<Int>()
    val tugasKuliahCompletionHistory_afterDeadline: LiveData<Int>
        get() = _tugasKuliahCompletionHistory_afterDeadline

    fun loadTugasKuliah()
    {
        uiScope.launch {
            _tugasKuliah.value = tugasKuliahDatabase.getOneTugasKuliahUnfinishedSortedByDeadline()
        }
    }

    fun countTugasKuliahCompletedHistoryData()
    {
        uiScope.launch {
            val realTimeClock = System.currentTimeMillis()
            val sevenDaysBefore = realTimeClock - 604800000
            _tugasKuliahCompletionHistory_total.value = tugasKuliahCompletionHistoryDatabase.getAllDataCount(sevenDaysBefore, realTimeClock)
            _tugasKuliahCompletionHistory_beforeTarget.value = tugasKuliahCompletionHistoryDatabase.getAllFinishedByTypeDataCount("Selesai Tepat Waktu Sebelum Target", sevenDaysBefore, realTimeClock)
            _tugasKuliahCompletionHistory_afterTarget.value = tugasKuliahCompletionHistoryDatabase.getAllFinishedByTypeDataCount("Selesai Tepat Waktu Melewati Target", sevenDaysBefore, realTimeClock)
            _tugasKuliahCompletionHistory_afterDeadline.value = tugasKuliahCompletionHistoryDatabase.getAllFinishedByTypeDataCount("Selesai Terlambat", sevenDaysBefore, realTimeClock)
        }
    }

//    fun clearTugasKuliah()
//    {
//
//    }

    fun loadToDoList(id: Long)
    {
        uiScope.launch {
            _tugasKuliahToDoList.value = tugasKuliahToDoListDatabase.loadToDoListsByTugasKuliahId(id)
        }
    }

    fun doneShowingSnackbar()
    {
        _showSnackbarEvent.value = null
    }

    fun getTugasKuliahAndDate(): List<TugasKuliahListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
//        for (i in tugasKuliah.value!!)
//        {
            var dateCursor: String = convertDeadlineToDateFormatted(tugasKuliah.value!!.deadline)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(tugasKuliah.value!!)
                count++
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        ""
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
                arrayList.add(tugasKuliah.value!!)
                count = 1
                arrayList[temp] =
                    TugasKuliahDate(
                        tempdate,
                        ""
                    )
                //kalau misalkan i++ disini, maka jumlah nya belum ketahuan ada berapa pas munculin header nya
                //bisa sih pakai query load data from deadline xxxx ke xxxx pakai modulus per hari buat nentuin parameter query nya
//            }
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
