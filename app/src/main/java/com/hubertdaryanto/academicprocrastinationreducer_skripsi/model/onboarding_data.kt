package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

object onboarding_data {
    lateinit var mTugas: TugasKuliah
    var mTugasKuliahToDoList: MutableList<TugasKuliahToDoList>? = null
    var mTugasKuliahImages: MutableList<TugasKuliahImage>? = null
    var mSubjectName: String? = null
    var mSubjectId: Long? = null
    var approvalForGoToFourthScreen: Boolean = false
    var approvalForGoToFifthScreen: Boolean = false

}