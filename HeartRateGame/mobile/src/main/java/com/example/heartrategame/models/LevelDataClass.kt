package com.example.heartrategame.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelDataClass(
    var name: String,
    val exercises: MutableList<Pair<Exercise, Long>> = mutableListOf(),
    var totalTime: Long,
    var imageUriPath: String? = null,
    val createdBy: String? = null,
): Parcelable {
    var imageUri: Uri?
        get() = imageUriPath?.let {
            Uri.parse(it)
        }
        set(value) {
            imageUriPath = value?.toString()
        }
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
