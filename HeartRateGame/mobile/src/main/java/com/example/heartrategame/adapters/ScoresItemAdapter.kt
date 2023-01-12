package com.example.heartrategame.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heartrategame.R
import com.example.heartrategame.models.Exercise

class ScoresItemAdapter(val context: Context, val levels: List<Pair<Exercise, String>>)
    : RecyclerView.Adapter<ScoresItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = (parent.parent as ViewGroup).measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pair = levels[position]
        val exercise = pair.first
        holder.levelName.text = exercise.title
        holder.levelImage.setImageResource(exercise.imageResource)
        holder.sideText.text = pair.second
    }

    override fun getItemCount(): Int {
        return levels.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var levelName: TextView = view.findViewById(R.id.level_name)
        var levelImage: ImageView = view.findViewById(R.id.level_image)
        var sideText: TextView = view.findViewById(R.id.side_text)
    }
}