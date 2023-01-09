package com.example.heartrategame.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.RoundingMode

@Parcelize
data class ScoreDataClass(
    val minHeartrate: Int,
    val maxHeartrate: Int,
    val avgHeartrate: Double
) : Parcelable {
    // Even though avg is rounded, adding seems to be able to introduce rounding error again, so round off the total
    val totalScore: Double
        get() = (minHeartrate + maxHeartrate + avgHeartrate).toBigDecimal().setScale(2, RoundingMode.HALF_UP).toDouble()
}