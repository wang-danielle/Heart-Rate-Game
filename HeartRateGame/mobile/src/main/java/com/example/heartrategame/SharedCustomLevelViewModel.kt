package com.example.heartrategame

import android.net.Uri
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.heartrategame.models.Exercise
import com.example.heartrategame.models.LevelDataClass
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SharedCustomLevelViewModel(
    val database: LevelDatabase,
): ViewModel() {
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

    fun saveLevel(name: String) {
        _levelData.value?.let {
            it.name = name
            GlobalScope.launch {
                val levelEntity = LevelEntity(levelData = it)
                database.levelDao.insert(levelEntity)
            }
        }
    }

    class Factory(private val database: LevelDatabase): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(LevelDatabase::class.java)
                .newInstance(database)
        }
    }
}