package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
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
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentCustomLevelTimeSelectionBinding
import com.example.heartrategame.databinding.FragmentTimeSelectionBinding

class CustomLevelTimeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentCustomLevelTimeSelectionBinding

    companion object {
        fun newInstance() = CustomLevelTimeSelectionFragment()
    }

    private lateinit var sharedViewModel: SharedCustomLevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_level_time_selection, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedCustomLevelViewModel::class.java)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        val args: CustomLevelTimeSelectionFragmentArgs by navArgs()
        val exercise = args.exercise
        binding.minutesPicker.maxValue = 59
        binding.secondsPicker.maxValue = 59

        binding.levelName.text = exercise.name
        binding.levelImage.setImageResource(exercise.imageResource)

        binding.addButton.setOnClickListener {
            val minutes = binding.minutesPicker.value
            val seconds = binding.secondsPicker.value
            val totalTime = (minutes*60 + seconds).toLong()
            if (totalTime < 10L) {
                Toast.makeText(
                    context,
                    "Choose a longer duration for this exercise! (at least 10s)",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            sharedViewModel.addExercise(exercise, totalTime)
            val navController = Navigation.findNavController(it)
            navController.popBackStack(R.id.customLevelFragment, false)
        }

        return binding.root
    }
}