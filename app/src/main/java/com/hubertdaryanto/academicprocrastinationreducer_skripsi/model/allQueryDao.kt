package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.lifecycle.LiveData
import androidx.room.*

//todo : Dao dipisah jadi masing-masing data class ya, jangan begini (after kelar kumpul softcover aja)
@Dao
interface allQueryDao{
    @Query("SELECT * FROM TugasKuliah ORDER BY tugasKuliahName ASC")
    suspend fun getAllSortedByName(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE isFinished LIKE 0")
    suspend fun loadAllTugasKuliahUnfinished(): List<TugasKuliah>

    @Query("SELECT tugasKuliahName FROM TugasKuliah")
    suspend fun loadAllTugasKuliahName(): List<String>

    @Query("SELECT * FROM TugasKuliah ORDER BY deadline ASC")
    suspend fun getAllSortedByDeadline(): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE isFinished LIKE 0 ORDER BY deadline ASC")
    suspend fun getAllTugasKuliahUnfinishedSortedByDeadline(): MutableList<TugasKuliah>

    @Query("SELECT * FROM TugasKuliahCompletionHistory ORDER BY tugasKuliahCompletionHistoryId DESC")
    suspend fun getAllTugasKuliahCompletionHistorySortedByMostRecent(): MutableList<TugasKuliahCompletionHistory>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId IN (:userIds)")
    suspend fun loadAllByIds(userIds: LongArray): List<TugasKuliah>

    @Query("SELECT * FROM TugasKuliah WHERE TugasKuliahId LIKE :id")
    suspend fun loadTugasKuliahById(id: Long): TugasKuliah

    @Query("SELECT tugasKuliahName FROM TugasKuliah WHERE TugasKuliahId LIKE :id")
    suspend fun loadTugasKuliahNameById(id: Long): String

    @Query("SELECT * FROM TugasKuliah WHERE tugasKuliahName LIKE :name")
    suspend fun findByName(name: String): TugasKuliah

    @Query("SELECT * FROM TugasKuliahToDoList WHERE bindToTugasKuliahId LIKE :id")
    suspend fun loadToDoListsByTugasKuliahId(id: Long): MutableList<TugasKuliahToDoList>

    @Query("SELECT * FROM TugasKuliahImage WHERE bindToTugasKuliahId LIKE :id")
    suspend fun loadImagesByTugasKuliahId(id: Long): MutableList<TugasKuliahImage>

    @Query("SELECT * FROM TugasKuliahCompletionHistory WHERE bindToTugasKuliahId LIKE :id")
    suspend fun getTugasKuliahCompletionHistoryByTugasKuliahId(id: Long): TugasKuliahCompletionHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliah(TugasKuliah: TugasKuliah): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahToDoLists(tugasKuliahToDoList: List<TugasKuliahToDoList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahImages(tugasKuliahImages: List<TugasKuliahImage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahImage(tugasKuliahImage: TugasKuliahImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Delete
    suspend fun deleteTugasKuliahCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Query("DELETE FROM TugasKuliahToDoList WHERE tugasKuliahToDoListId LIKE :id")
    suspend fun deleteTugasKuliahToDoListById(id: Long)

    @Query("DELETE FROM TugasKuliahImage WHERE tugasKuliahImageId LIKE :id")
    suspend fun deleteTugasKuliahImageById(id: Long)

    @Update
    suspend fun updateTugasKuliah(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateTugasKuliahToDoLists(tugasKuliahToDoList: List<TugasKuliahToDoList>)

    @Update
    suspend fun updateTugasKuliahImages(imageKuliahImages: List<TugasKuliahImage>)

    @Update
    suspend fun updateTugasKuliahCompletionHistory(tugasKuliahCompletionHistory: TugasKuliahCompletionHistory)

    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM TugasKuliah WHERE tugasSubjectId  IN (SELECT DISTINCT(subjectId) FROM Subject)")
//    fun getTugasKuliahAndSubject(): LiveData<List<SubjectAndTugasKuliah>>

    //one to many relationship
//    @Transaction
//    @Query("SELECT * FROM ToDoList WHERE bindToTugasKuliahId IN (SELECT DISTINCT(tugasKuliahId) FROM TugasKuliah)")
//    fun getTugasKuliahWithToDoList(): LiveData<List<TugasKuliahWithToDoList>>

    @Query("SELECT subjectName FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    suspend fun loadSubjectTugasKuliahNameById(id: Long): String
    @Query("DELETE FROM SubjectTugasKuliah WHERE subjectTugasKuliahId LIKE :id")
    suspend fun deleteSubjectTugasKuliahById(id: Long)
    @Query("SELECT * FROM SubjectTugasKuliah ORDER BY SubjectName ASC")
    fun getSubjectTugasKuliahByNameForeground(): LiveData<List<SubjectTugasKuliah>>
}