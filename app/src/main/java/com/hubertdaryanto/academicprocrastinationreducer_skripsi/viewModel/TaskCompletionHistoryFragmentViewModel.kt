package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TaskCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.allQueryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TaskCompletionHistoryDate
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TaskCompletionHistoryListItemType
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.view.TugasKuliahDate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TaskCompletionHistoryFragmentViewModel(dataSource: allQueryDao, application: Application): ViewModel()  {
    val database = dataSource
//    var taskCompletionHistories = database.getAllTaskCompletionHistorySortedByMostRecentForeground()

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _taskCompletionHistories = MutableLiveData<MutableList<TaskCompletionHistory>>()
    val taskCompletionHistories: LiveData<MutableList<TaskCompletionHistory>>
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

    fun getTaskCompletionHistoryDate(): List<TaskCompletionHistoryListItemType>
    {
        var arrayList = arrayListOf<TaskCompletionHistoryListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
        for (i in taskCompletionHistories.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.taskCompletionHistoryId)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
                count++
                arrayList[temp] = TaskCompletionHistoryDate(tempdate, count.toString())
            }
            else
            {
                date = dateCursor
                tempdate = date
                var taskCompletionHistoryDate = TaskCompletionHistoryDate(date, "0")
                arrayList.add(taskCompletionHistoryDate)
                temp = arrayList.count() - 1
                arrayList.add(i)
                count = 1
                arrayList[temp] = TaskCompletionHistoryDate(tempdate, count.toString())
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