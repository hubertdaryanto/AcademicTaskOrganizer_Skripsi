package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*

@Dao
interface tugasKuliahImageDao {
    @Query("SELECT * FROM TugasKuliahImage WHERE bindToTugasKuliahId LIKE :id")
    suspend fun loadImagesByTugasKuliahId(id: Long): MutableList<TugasKuliahImage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahImages(tugasKuliahImages: List<TugasKuliahImage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTugasKuliahImage(tugasKuliahImage: TugasKuliahImage)

    @Query("DELETE FROM TugasKuliahImage WHERE tugasKuliahImageId LIKE :id")
    suspend fun deleteTugasKuliahImageById(id: Long)

    @Update
    suspend fun updateTugasKuliahImages(imageKuliahImages: List<TugasKuliahImage>)
}