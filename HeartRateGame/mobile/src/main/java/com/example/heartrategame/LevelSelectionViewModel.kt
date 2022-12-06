package com.example.heartrategame

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heartrategame.models.LevelDataClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class LevelSelectionViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val storageRef = FirebaseStorage.getInstance().getReference()
    var itemAdapter: ItemAdapter? = null

    private val _levelsUpdate = MutableLiveData<Boolean?>()
    val levelsUpdate: LiveData<Boolean?>
        get() = _levelsUpdate

    fun listenForLevels(context: Context?, username: String? = null) {
        val levelsRef = database.getReference("levels")

        // Allows base levels to be loaded even when there is no network
        itemAdapter = context?.let {
            ItemAdapter(
                context = it,
                levels = LevelDataClass.getBaseLevels()
            )
        }
        _levelsUpdate.value = true

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
}