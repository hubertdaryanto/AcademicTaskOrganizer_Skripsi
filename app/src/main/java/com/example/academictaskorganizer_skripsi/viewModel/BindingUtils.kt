package com.example.academictaskorganizer_skripsi.viewModel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.SubjectAndTugasKuliah
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
fun TextView.loadTugasKuliahName(tugasKuliahId: Long)
{
    val job = Job()
    val uiScope = CoroutineScope(Dispatchers.Main + job)
    tugasKuliahId?.let {
        var database = AppDatabase.getInstance(context).getAllQueryListDao
        uiScope.launch {
            val text1 = database.loadTugasKuliahNameById(it)
            text =  text1
        }
    }
}