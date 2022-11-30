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

class SelectActivityViewModel : ViewModel() {
    private val database = FirebaseDatabase.getInstance()
    private val storageRef = FirebaseStorage.getInstance().getReference()
    var itemAdapter: ItemAdapter? = null

    private val _activitiesUpdate = MutableLiveData<Boolean?>()
    val activitiesUpdate: LiveData<Boolean?>
        get() = _activitiesUpdate

    fun listenForActivities(context: Context?, username: String? = null) {
        val activitiesRef = database.getReference("activities")

        activitiesRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val activityNames = ArrayList<String>()
                val activityImageUris = ArrayList<Uri>()
                for (activity in snapshot.children) {
                    activity.key?.let {
                        activityNames.add(it)
                        val activityImageUri = Uri.parse(activity.getValue(String::class.java))
                        activityImageUris.add(activityImageUri)
                    }
                }
                itemAdapter = context?.let {
                    ItemAdapter(
                        context = it,
                        activityNames = activityNames,
                        activityImageUris = activityImageUris
                    )
                }
                _activitiesUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun resetUpdate() {
        _activitiesUpdate.value = false
    }
}