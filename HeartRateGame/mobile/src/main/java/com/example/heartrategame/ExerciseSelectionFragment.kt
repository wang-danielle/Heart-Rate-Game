package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentExerciseSelectionBinding

class ExerciseSelectionFragment : Fragment() {

    private lateinit var binding: FragmentExerciseSelectionBinding

    companion object {
        fun newInstance() = ExerciseSelectionFragment()
    }

    private lateinit var viewModel: ExerciseSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(ExerciseSelectionViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_exercise_selection, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.setupExercisesItemAdapter(context)
        binding.exerciseRecyclerView.adapter = viewModel.exercisesItemAdapter

        return binding.root
    }
}