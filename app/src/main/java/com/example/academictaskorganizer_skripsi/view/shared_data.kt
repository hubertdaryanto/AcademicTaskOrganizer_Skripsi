package com.example.academictaskorganizer_skripsi.view

import com.example.academictaskorganizer_skripsi.database.ImageForTugas
import com.example.academictaskorganizer_skripsi.database.ToDoList
import com.example.academictaskorganizer_skripsi.database.TugasKuliah

object shared_data {
    lateinit var mTugas: TugasKuliah
    var mToDoList: MutableList<ToDoList>? = null
    var mImageForTugas: MutableList<ImageForTugas>? = null
}