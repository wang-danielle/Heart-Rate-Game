package com.example.heartrategame.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScoreDataClass(
    val minHeartrate: Int,
    val maxHeartrate: Int,
    val avgHeartrate: Double
) : Parcelable {
    val totalScore: Double
        get() = minHeartrate + maxHeartrate + avgHeartrate
}