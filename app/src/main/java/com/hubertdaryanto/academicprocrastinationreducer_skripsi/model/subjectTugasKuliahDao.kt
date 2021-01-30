package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah

@Dao
interface subjectTugasKuliahDao{
    @Query("SELECT SubjectName FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    suspend fun loadSubjectNameById(id: Long): String


    @Query("SELECT SubjectName FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    fun loadSubjectNameByIdForeground(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(subjectTugasKuliah: SubjectTugasKuliah)

    @Delete
    suspend fun deleteSubjectTugasKuliah(subjectTugasKuliah: SubjectTugasKuliah)

    @Update
    suspend fun updateSubjectTugasKuliah(subjectTugasKuliah: SubjectTugasKuliah)
}