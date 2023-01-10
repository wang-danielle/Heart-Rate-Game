package com.example.heartrategame.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "level_id")
    var levelId: Long,

    @ColumnInfo(name = "best_score")
    var bestScore: Double,
)