package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

object shared_data {
    lateinit var mTugas: TugasKuliah
    var mTugasKuliahToDoList: MutableList<TugasKuliahToDoList>? = null
    var mTugasKuliahImages: MutableList<TugasKuliahImage>? = null
    var mSubjectAtAddTugasFragment: String? = null
    var mSubjectId: Long? = null
    var fromFragment = ""
    var toDoListFinished: Boolean = false
}