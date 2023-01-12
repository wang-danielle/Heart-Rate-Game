package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.adapters.FriendRequestsItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendRequestsViewModel(
    private val myUsername: String
): ViewModel() {
    private var usersRef = FirebaseDatabase.getInstance().getReference("Users")
    private var friendRequestsRef = usersRef.child(myUsername).child("FriendRequests")
    var friendRequestsItemAdapter: FriendRequestsItemAdapter? = null

    private val _friendRequestsUpdate = MutableLiveData<Boolean?>()
    val friendRequestsUpdate: LiveData<Boolean?>
        get() = _friendRequestsUpdate

    fun listenForFriendRequests(context: Context?) {
        friendRequestsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val requests = dataSnapshot.children.mapNotNull {
                    it.key
                }.toMutableList()

                friendRequestsItemAdapter = context?.let {
                    FriendRequestsItemAdapter(
                        context = it,
                        requests = requests
                    )
                }
                _friendRequestsUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun resetUpdate() {
        _friendRequestsUpdate.value = false
    }

    class Factory(private val username: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java)
                .newInstance(username)
        }
    }
}