package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.Dao

@Dao
interface allQueryDao{
    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM TugasKuliah WHERE tugasSubjectId  IN (SELECT DISTINCT(subjectId) FROM Subject)")
//    fun getTugasKuliahAndSubject(): LiveData<List<SubjectAndTugasKuliah>>

    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM ToDoList WHERE bindToTugasKuliahId IN (SELECT DISTINCT(tugasKuliahId) FROM TugasKuliah)")
//    fun getTugasKuliahWithToDoList(): LiveData<List<TugasKuliahWithToDoList>>
}