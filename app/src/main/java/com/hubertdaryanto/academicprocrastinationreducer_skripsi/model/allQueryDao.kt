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
    suspend fun getAllTugasKuliahSortedByDeadlineForeground(): MutableList<TugasKuliah>

    @Query("SELECT * FROM TugasKuliahCompletionHistory ORDER BY tugasKuliahCompletionHistoryId DESC")
    suspend fun getAllTaskCompletionHistorySortedByMostRecentForeground(): MutableList<TugasKuliahCompletionHistory>

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
    suspend fun getTaskCompletionHistoryByTugasKuliahId(id: Long): TugasKuliahCompletionHistory

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugas(TugasKuliah: TugasKuliah): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoLists(tugasKuliahToDoList: List<TugasKuliahToDoList>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImages(tugasKuliahImages: List<TugasKuliahImage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToDoList(tugasKuliahToDoList: TugasKuliahToDoList)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(tugasKuliahImage: TugasKuliahImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Delete
    suspend fun deleteTugas(TugasKuliah: TugasKuliah)

    @Delete
    suspend fun deleteTaskCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

    @Query("DELETE FROM TugasKuliahToDoList WHERE toDoListId LIKE :id")
    suspend fun deleteToDoList(id: Long)

    @Query("DELETE FROM TugasKuliahImage WHERE imageId LIKE :id")
    suspend fun deleteImage(id: Long)

    @Update
    suspend fun updateTugas(TugasKuliah: TugasKuliah)

    @Update
    suspend fun updateListOfToDoList(tugasKuliahToDoList: List<TugasKuliahToDoList>)

    @Update
    suspend fun updateListOfImages(imageKuliahImages: List<TugasKuliahImage>)

    @Update
    suspend fun updateTaskCompletionHistory(taskCompletionHistory: TugasKuliahCompletionHistory)

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
    @Query("DELETE FROM Subject WHERE subjectId LIKE :id")
    suspend fun deleteSubjectById(id: Long)
    @Query("SELECT * FROM Subject ORDER BY SubjectName ASC")
    fun getSubjectByNameForeground(): LiveData<List<SubjectTugasKuliah>>
}