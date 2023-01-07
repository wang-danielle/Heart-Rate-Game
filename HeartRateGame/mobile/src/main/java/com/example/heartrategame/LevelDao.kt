package com.example.heartrategame

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LevelDao {
    @Insert
    fun insert(level: LevelEntity)

    @Query("SELECT * FROM level_data_table")
    fun getAll(): List<LevelEntity>
}