package com.example.heartrategame

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.heartrategame.models.Exercise

class ExerciseTimeItemAdapter(val context: Context, val exercises: List<Pair<Exercise, Long?>>): RecyclerView.Adapter<ExerciseTimeItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.level_row, parent, false)
        val height = (parent.parent as ViewGroup).measuredHeight / 7
        itemView.layoutParams.height = height.toInt()
        return ViewHolder(
            itemView
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == exercises.size) {
            holder.exerciseName.text = "Add exercise..."
            holder.exerciseName.setTextColor(Color.GRAY)
            holder.bottomLine.visibility = View.INVISIBLE
            holder.itemView.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_customLevelFragment_to_exerciseSelectionFragment)
            }
            return
        }

        val exercise = exercises[position]
        holder.exerciseName.text = exercise.first.title
        exercise.second?.let {
            holder.sideText.text = formatTime(it)
        }

        holder.exerciseImage.setImageResource(exercise.first.imageResource)
    }

    override fun getItemCount(): Int {
        return exercises.size + 1
    }

    private fun formatTime(totalSecs: Long): String {
        val mins = totalSecs / 60
        val secs = totalSecs % 60
        return "$mins:${secs.toString().padStart(2, '0')}"
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var exerciseName = view.findViewById<TextView>(R.id.level_name)
        var exerciseImage = view.findViewById<ImageView>(R.id.level_image)
        var bottomLine = view.findViewById<View>(R.id.bottom_line)
        var sideText = view.findViewById<TextView>(R.id.side_text)
    }
}