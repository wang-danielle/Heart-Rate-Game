package com.example.heartrategame

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.ActivityRowBinding.inflate

class ItemAdapter(val context: Context, val activityNames: ArrayList<String>, val activityImageUris: ArrayList<Uri>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.activity_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.activityName.text = activityNames[position]
        Glide
            .with(context)
            .load(activityImageUris[position])
            .into(holder.activityImage)
        holder.activityImage.setImageURI(activityImageUris[position])
    }

    override fun getItemCount(): Int {
        return activityNames.size
    }
    
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var activityName = view.findViewById<TextView>(R.id.activity_name)
        var activityImage = view.findViewById<ImageView>(R.id.activity_image)
    }
}