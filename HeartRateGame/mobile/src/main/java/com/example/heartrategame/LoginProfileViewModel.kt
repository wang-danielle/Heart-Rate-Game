package com.example.heartrategame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.provider.MediaStore
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.ByteArrayOutputStream

class LoginProfileViewModel(
    private val auth: FirebaseAuth
) : ViewModel() {
    var imageUri: Uri? = null

    var storageRef = FirebaseStorage.getInstance().reference

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
                        else -> "Unknown error"
                    }
                }
            }
    }

    private fun uploadImage(username: String, context: Context): UploadTask? {
        if (imageUri == null) {
            return null
        }

        val profileImageRef = storageRef.child("ProfileImages/$username.jpg")
        val matrix = Matrix()
        matrix.postRotate(90F)
        var imageBitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val ratio = 13F
        val imageBitmapScaled = Bitmap.createScaledBitmap(
            imageBitmap,
            (imageBitmap.width / ratio).toInt(), (imageBitmap.height / ratio).toInt(), false
        )
        imageBitmap = Bitmap.createBitmap(
            imageBitmapScaled, 0, 0,
            (imageBitmap.width / ratio).toInt(), (imageBitmap.height / ratio).toInt(),
            matrix, true
        )
        val stream = ByteArrayOutputStream()
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val imageByteArray = stream.toByteArray()
        return profileImageRef.putBytes(imageByteArray)
    }

    fun signUp(username: String, password: String, context: Context) {
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
                val uploadTask = uploadImage(username, context)
                if (uploadTask == null) {
                    _errorMessage.value = null
                    return@addOnSuccessListener
                }

                uploadTask.addOnFailureListener {
                    _errorMessage.value = "Failed to upload profile picture"
                }.addOnSuccessListener {
                    val urlTask = storageRef.child("ProfileImages/$username.jpg").downloadUrl
                    urlTask.addOnFailureListener {
                        _errorMessage.value = "Failed to set profile picture"
                    }.addOnSuccessListener { downloadUrl ->
                        auth!!.currentUser!!.updateProfile(
                            userProfileChangeRequest {
                                displayName = username
                                photoUri = downloadUrl
                            }
                        ).addOnFailureListener {
                            _errorMessage.value = "Failed to set profile picture"
                        }.addOnSuccessListener {
                            _errorMessage.value = null
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                _errorMessage.value = when (exception) {
                    is FirebaseAuthWeakPasswordException -> "Password too weak (must be >5 characters long)"
                    is FirebaseAuthInvalidCredentialsException -> "Username cannot contain special characters"
                    is FirebaseAuthUserCollisionException -> "This username is already in use"
                    else -> "Unknown error"
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