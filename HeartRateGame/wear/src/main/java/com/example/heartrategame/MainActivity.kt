package com.example.heartrategame

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.heartrategame.databinding.ActivityMainBinding
import com.example.heartrategame.models.Exercise
import com.google.android.gms.wearable.*

class MainActivity : Activity(), DataClient.OnDataChangedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onResume() {
        super.onResume()

        Wearable.getDataClient(this).addListener(this)
    }

    override fun onPause() {
        super.onPause()

        Wearable.getDataClient(this).removeListener(this)
    }

    override fun onDataChanged(dataEvents: DataEventBuffer) {
        dataEvents.filter { it.dataItem.uri.path == "/newTimeRequest" }
            .forEach { event ->
                val timeLeft = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getString("timeLeft")

                binding.bottomTextView.text = timeLeft
            }
        dataEvents.filter { it.dataItem.uri.path == "/newExerciseRequest" }
            .forEach { event ->
                val timeLeft = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getString("timeLeft")
                binding.bottomTextView.text = timeLeft

                val newExercise = DataMapItem.fromDataItem(event.dataItem)
                    .dataMap
                    .getInt("newExercise", -1)
                val exercise = Exercise.values()[newExercise]
                displayExercise(exercise)
            }
    }

    private fun displayExercise(exercise: Exercise) {
        binding.topTextView.text = exercise.title
        binding.image.setImageResource(exercise.imageResource)
        binding.image.setColorFilter(Color.WHITE)
        binding.parentView.setBackgroundColor(
            ResourcesCompat.getColor(resources, R.color.pink_600, null)
        )
    }
}