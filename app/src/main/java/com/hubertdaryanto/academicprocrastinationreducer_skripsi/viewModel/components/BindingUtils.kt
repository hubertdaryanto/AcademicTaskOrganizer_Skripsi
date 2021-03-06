package com.hubertdaryanto.academicprocrastinationreducer_skripsi.viewModel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.AppDatabase
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahCompletionHistory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@BindingAdapter("tugasSubjectNameAndDeadlineTime")
fun TextView.setTugasSubjectNameAndDeadline(item: TugasKuliah?)
{
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    item?.let {
        var database = AppDatabase.getInstance(context).getSubjectTugasKuliahDao
        uiScope.launch {
            val text1 = database.loadSubjectNameById(item.tugasKuliahSubjectId)
            text =  text1 + " - " + item.deadline.let { it1 ->
                convertDeadlineToTimeFormatted(
                    it1
                )
            }
        }
    }
}

@BindingAdapter("loadTugasKuliahName")
fun TextView.loadTugasKuliahName(taskCompletionHistory: TugasKuliahCompletionHistory?)
{
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    taskCompletionHistory?.let {
        var database = AppDatabase.getInstance(context).getTugasKuliahDao
        uiScope.launch {
            val text1: TugasKuliah? = database.loadTugasKuliahById(it.bindToTugasKuliahId)
            if (text1 != null)
            {
                text =  text1.tugasKuliahName + " - " + it.tugasKuliahCompletionHistoryId.let { it1 ->
                    convertDeadlineToTimeFormatted(it1)
                }
            }
        }
    }
}