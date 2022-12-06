package com.example.heartrategame.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelDataClass(
    val name: String,
    val exercises: MutableList<Pair<Exercise, Long>> = mutableListOf(),
    var totalTime: Long,
    val imageUri: Uri? = null,
    val createdBy: String? = null,
): Parcelable {
    companion object {
        fun getBaseLevels(): List<LevelDataClass> {
            return Exercise.values().map {
                LevelDataClass(
                    name = it.title,
                    totalTime = 0,
                    exercises = mutableListOf(Pair(it, 0)),
                )
            }
        }
    }
}
