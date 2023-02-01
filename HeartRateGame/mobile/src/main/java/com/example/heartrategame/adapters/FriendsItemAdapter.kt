package com.example.heartrategame.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heartrategame.FriendsListFragmentDirections
import com.example.heartrategame.MainActivity
import com.example.heartrategame.R
import com.google.firebase.storage.FirebaseStorage

class FriendsItemAdapter(val context: Context, val friends: List<String>)
    : RecyclerView.Adapter<FriendsItemAdapter.ViewHolder>() {
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.friend_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == friends.size) {
            holder.username.text = "Add friend..."
            holder.username.setTextColor(Color.GRAY)
            holder.profilePicture.visibility = View.INVISIBLE
            holder.bottomLine.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_friendsListFragment_to_addFriendFragment)
            }
            return
        }

        val username = friends[position]
        holder.username.text = username

        val profileImageRef = storageRef.child("ProfileImages/$username")
        profileImageRef.downloadUrl.addOnSuccessListener {
            Glide
                .with(context as MainActivity)
                .load(it)
                .into(holder.profilePicture)
        }

        holder.itemView.setOnClickListener {
            val directions = FriendsListFragmentDirections.actionFriendsListFragmentToFriendProfileFragment(
                username
            )
            Navigation.findNavController(it).navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return friends.size + 1
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var username: TextView = view.findViewById(R.id.username)
        var profilePicture: ImageView = view.findViewById(R.id.profile_picture)
        var bottomLine: View = view.findViewById(R.id.bottom_line)
    }
}