package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahImage
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliahToDoList
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.TugasKuliah

object shared_data {
    lateinit var mTugas: TugasKuliah
    var mTugasKuliahToDoList: MutableList<TugasKuliahToDoList>? = null
    var mTugasKuliahImages: MutableList<TugasKuliahImage>? = null
    var mSubjectAtAddTugasFragment: String? = null
    var mSubjectId: Long? = null
    var fromFragment = ""
    var toDoListFinished: Boolean = false
}