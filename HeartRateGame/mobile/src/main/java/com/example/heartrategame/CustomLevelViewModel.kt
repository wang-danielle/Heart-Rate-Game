package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.Exercise

class CustomLevelViewModel : ViewModel() {
    val exercises = mutableListOf<Pair<Exercise, Long>>()
    var exercisesItemAdapter: ExerciseItemAdapter? = null

    private val _exercisesUpdate = MutableLiveData<Boolean?>()
    val exercisesUpdate: LiveData<Boolean?>
        get() = _exercisesUpdate

    fun listenForExercises(context: Context?) {
        exercisesItemAdapter = context?.let {
            ExerciseItemAdapter(
                context = it,
                exercises = exercises,
            )
        }

        _exercisesUpdate.value = true
    }

    fun resetUpdate() {
        _exercisesUpdate.value = false
    }
}