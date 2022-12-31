package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.Exercise

class CustomLevelViewModel : ViewModel() {
    var exercises = mutableListOf<Pair<Exercise, Long>>()
    var exercisesItemAdapter: ExerciseTimeItemAdapter? = null

    private val _exercisesUpdate = MutableLiveData<Boolean?>()
    val exercisesUpdate: LiveData<Boolean?>
        get() = _exercisesUpdate

    fun listenForExercises(context: Context?) {
        exercisesItemAdapter = context?.let {
            ExerciseTimeItemAdapter(
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