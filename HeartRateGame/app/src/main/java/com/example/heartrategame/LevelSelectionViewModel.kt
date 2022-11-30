package com.example.heartrategame

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

        levelsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val levelNames = ArrayList<String>()
                val levelImageUris = ArrayList<Uri>()
                for (level in snapshot.children) {
                    level.key?.let {
                        levelNames.add(it)
                        val levelImageUri = Uri.parse(level.getValue(String::class.java))
                        levelImageUris.add(levelImageUri)
                    }
                }
                itemAdapter = context?.let {
                    ItemAdapter(
                        context = it,
                        levelNames = levelNames,
                        levelImageUris = levelImageUris
                    )
                }
                _levelsUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun resetUpdate() {
        _levelsUpdate.value = false
    }
}