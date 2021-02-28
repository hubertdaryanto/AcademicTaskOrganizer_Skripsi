package com.hubertdaryanto.academicprocrastinationreducer_skripsi.model

import androidx.room.*

@Dao
interface userDao{
    @Query("SELECT * FROM User")
    suspend fun loadAllUser(): User


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

}