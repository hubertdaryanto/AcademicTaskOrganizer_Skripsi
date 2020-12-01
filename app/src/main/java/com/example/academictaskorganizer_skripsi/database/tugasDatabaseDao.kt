package com.example.academictaskorganizer_skripsi.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface tugasDatabaseDao{
    @Query("SELECT * FROM TugasKuliah ORDER BY tugasKuliahName ASC")
    suspend fun getAllSortedByName(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline DESC")
    suspend fun getAllSortedByDeadline(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline DESC")
    fun getAllSortedByDeadlineForeground(): LiveData<List<TugasKuliah>>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId IN (:userIds)")
    suspend fun loadAllByIds(userIds: LongArray): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId LIKE :id")
    suspend fun loadTugasKuliahById(id: Long): TugasKuliah

    @Query("SELECT * FROM TugasKuliah WHERE tugasKuliahName LIKE :name")
    suspend fun findByName(name: String): TugasKuliah

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugas(TugasKuliah: TugasKuliah): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoLists(toDoList: List<ToDoList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(imageForTugas: List<ImageForTugas>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(toDoList: ToDoList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(imageForTugas: ImageForTugas)

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateTugas(TugasKuliah: TugasKuliah)

    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM TugasKuliah WHERE tugasSubjectId  IN (SELECT DISTINCT(subjectId) FROM Subject)")
//    fun getTugasKuliahAndSubject(): LiveData<List<SubjectAndTugasKuliah>>

    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM ToDoList WHERE bindToTugasKuliahId IN (SELECT DISTINCT(tugasKuliahId) FROM TugasKuliah)")
//    fun getTugasKuliahWithToDoList(): LiveData<List<TugasKuliahWithToDoList>>

    @Query("SELECT subjectName FROM Subject WHERE subjectId LIKE :id")
    suspend fun loadSubjectName(id: Long): String
}