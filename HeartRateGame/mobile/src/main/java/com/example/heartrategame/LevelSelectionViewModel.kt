package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.adapters.LevelItemAdapter
import com.example.heartrategame.models.LevelDataClass
import com.example.heartrategame.room.LevelDatabase
import com.example.heartrategame.room.LevelEntity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LevelSelectionViewModel(
    private val roomDatabase: LevelDatabase
) : ViewModel() {
    var levelItemAdapter: LevelItemAdapter? = null
    var levels: LiveData<List<LevelEntity>> = roomDatabase.levelDao.getAllLive()
    private var _levelsUpdate = MutableLiveData<Boolean?>()
    val levelsUpdate: LiveData<Boolean?>
        get() = _levelsUpdate

    fun listenForLevels(context: Context?, loadedLevels: List<LevelEntity>? = null) {
        val baseLevels = LevelDataClass.getBaseLevels().map {
            LevelEntity(
                id = -(it.exercises[0].first.ordinal + 1).toLong(),
                levelData = it
            )
        }
        if (loadedLevels != null) {
            // Used when LevelDao.getAllLive is triggered to avoid requerying
            val levels = baseLevels + loadedLevels

            levelItemAdapter = context?.let {
                LevelItemAdapter(
                    context = it,
                    levels = levels
                )
            }
            _levelsUpdate.postValue(true)
            return
        }
        GlobalScope.launch {
            val levels = baseLevels + roomDatabase.levelDao.getAll()

            levelItemAdapter = context?.let {
                LevelItemAdapter(
                    context = it,
                    levels = levels
                )
            }
            _levelsUpdate.postValue(true)
        }
    }

    fun resetUpdate() {
        _levelsUpdate.value = false
    }

    class Factory(private val database: LevelDatabase): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(LevelDatabase::class.java)
                .newInstance(database)
        }
    }
}