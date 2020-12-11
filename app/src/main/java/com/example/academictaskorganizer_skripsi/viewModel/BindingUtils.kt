package com.example.academictaskorganizer_skripsi.viewModel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.academictaskorganizer_skripsi.database.SubjectAndTugasKuliah
import com.example.academictaskorganizer_skripsi.database.TugasKuliah

@BindingAdapter("tugasSubjectNameAndDeadlineTime")
fun TextView.setTugasSubjectNameAndDeadline(item: SubjectAndTugasKuliah?)
{
    item?.let {
        text = item.subject.subjectName.toString() + " - " + item.tugasKuliah.deadline.let { it1 ->
            convertDeadlineToTimeFormatted(
                it1
            )
        }
    }
}