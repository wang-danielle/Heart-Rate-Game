package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentLevelSelectionBinding

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
        viewModel = ViewModelProvider(this).get(LevelSelectionViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_selection, container, false)

        binding.levelRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.listenForLevels(context)
        viewModel.levelsUpdate.observe(viewLifecycleOwner, Observer { activitiesUpdate ->
            if (activitiesUpdate == true) {
                binding.levelRecyclerView.adapter = viewModel.levelItemAdapter
                viewModel.resetUpdate()
            }
        })

        return binding.root
    }
}