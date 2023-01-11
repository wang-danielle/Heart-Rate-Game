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
import com.example.heartrategame.LevelSelectionFragmentDirections
import com.example.heartrategame.room.LevelEntity
import com.example.heartrategame.R

class LevelItemAdapter(val context: Context, val levels: List<LevelEntity>): RecyclerView.Adapter<LevelItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == levels.size) {
            holder.levelName.text = "Add custom level..."
            holder.levelName.setTextColor(Color.GRAY)
            holder.bottomLine.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_levelSelectionFragment_to_customLevelFragment)
            }
            return
        }

        val levelEntity = levels[position]
        val level = levelEntity.levelData
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
            val directions =
                if (level.createdBy == null) {
                    LevelSelectionFragmentDirections.actionLevelSelectionFragmentToTimeSelectionFragment(
                        level
                    )
                } else {
                    LevelSelectionFragmentDirections.actionLevelSelectionFragmentToGameFragment(
                        level,
                        levelEntity.id
                    )
                }
            Navigation.findNavController(it).navigate(directions)
        }
    }

    override fun getItemCount(): Int {
        return levels.size + 1
    }
    
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var levelName: TextView = view.findViewById(R.id.level_name)
        var levelImage: ImageView = view.findViewById(R.id.level_image)
        var bottomLine: View = view.findViewById(R.id.bottom_line)
    }
}