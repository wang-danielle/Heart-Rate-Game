package com.example.heartrategame

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.heartrategame.models.LevelDataClass
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class LevelTypeConverter {
    @TypeConverter
    fun fromLevelData(levelData: LevelDataClass): String {
        val gson = Gson()
        val type = object : TypeToken<LevelDataClass>() {}.type
        return gson.toJson(levelData, type)
    }

    @TypeConverter
    fun toLevelData(levelDataJson: String): LevelDataClass {
        val gson = Gson()
        val type = object: TypeToken<LevelDataClass>() {}.type
        return gson.fromJson(levelDataJson, type)
    }
}