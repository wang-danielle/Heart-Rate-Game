package com.example.heartrategame

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentCustomLevelBinding
import com.example.heartrategame.models.Exercise

class CustomLevelFragment : Fragment() {

    private lateinit var binding: FragmentCustomLevelBinding

    companion object {
        fun newInstance() = CustomLevelFragment()
    }

    private lateinit var viewModel: CustomLevelViewModel
    private lateinit var sharedViewModel: SharedCustomLevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CustomLevelViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedCustomLevelViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_level, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.listenForExercises(context)
        viewModel.exercisesUpdate.observe(viewLifecycleOwner, Observer { exercisesUpdate ->
            if (exercisesUpdate == true) {
                binding.exerciseRecyclerView.adapter = viewModel.exercisesItemAdapter
                viewModel.resetUpdate()
            }
        })

        sharedViewModel.levelData.observe(viewLifecycleOwner, Observer { levelData ->
            viewModel.exercises = levelData.exercises
            viewModel.listenForExercises(context)
        })

        return binding.root
    }
}