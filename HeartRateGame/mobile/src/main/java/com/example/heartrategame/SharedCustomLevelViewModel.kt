package com.example.heartrategame

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.Exercise
import com.example.heartrategame.models.LevelDataClass

class SharedCustomLevelViewModel: ViewModel() {
    private val _levelData = MutableLiveData(LevelDataClass(name = "", totalTime = 0))
    val levelData: LiveData<LevelDataClass>
        get() = _levelData

    fun resetLevel() {
        _levelData.value = LevelDataClass(name = "", totalTime = 0)
    }

    fun addExercise(exercise: Exercise, time: Long) {
        _levelData.value?.exercises?.add(Pair<Exercise, Long>(exercise, time))
    }

    fun setImage(imageUri: Uri?) {
        _levelData.value?.imageUri = imageUri
    }
}