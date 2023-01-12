package com.example.heartrategame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.room.ScoreDatabase
import com.example.heartrategame.room.ScoreEntity
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val roomDatabase: ScoreDatabase,
    private val levelId: Long,
    private val username: String?
) : ViewModel() {
    private val _bestScore = MutableLiveData<Double>()
    var isNewBestScore = false
    val bestScore: LiveData<Double>
        get() = _bestScore

    fun fetchAndUpdateBestScore(currScore: Double) {
        GlobalScope.launch {
            val scoreEntity = roomDatabase.scoreDao.findScoreForLevel(levelId)
            if (scoreEntity == null) {
                isNewBestScore = true
                _bestScore.postValue(currScore)
                roomDatabase.scoreDao.insert(
                    ScoreEntity(
                        levelId = levelId,
                        bestScore = currScore
                    )
                )
                postScoreToDatabase(levelId)
                return@launch
            }

            val storedBestScore = scoreEntity.bestScore
            if (currScore < storedBestScore) {
                isNewBestScore = true
                _bestScore.postValue(currScore)
                scoreEntity.bestScore = currScore
                roomDatabase.scoreDao.update(
                    scoreEntity
                )
            } else {
                _bestScore.postValue(storedBestScore)
            }
            postScoreToDatabase(levelId)
        }
    }

    private fun postScoreToDatabase(levelId: Long) {
        if (username == null) {
            return
        }
        val scoresRef = FirebaseDatabase.getInstance()
            .getReference("Users")
            .child(username)
            .child("Scores")
        if (levelId < 0) {
            scoresRef.child(levelId.toString()).setValue(_bestScore.value)
        }
    }

    class Factory(
        private val database: ScoreDatabase,
        private val levelId: Long,
        private val username: String?
        ): ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ResultsViewModel(
                database,
                levelId,
                username
            ) as T
        }
    }
}