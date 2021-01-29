package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*


data class SubjectAndTugasKuliah(
    @Embedded val subjectTugasKuliah: SubjectTugasKuliah,
    @Relation(
        parentColumn = "subjectId",
        entityColumn = "tugasSubjectId"
    )
    val tugasKuliah: List<TugasKuliah>
)

data class TugasKuliahWithToDoList(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "bindToTugasKuliahId"
    )
    val tugasKuliahToDoList: List<TugasKuliahToDoList>
)

data class TugasKuliahWithImageForTugas(
    @Embedded val tugasKuliah: TugasKuliah,
    @Relation(
        parentColumn = "tugasKuliahId",
        entityColumn = "bindToTugasKuliahId"
    )
    val tugasKuliahImages: List<TugasKuliahImage>
)