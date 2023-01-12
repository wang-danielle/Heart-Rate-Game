package com.example.heartrategame

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class LoginProfileViewModel(
    private val auth: FirebaseAuth
) : ViewModel() {
    var imageUri: Uri? = null

    val storageRef = FirebaseStorage.getInstance().reference
    val usersRef = FirebaseDatabase.getInstance().getReference("Users")

    private var _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    fun signIn(username: String, password: String) {
        if (username == "") {
            _errorMessage.value = "Enter a username"
            return
        }
        if (password == "") {
            _errorMessage.value = "Enter a password"
            return
        }
        val email = "$username@heartrategame.com"
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = when (task.exception) {
                        is FirebaseAuthInvalidUserException -> "User not found"
                        is FirebaseAuthInvalidCredentialsException -> "Incorrect password"
                        else -> "Unknown error ${task.exception?.message}"
                    }
                }
            }
    }

    fun signUp(username: String, password: String) {
        if (username == "") {
            _errorMessage.value = "Enter a username"
            return
        }
        if (password == "") {
            _errorMessage.value = "Enter a password"
            return
        }
        val email = "$username@heartrategame.com"

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                if (imageUri == null) {
                    auth.currentUser!!.updateProfile(
                        userProfileChangeRequest {
                            displayName = username
                        }
                    ).addOnFailureListener {
                        _errorMessage.value = "Failed to set profile info"
                        auth.currentUser?.delete()
                    }.addOnSuccessListener {
                        usersRef.child(username).setValue("")
                        _errorMessage.value = null
                    }
                    return@addOnSuccessListener
                }

                val profileImageRef = storageRef.child("ProfileImages/$username")
                profileImageRef.putFile(imageUri!!).addOnFailureListener {
                    _errorMessage.value = "Failed to upload profile picture"
                    auth.currentUser?.delete()
                }.addOnSuccessListener {
                    val urlTask = profileImageRef.downloadUrl
                    urlTask.addOnFailureListener {
                        _errorMessage.value = "Failed to set profile picture"
                        auth.currentUser?.delete()
                    }.addOnSuccessListener { downloadUrl ->
                        auth.currentUser!!.updateProfile(
                            userProfileChangeRequest {
                                displayName = username
                                photoUri = downloadUrl
                            }
                        ).addOnFailureListener {
                            _errorMessage.value = "Failed to set profile info"
                            auth.currentUser?.delete()
                        }.addOnSuccessListener {
                            usersRef.child(username).setValue("")
                            _errorMessage.value = null
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                _errorMessage.value = when (exception) {
                    is FirebaseAuthWeakPasswordException -> "Password too weak (must be >5 characters long)"
                    is FirebaseAuthInvalidCredentialsException -> "Username cannot contain special characters"
                    is FirebaseAuthUserCollisionException -> "This username is already in use"
                    else -> "Unknown error ${exception.message}"
                }
            }
    }

    class Factory(private val auth: FirebaseAuth): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass.getConstructor(FirebaseAuth::class.java)
                .newInstance(auth)
        }
    }
}