package com.example.heartrategame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heartrategame.MainActivity
import com.example.heartrategame.R
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FriendRequestsItemAdapter(val context: Context, val requests: MutableList<String>)
    : RecyclerView.Adapter<FriendRequestsItemAdapter.ViewHolder>() {
    private val storageRef = FirebaseStorage.getInstance().reference
    private val myUsername = (context as MainActivity).auth.currentUser!!.displayName!!
    private val usersRef = FirebaseDatabase.getInstance().getReference("Users")
    private var friendRequestsRef = usersRef.child(myUsername).child("FriendRequests")
    private var friendsRef = usersRef.child(myUsername).child("Friends")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.friend_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    fun deleteRequest(sourceUsername: String, position: Int) {
        friendRequestsRef
            .child(sourceUsername)
            .removeValue()
        usersRef
            .child(sourceUsername)
            .child("SentRequests")
            .child(myUsername)
            .removeValue()
        requests.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, requests.size)
    }

    fun addFriend(sourceUsername: String) {
        friendsRef
            .child(sourceUsername)
            .setValue("")
        usersRef
            .child(sourceUsername)
            .child("Friends")
            .child(myUsername)
            .setValue("")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sourceUsername = requests[position]
        holder.username.text = sourceUsername

        val profileImageRef = storageRef.child("ProfileImages/$sourceUsername")
        profileImageRef.downloadUrl.addOnSuccessListener {
            Glide
                .with(context as MainActivity)
                .load(it)
                .into(holder.profilePicture)
        }.addOnFailureListener {

        }

        holder.requestButtons.visibility = View.VISIBLE

        holder.acceptButton.setOnClickListener {
            addFriend(sourceUsername)
            deleteRequest(sourceUsername, position)
        }
        holder.denyButton.setOnClickListener {
            deleteRequest(sourceUsername, position)
        }
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var username: TextView = view.findViewById(R.id.username)
        var profilePicture: ImageView = view.findViewById(R.id.profile_picture)
        var requestButtons: View = view.findViewById(R.id.request_buttons)
        var acceptButton: View = view.findViewById(R.id.accept_button)
        var denyButton: View = view.findViewById(R.id.deny_button)
    }
}