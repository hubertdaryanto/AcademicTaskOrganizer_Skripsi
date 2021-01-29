package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*
import com.hubertdaryanto.academicprocrastinationreducer_skripsi.model.SubjectTugasKuliah

@Dao
interface subjectTugasKuliahDao{
    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    suspend fun loadSubjectNameById(id: Long): String


    @Query("SELECT SubjectName FROM Subject WHERE SubjectId LIKE :id")
    fun loadSubjectNameByIdForeground(id: Long): String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubject(vararg subjectTugasKuliah: SubjectTugasKuliah)

    @Delete
    suspend fun deleteSubject(subjectTugasKuliah: SubjectTugasKuliah)

    @Update
    suspend fun updateSubject(subjectTugasKuliah: SubjectTugasKuliah)
}