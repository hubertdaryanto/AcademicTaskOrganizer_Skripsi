package com.example.academictaskorganizer_skripsi.viewModel

import java.text.SimpleDateFormat
import java.util.*

fun convertDeadlineToTimeFormatted(time: Long): String {
    val weekdayString = SimpleDateFormat("H:mm", Locale.getDefault()).format(time)
    return weekdayString
}

fun convertDeadlineToDateFormatted(time: Long): String {
    val string = SimpleDateFormat("d MMMM YYYY", Locale.getDefault()).format(time)
    return string
}
