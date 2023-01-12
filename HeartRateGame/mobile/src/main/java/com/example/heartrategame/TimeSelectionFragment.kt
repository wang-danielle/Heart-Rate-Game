package com.example.heartrategame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.heartrategame.databinding.FragmentTimeSelectionBinding

class TimeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentTimeSelectionBinding

    companion object {
        fun newInstance() = TimeSelectionFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_selection, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        val args: TimeSelectionFragmentArgs by navArgs()
        val levelData = args.levelData
        binding.minutesPicker.maxValue = 59
        binding.secondsPicker.maxValue = 59
        binding.levelName.text = levelData.name
        binding.levelImage.setImageResource(levelData.exercises[0].first.imageResource)

        binding.startButton.setOnClickListener {
            val minutes = binding.minutesPicker.value
            val seconds = binding.secondsPicker.value
            val totalTime = (minutes*60 + seconds).toLong()
            if (totalTime < 10L) {
                Toast.makeText(
                    context,
                    "Choose a longer duration for this exercise (at least 10s)!",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val exercise = levelData.exercises[0].first
            levelData.exercises.removeAt(0)
            levelData.exercises.add(Pair(exercise, totalTime))
            levelData.totalTime = totalTime

            val directions = TimeSelectionFragmentDirections.actionTimeSelectionFragmentToGameFragment(
                levelData,
                -(exercise.ordinal + 1).toLong()
            )
            Navigation.findNavController(it).navigate(directions)
        }

        return binding.root
    }

}