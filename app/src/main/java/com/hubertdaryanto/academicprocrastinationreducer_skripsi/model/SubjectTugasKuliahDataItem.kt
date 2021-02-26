package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

sealed class SubjectTugasKuliahDataItem {
    abstract val id: Long
    data class SubjectTugasKuliahItem(val subjectTugasKuliah: SubjectTugasKuliah): SubjectTugasKuliahDataItem(){
        override val id = subjectTugasKuliah.subjectTugasKuliahId
    }

    object Header: SubjectTugasKuliahDataItem(){
        override val id = Long.MIN_VALUE
    }

    object AddSubject: SubjectTugasKuliahDataItem() {
        override val id: Long
            get() = Long.MIN_VALUE
    }


}