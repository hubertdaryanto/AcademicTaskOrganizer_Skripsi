package com.example.academictaskorganizer_skripsi.view

import android.graphics.Paint
import android.view.MenuItem
import android.widget.TextView
import androidx.databinding.BindingAdapter

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
}


@BindingAdapter("strikeThrough")
fun strikeThrough(textView: TextView, strikeThrough: Boolean) {
    if (strikeThrough) {
        textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    } else {
        textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }
}