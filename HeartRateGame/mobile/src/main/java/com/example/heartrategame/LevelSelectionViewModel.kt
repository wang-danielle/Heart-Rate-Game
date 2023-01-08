package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.models.LevelDataClass
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LevelSelectionViewModel(
    private val roomDatabase: LevelDatabase
) : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val storageRef = FirebaseStorage.getInstance().getReference()
    var levelItemAdapter: LevelItemAdapter? = null
    var levels: LiveData<List<LevelEntity>> = roomDatabase.levelDao.getAllLive()
    private var _levelsUpdate = MutableLiveData<Boolean?>()
    val levelsUpdate: LiveData<Boolean?>
        get() = _levelsUpdate

    fun listenForLevels(context: Context?, username: String? = null) {
        GlobalScope.launch {
            val levels = LevelDataClass.getBaseLevels() + roomDatabase.levelDao.getAll().map { it.levelData }

            // Allows base and locally saved levels to be loaded even when there is no network
            levelItemAdapter = context?.let {
                LevelItemAdapter(
                    context = it,
                    levels = levels
                )
            }
            _levelsUpdate.postValue(true)
        }

//        val levelsRef = database.getReference("levels")
//        levelsRef.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                _levelsUpdate.value = false
//                val customLevels = ArrayList<LevelDataClass>()
//                for (level in snapshot.children) {
//                    level.key?.let {
//                        customLevels.add(
//                            LevelDataClass(
//                                name = it,
//                                totalTime = 0, // TODO: fix
//                                imageUri = Uri.parse(level.getValue(String::class.java)),
//                                createdBy = null,
//                            )
//                        )
//                    }
//                }
//                val levels = customLevels + LevelDataClass.getBaseLevels()
//                itemAdapter = context?.let {
//                    ItemAdapter(
//                        context = it,
//                        levels = levels
//                    )
//                }
//                _levelsUpdate.value = true
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//        })
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