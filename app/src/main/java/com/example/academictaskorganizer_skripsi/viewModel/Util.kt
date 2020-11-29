package com.example.academictaskorganizer_skripsi.viewModel

import java.text.SimpleDateFormat
import java.util.*

fun convertDeadlineToTimeFormatted(time: Long): String {
    val weekdayString = SimpleDateFormat("H:mm", Locale.getDefault()).format(time)
    return weekdayString
}

