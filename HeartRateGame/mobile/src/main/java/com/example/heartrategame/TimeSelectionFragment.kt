package com.example.heartrategame

import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentTimeSelectionBinding

class TimeSelectionFragment : Fragment() {

    private lateinit var binding: FragmentTimeSelectionBinding

    companion object {
        fun newInstance() = TimeSelectionFragment()
    }

    private lateinit var viewModel: TimeSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_time_selection, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_timeSelectionFragment_to_selectActivityFragment)
        }

        val args: TimeSelectionFragmentArgs by navArgs()
        val levelData = args.levelData
        if (levelData.createdBy == null) {
            binding.minutesPicker.maxValue = 59
            binding.secondsPicker.maxValue = 59
        } else {
            binding.timeSetDialog.visibility = GONE
        }
        binding.levelName.text = levelData.name
        Glide
            .with(this)
            .load(levelData.imageUri)
            .into(binding.levelImage)
        binding.startButton.setOnClickListener {
            val minutes = binding.minutesPicker.value
            val seconds = binding.secondsPicker.value
            levelData.time = (minutes*60 + seconds).toLong()
            val directions = TimeSelectionFragmentDirections.actionTimeSelectionFragmentToGameFragment(levelData)
            Navigation.findNavController(it).navigate(directions)
        }

        return binding.root
    }

}