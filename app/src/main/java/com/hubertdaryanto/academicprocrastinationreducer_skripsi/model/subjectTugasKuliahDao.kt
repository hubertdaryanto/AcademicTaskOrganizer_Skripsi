package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.lifecycle.LiveData
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

    @Query("SELECT subjectName FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    suspend fun loadSubjectTugasKuliahNameById(id: Long): String
    @Query("DELETE FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    suspend fun deleteSubjectTugasKuliahById(id: Long)
    @Query("SELECT * FROM SubjectTugasKuliah ORDER BY SubjectName ASC")
    fun getSubjectTugasKuliahByNameForeground(): LiveData<List<SubjectTugasKuliah>>
}