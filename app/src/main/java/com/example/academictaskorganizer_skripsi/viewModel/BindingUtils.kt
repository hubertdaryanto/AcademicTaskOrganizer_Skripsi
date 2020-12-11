package com.example.academictaskorganizer_skripsi.viewModel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.academictaskorganizer_skripsi.database.AppDatabase
import com.example.academictaskorganizer_skripsi.database.SubjectAndTugasKuliah
import com.example.academictaskorganizer_skripsi.database.TugasKuliah
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@BindingAdapter("tugasSubjectNameAndDeadlineTime")
fun TextView.setTugasSubjectNameAndDeadline(item: TugasKuliah?)
{
    item?.let {
        var database = AppDatabase.getInstance(context).getSubjectDao
        GlobalScope.launch {
            val text1 = database.loadSubjectNameById(item.tugasSubjectId)
            text =  text1 + " - " + item.deadline.let { it1 ->
                convertDeadlineToTimeFormatted(
                    it1
                )
            }
        }

    }
}