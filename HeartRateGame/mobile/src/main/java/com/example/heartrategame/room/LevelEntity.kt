package com.example.heartrategame.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.heartrategame.models.LevelDataClass

@Entity(tableName = "level_data_table")
data class LevelEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    @ColumnInfo(name = "level_data")
    var levelData: LevelDataClass,
)