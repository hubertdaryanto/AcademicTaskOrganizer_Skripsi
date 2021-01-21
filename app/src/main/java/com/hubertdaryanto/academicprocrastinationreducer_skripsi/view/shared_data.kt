package com.hubertdaryanto.academicprocrastinationreducer_skripsi.view

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.ImageForTugas
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.ToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.database.TugasKuliah

object shared_data {
    lateinit var mTugas: TugasKuliah
    var mToDoList: MutableList<ToDoList>? = null
    var mImageForTugas: MutableList<ImageForTugas>? = null
    var mSubjectAtAddTugasFragment: String? = null
    var mSubjectId: Long? = null
}