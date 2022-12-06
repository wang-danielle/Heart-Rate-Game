package com.example.heartrategame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heartrategame.models.LevelDataClass

class ItemAdapter(val context: Context, val levels: List<LevelDataClass>): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val level = levels[position]
        holder.levelName.text = level.name
        if (level.createdBy == null) {
            holder.levelImage.setImageResource(level.exercises[0].first.imageResource)
        } else if (level.imageUri == null) {
            holder.levelImage.setImageResource(R.drawable.combo)
        } else {
            Glide
                .with(context)
                .load(level.imageUri)
                .into(holder.levelImage)
        }
        
        holder.itemView.setOnClickListener {
            val level = levels[position]
            val directions = LevelSelectionFragmentDirections.actionSelectActivityFragmentToTimeSelectionFragment(level)
            Navigation.findNavController(it).navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return levels.size
    }
    
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var levelName = view.findViewById<TextView>(R.id.level_name)
        var levelImage = view.findViewById<ImageView>(R.id.level_image)
    }
}