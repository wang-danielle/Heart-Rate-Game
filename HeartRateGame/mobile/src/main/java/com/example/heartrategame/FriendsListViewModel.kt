package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.adapters.FriendsItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FriendsListViewModel(
    private val username: String
) : ViewModel() {
    private val friendsRef = FirebaseDatabase.getInstance()
        .getReference("Users")
        .child(username)
        .child("Friends")

    var friendsItemAdapter: FriendsItemAdapter? = null

    private val _friendsUpdate = MutableLiveData<Boolean?>()
    val friendsUpdate: LiveData<Boolean?>
        get() = _friendsUpdate

    fun listenForFriends(context: Context?) {
        friendsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val friends = dataSnapshot.children.map {
                    it.value.toString()
                }

                friendsItemAdapter = context?.let {
                    FriendsItemAdapter(
                        context = it,
                        friends = friends
                    )
                }
                _friendsUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun resetUpdate() {
        _friendsUpdate.value = false
    }

    class Factory(private val username: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java)
                .newInstance(username)
        }
    }
}