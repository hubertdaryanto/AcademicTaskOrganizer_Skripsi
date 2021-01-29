package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.allQueryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TugasKuliahCompletionHistoryDate
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TugasKuliahCompletionHistoryListItemType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TugasKuliahCompletionHistoryFragmentViewModel(dataSource: allQueryDao, application: Application): ViewModel()  {
    val database = dataSource
//    var taskCompletionHistories = database.getAllTaskCompletionHistorySortedByMostRecentForeground()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _taskCompletionHistories = MutableLiveData<MutableList<TugasKuliahCompletionHistory>>()
    val taskCompletionHistories: LiveData<MutableList<TugasKuliahCompletionHistory>>
        get() = _taskCompletionHistories

    private val _navigateToViewTaskCompletionHistoryDetails = MutableLiveData<Long?>()
    val navigateToViewTaskCompletionHistoryDetails: LiveData<Long?>
        get() = _navigateToViewTaskCompletionHistoryDetails

    fun loadTaskCompletionHistory()
    {
        uiScope.launch {
            _taskCompletionHistories.value = database.getAllTaskCompletionHistorySortedByMostRecentForeground()
        }
    }

    fun getTaskCompletionHistoryDate(): List<TugasKuliahCompletionHistoryListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahCompletionHistoryListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
        for (i in taskCompletionHistories.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.tugasKuliahCompletionHistoryId)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
                count++
                arrayList[temp] = TugasKuliahCompletionHistoryDate(tempdate, count.toString())
            }
            else
            {
                date = dateCursor
                tempdate = date
                var taskCompletionHistoryDate = TugasKuliahCompletionHistoryDate(date, "0")
                arrayList.add(taskCompletionHistoryDate)
                temp = arrayList.count() - 1
                arrayList.add(i)
                count = 1
                arrayList[temp] = TugasKuliahCompletionHistoryDate(tempdate, count.toString())
                //kalau misalkan i++ disini, maka jumlah nya belum ketahuan ada berapa pas munculin header nya
                //bisa sih pakai query load data from deadline xxxx ke xxxx pakai modulus per hari buat nentuin parameter query nya

            }
        }
        return arrayList
    }

    fun onTaskCompletionHistoryClicked(id: Long) {
        _navigateToViewTaskCompletionHistoryDetails.value = id
    }

    fun onTaskCompletionHistoryNavigated()
    {
        _navigateToViewTaskCompletionHistoryDetails.value = null
    }
}