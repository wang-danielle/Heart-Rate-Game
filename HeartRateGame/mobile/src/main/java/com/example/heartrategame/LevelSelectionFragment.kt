package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentLevelSelectionBinding
import com.example.heartrategame.room.LevelDatabase

class LevelSelectionFragment : Fragment() {
    private lateinit var binding: FragmentLevelSelectionBinding

    companion object {
        fun newInstance() = LevelSelectionFragment()
    }

    private lateinit var viewModel: LevelSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_selection, container, false)
        val application = requireNotNull(context as MainActivity).application
        val database = LevelDatabase.getInstance(application)
        val factory = LevelSelectionViewModel.Factory(database)
        viewModel = ViewModelProvider(this, factory).get(LevelSelectionViewModel::class.java)

        binding.levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.levelsUpdate.observe(viewLifecycleOwner, Observer { activitiesUpdate ->
            if (activitiesUpdate == true) {
                binding.levelRecyclerView.adapter = viewModel.levelItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForLevels(context)
        viewModel.levels.observe(viewLifecycleOwner, Observer { loadedLevels ->
            viewModel.listenForLevels(context, loadedLevels)
        })

        val currentUser = (activity as MainActivity).auth.currentUser
        if (currentUser == null) {
            binding.accountButton.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_levelSelectionFragment_to_loginProfileFragment)
            }
        } else {
            currentUser.photoUrl?.let {
                Glide
                    .with(context as MainActivity)
                    .load(it)
                    .into(binding.accountButton)
            }
            binding.accountButton.setOnClickListener {
                Navigation.findNavController(it)
                    .navigate(R.id.action_levelSelectionFragment_to_accountFragment)
            }
        }

        return binding.root
    }
}