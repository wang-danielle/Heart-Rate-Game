package com.example.heartrategame.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: ScoreEntity)

    @Query("SELECT * FROM score_table WHERE level_id = :levelId LIMIT 1")
    suspend fun findScoreForLevel(levelId: Long): ScoreEntity?

    @Update
    suspend fun update(score: ScoreEntity)
}