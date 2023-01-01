package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.Exercise

class ExerciseSelectionViewModel : ViewModel() {
    val exercises = Exercise.values()
    var exercisesItemAdapter: ExerciseItemAdapter? = null

    fun setupExercisesItemAdapter(context: Context?) {
        exercisesItemAdapter = context?.let {
            ExerciseItemAdapter(
                context = it,
                exercises = exercises,
            )
        }
    }
}