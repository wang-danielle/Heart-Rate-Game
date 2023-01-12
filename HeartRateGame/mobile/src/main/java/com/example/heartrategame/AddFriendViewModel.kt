package com.example.heartrategame

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.heartrategame.adapters.SentRequestsItemAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddFriendViewModel(
    private val myUsername: String
) : ViewModel() {
    private var usersRef = FirebaseDatabase.getInstance().getReference("Users")
    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    private var sentRequestsRef = usersRef.child(myUsername).child("SentRequests")
    var sentRequestsItemAdapter: SentRequestsItemAdapter? = null
    private val _sentRequestsUpdate = MutableLiveData<Boolean?>()
    val sentRequestsUpdate: LiveData<Boolean?>
        get() = _sentRequestsUpdate

    fun listenForSentRequests(context: Context?) {
        sentRequestsRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val requests = dataSnapshot.children.map {
                    it.key ?: ""
                }

                sentRequestsItemAdapter = context?.let {
                    SentRequestsItemAdapter(
                        context = it,
                        requests = requests
                    )
                }
                _sentRequestsUpdate.value = true
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun resetUpdate() {
        _sentRequestsUpdate.value = false
    }

    fun sendFriendRequest(targetUsername: String) {
        if (targetUsername == myUsername) {
            _errorMessage.value = "That's you!"
            return
        }
        usersRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children) {
                    val username = user.key
                    if (username == targetUsername) {
                        usersRef.child(targetUsername)
                            .child("FriendRequests")
                            .child(myUsername)
                            .setValue("")
                        usersRef.child(myUsername)
                            .child("SentRequests")
                            .child(targetUsername)
                            .setValue("")
                            .addOnSuccessListener {
                                _errorMessage.value = null
                            }
                    }
                }
                _errorMessage.value = "User not found"
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    class Factory(private val username: String): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(String::class.java)
                .newInstance(username)
        }
    }
}