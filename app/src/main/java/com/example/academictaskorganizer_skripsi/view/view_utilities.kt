package com.example.academictaskorganizer_skripsi.view

import android.view.MenuItem

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