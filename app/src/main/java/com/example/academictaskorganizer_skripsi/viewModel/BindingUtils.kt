package com.example.academictaskorganizer_skripsi.viewModel

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.academictaskorganizer_skripsi.database.TugasKuliah

@BindingAdapter("tugasSubjectNameAndDeadlineTime")
fun TextView.setTugasSubjectNameAndDeadline(item: TugasKuliah?)
{
    item?.let {
        text = item.SubjectId.toString() + " - " + item.Deadline?.let { it1 ->
            convertDeadlineToTimeFormatted(
                it1
            )
        }
    }
}