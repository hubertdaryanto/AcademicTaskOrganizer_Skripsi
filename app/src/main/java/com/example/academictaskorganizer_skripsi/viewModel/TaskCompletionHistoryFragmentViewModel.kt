package com.example.academictaskorganizer_skripsi.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.academictaskorganizer_skripsi.database.TaskCompletionHistory
import com.example.academictaskorganizer_skripsi.database.ToDoList
import com.example.academictaskorganizer_skripsi.database.allQueryDao
import com.example.academictaskorganizer_skripsi.view.TaskCompletionHistoryDate
import com.example.academictaskorganizer_skripsi.view.TaskCompletionHistoryListItemType
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

    private val _navigateToViewTaskCompletionHistoryDetails = MutableLiveData<Long>()
    val navigateToViewTaskCompletionHistoryDetails: LiveData<Long>
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
        for (i in taskCompletionHistories.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.taskCompletionHistoryId)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
            }
            else
            {
                date = dateCursor
                var taskCompletionHistoryDate = TaskCompletionHistoryDate(date)
                arrayList.add(taskCompletionHistoryDate)
                arrayList.add(i)
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