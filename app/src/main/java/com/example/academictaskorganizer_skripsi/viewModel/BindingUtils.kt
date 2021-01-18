package com.example.academictaskorganizer_skripsi.viewModel

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.TaskCompletionHistory
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import kotlinx.coroutines.*

@BindingAdapter("tugasSubjectNameAndDeadlineTime")
fun TextView.setTugasSubjectNameAndDeadline(item: TugasKuliah?)
{
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    item?.let {
        var database = AppDatabase.getInstance(context).getSubjectDao
        uiScope.launch {
            val text1 = database.loadSubjectNameById(item.tugasSubjectId)
            text =  text1 + " - " + item.deadline.let { it1 ->
                convertDeadlineToTimeFormatted(
                    it1
                )
            }
        }
    }
}

@BindingAdapter("loadTugasKuliahName")
fun TextView.loadTugasKuliahName(taskCompletionHistory: TaskCompletionHistory?)
{
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    taskCompletionHistory?.let {
        var database = AppDatabase.getInstance(context).getAllQueryListDao
        uiScope.launch {
            val text1 = database.loadTugasKuliahById(it.bindToTugasKuliahId)
            text =  text1.tugasKuliahName + " - " + it.taskCompletionHistoryId.let { it1 ->
                convertDeadlineToTimeFormatted(it1)
            }
        }
    }
}