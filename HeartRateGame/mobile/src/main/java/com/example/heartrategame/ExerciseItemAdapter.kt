package com.example.heartrategame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heartrategame.models.Exercise

class ExerciseItemAdapter(val context: Context, val exercises: Array<Exercise>): RecyclerView.Adapter<ExerciseItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = parent.measuredHeight / 7
        itemView.layoutParams.height = height
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.text = exercise.title
        holder.exerciseImage.setImageResource(exercise.imageResource)
        holder.itemView.setOnClickListener {
            
        }
    }

    override fun getItemCount(): Int {
        return exercises.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var exerciseName = view.findViewById<TextView>(R.id.level_name)
        var exerciseImage = view.findViewById<ImageView>(R.id.level_image)
        var bottomLine = view.findViewById<View>(R.id.bottom_line)
        var sideText = view.findViewById<TextView>(R.id.side_text)
    }
}