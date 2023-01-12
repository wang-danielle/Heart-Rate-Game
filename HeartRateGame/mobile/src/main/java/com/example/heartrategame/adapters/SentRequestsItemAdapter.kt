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
import com.google.firebase.storage.FirebaseStorage

class SentRequestsItemAdapter(val context: Context, val requests: List<String>)
    : RecyclerView.Adapter<SentRequestsItemAdapter.ViewHolder>() {
    private val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.friend_row, parent, false)
        val height = (parent.parent as ViewGroup).measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val username = requests[position]
        holder.username.text = username

        val profileImageRef = storageRef.child("ProfileImages/$username")
        profileImageRef.downloadUrl.addOnSuccessListener {
            Glide
                .with(context as MainActivity)
                .load(it)
                .into(holder.profilePicture)
        }
    }

    override fun getItemCount(): Int {
        return requests.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var username: TextView = view.findViewById(R.id.username)
        var profilePicture: ImageView = view.findViewById(R.id.profile_picture)
    }
}