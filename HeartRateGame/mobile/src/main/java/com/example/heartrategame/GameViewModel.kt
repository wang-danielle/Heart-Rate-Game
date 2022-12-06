package com.example.heartrategame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.Exercise
import com.example.heartrategame.models.LevelDataClass
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.PutDataMapRequest

class GameViewModel : ViewModel() {
    lateinit var dataClient: DataClient
    lateinit var level: LevelDataClass // TODO: factory instead of lateinit
    val MILLIS_PER_SEC = 1000L // TODO: duplicated in fragment
    private var exerciseIndex = 0;
    private var nextExerciseTime = -1L;

    private val _currentExercise = MutableLiveData<Exercise>()
    val currentExercise: LiveData<Exercise>
        get() = _currentExercise

    fun setFirstExercise() {
        exerciseIndex = 0
        nextExerciseTime = level.totalTime - level.exercises[0].second
        _currentExercise.value = level.exercises[exerciseIndex].first

        val request = PutDataMapRequest.create("/newExerciseRequest").run {
            val timeLeftText = formatTime(level.totalTime * MILLIS_PER_SEC)
            dataMap.putString("timeLeft", timeLeftText)

            dataMap.putInt("newExercise", _currentExercise.value!!.ordinal)
            asPutDataRequest()
        }
        request.setUrgent()
        dataClient.putDataItem(request)
    }

    fun setNextExercise() {
        exerciseIndex++
        nextExerciseTime -= level.exercises[exerciseIndex].second
        _currentExercise.value = level.exercises[exerciseIndex].first
    }

    fun sendTimeToWear(msLeft: Long) {
        val request = PutDataMapRequest.create("/newTimeRequest").run {
            val timeLeftText = formatTime(msLeft)
            dataMap.putString("timeLeft", timeLeftText)

            if ((msLeft / MILLIS_PER_SEC) < nextExerciseTime) {
                setNextExercise()
                currentExercise.value?.let {
                    // Pass the new exercise's ordinal to wear so it can update title and image
                    dataMap.putInt("newExercise", it.ordinal)
                }
            }

            asPutDataRequest()
        }

        request.setUrgent()
        val putTask = dataClient.putDataItem(request)
    }

    fun sendQuitToWear() {
        val request = PutDataMapRequest.create("/quitRequest").run {
            dataMap.putLong("now", System.currentTimeMillis())
            asPutDataRequest()
        }
        request.setUrgent()
        dataClient.putDataItem(request)
    }

    fun sendResultsToWear() {
        val request = PutDataMapRequest.create("/resultsRequest").run {
            dataMap.putLong("now", System.currentTimeMillis())
            dataMap.putInt("score", 123) // TODO: temp score, replace when score implemented
            asPutDataRequest()
        }
        request.setUrgent()
        dataClient.putDataItem(request)
    }

    fun formatTime(msLeft: Long): String {
        val secsLeft = msLeft / MILLIS_PER_SEC
        val mins = secsLeft / 60
        val secs = secsLeft % 60
        return "$mins:${secs.toString().padStart(2, '0')}"
    }
}