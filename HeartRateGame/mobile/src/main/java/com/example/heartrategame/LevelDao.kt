package com.example.heartrategame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LevelDao {
    @Insert
    suspend fun insert(level: LevelEntity)

    @Query("SELECT * FROM level_data_table")
    suspend fun getAll(): List<LevelEntity>

    @Query("DELETE FROM level_data_table")
    suspend fun clear()
}