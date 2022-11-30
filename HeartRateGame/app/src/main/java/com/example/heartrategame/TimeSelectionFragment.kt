package com.example.heartrategame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

        binding.minutesPicker.maxValue = 59
        binding.secondsPicker.maxValue = 59

        val args: TimeSelectionFragmentArgs by navArgs()
        val levelData = args.levelData
        binding.levelName.text = levelData.name
        Glide
            .with(this)
            .load(levelData.imageUri)
            .into(binding.levelImage)

        return binding.root
    }

}