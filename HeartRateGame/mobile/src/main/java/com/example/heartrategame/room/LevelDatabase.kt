package com.example.heartrategame.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LevelEntity::class], version = 2, exportSchema = false)
@TypeConverters(LevelTypeConverter::class)
abstract class LevelDatabase: RoomDatabase() {
    abstract val levelDao: LevelDao
    companion object {
        @Volatile
        private var INSTANCE: LevelDatabase? = null

        fun getInstance(context: Context): LevelDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LevelDatabase::class.java,
                        "level_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}