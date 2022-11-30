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
import com.example.heartrategame.databinding.FragmentSelectActivityBinding

class SelectActivityFragment : Fragment() {

    private lateinit var binding: FragmentSelectActivityBinding

    companion object {
        fun newInstance() = SelectActivityFragment()
    }

    private lateinit var viewModel: SelectActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SelectActivityViewModel::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_select_activity, container, false)

        binding.activityRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.listenForActivities(context)
        viewModel.activitiesUpdate.observe(viewLifecycleOwner, Observer { activitiesUpdate ->
            if (activitiesUpdate == true) {
                binding.activityRecyclerView.adapter = viewModel.itemAdapter
                viewModel.resetUpdate()
            }
        })

        return binding.root
    }
}