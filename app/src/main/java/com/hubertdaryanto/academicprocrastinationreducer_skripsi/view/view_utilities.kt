package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import android.graphics.Paint
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.SimpleDateFormat

object view_utilities {
    fun menuIconColor(menuItem: MenuItem, color: Int)
    {
        var drawable = menuItem.icon
        if (drawable != null)
        {
            drawable.mutate()
            drawable.setTint(color)
        }
    }

    fun convertDateAndTimeToLong(date: String, time: String): Long {
        val formatter = SimpleDateFormat("dd - MM - yyyy H:mm")
        val date = formatter.parse(date + " " + time)
        return date.time
    }


}


@BindingAdapter("strikeThrough")
fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
    if (strikeThrough) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}