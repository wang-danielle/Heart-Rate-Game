package com.example.heartrategame

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heartrategame.models.LevelDataClass

class ItemAdapter(val context: Context, val levelNames: ArrayList<String>, val levelImageUris: ArrayList<Uri>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.levelName.text = levelNames[position]
        Glide
            .with(context)
            .load(levelImageUris[position])
            .into(holder.levelImage)
        holder.levelImage.setImageURI(levelImageUris[position])
        holder.itemView.setOnClickListener {
            val levelData = LevelDataClass(levelNames[position], levelImageUris[position])
            val directions = LevelSelectionFragmentDirections.actionSelectActivityFragmentToTimeSelectionFragment(levelData)
            Navigation.findNavController(it).navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return levelNames.size
    }
    
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var levelName = view.findViewById<TextView>(R.id.level_name)
        var levelImage = view.findViewById<ImageView>(R.id.level_image)
    }
}