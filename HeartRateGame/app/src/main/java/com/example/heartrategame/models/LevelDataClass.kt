package com.example.heartrategame.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LevelDataClass(
    val name: String,
    val imageUri: Uri,
    var time: Long? = null,
    val createdBy: String? = null,
): Parcelable
