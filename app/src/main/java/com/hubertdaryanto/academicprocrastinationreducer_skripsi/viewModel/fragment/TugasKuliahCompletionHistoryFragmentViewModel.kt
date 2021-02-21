package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.fragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistoryDate
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.tugasKuliahCompletionHistoryDao
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.adapter.TugasKuliahCompletionHistoryListItemType
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel.convertDeadlineToDateFormatted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TugasKuliahCompletionHistoryFragmentViewModel(tugasKuliahCompletionHistoryDataSource: tugasKuliahCompletionHistoryDao, application: Application): ViewModel()  {
    val tugasKuliahCompletionHistoryDatabase = tugasKuliahCompletionHistoryDataSource

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _tugasKuliahCompletionHistories = MutableLiveData<MutableList<TugasKuliahCompletionHistory>>()
    val tugasKuliahCompletionHistories: LiveData<MutableList<TugasKuliahCompletionHistory>>
        get() = _tugasKuliahCompletionHistories

    private val _navigateToViewTugasKuliahCompletionHistoryDetails = MutableLiveData<Long?>()
    val navigateToViewTugasKuliahCompletionHistoryDetails: LiveData<Long?>
        get() = _navigateToViewTugasKuliahCompletionHistoryDetails

    fun loadTugasKuliahCompletionHistory()
    {
        uiScope.launch {
            _tugasKuliahCompletionHistories.value = tugasKuliahCompletionHistoryDatabase.getAllTugasKuliahCompletionHistorySortedByMostRecent()
        }
    }

    fun getTugasKuliahCompletionHistoryDate(): List<TugasKuliahCompletionHistoryListItemType>
    {
        var arrayList = arrayListOf<TugasKuliahCompletionHistoryListItemType>()
        var date: String = ""
        var temp = 0
        var count = 1
        var tempdate = ""
        for (i in tugasKuliahCompletionHistories.value!!)
        {
            var dateCursor: String = convertDeadlineToDateFormatted(i.tugasKuliahCompletionHistoryId)
            if (date.equals(dateCursor, true))
            {
                arrayList.add(i)
                count++
                arrayList[temp] =
                    TugasKuliahCompletionHistoryDate(
                        tempdate,
                        count.toString()
                    )
            }
            else
            {
                date = dateCursor
                tempdate = date
                var taskCompletionHistoryDate =
                    TugasKuliahCompletionHistoryDate(
                        date,
                        "0"
                    )
                arrayList.add(taskCompletionHistoryDate)
                temp = arrayList.count() - 1
                arrayList.add(i)
                count = 1
                arrayList[temp] =
                    TugasKuliahCompletionHistoryDate(
                        tempdate,
                        count.toString()
                    )
                //kalau misalkan i++ disini, maka jumlah nya belum ketahuan ada berapa pas munculin header nya
                //bisa sih pakai query load data from deadline xxxx ke xxxx pakai modulus per hari buat nentuin parameter query nya

            }
        }
        return arrayList
    }

    fun onTugasKuliahCompletionHistoryClicked(id: Long)
    {
        _navigateToViewTugasKuliahCompletionHistoryDetails.value = id
    }

    fun onTugasKuliahCompletionHistoryNavigated()
    {
        _navigateToViewTugasKuliahCompletionHistoryDetails.value = null
    }
}