package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.adapters.ScoresItemAdapter
import com.example.heartrategame.models.Exercise
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendProfileViewModel(
    username: String
) : ViewModel() {
    private val scoresRef = FirebaseDatabase.getInstance()
        .getReference("Users")
        .child(username)
        .child("Scores")

    var scoresItemAdapter: ScoresItemAdapter? = null

    private val _scoresUpdate = MutableLiveData<Boolean?>()
    val scoresUpdate: LiveData<Boolean?>
        get() = _scoresUpdate

    fun listenForScores(context: Context?) {
        scoresRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val scoreForLevel = mutableMapOf<Int, String?>()
                dataSnapshot.children.forEach {
                    it.key?.let { levelId ->
                        val ordinal = -levelId.toInt() - 1
                        scoreForLevel[ordinal] = it.value?.toString()
                    }
                }

                val levels = Exercise.values().map {
                    Pair(it, scoreForLevel[it.ordinal] ?: "N/A")
                }

                scoresItemAdapter = context?.let {
                    ScoresItemAdapter(
                        context = it,
                        levels = levels
                    )
                }
                _scoresUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun resetUpdate() {
        _scoresUpdate.value = false
    }

    class Factory(private val username: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java)
                .newInstance(username)
        }
    }
}