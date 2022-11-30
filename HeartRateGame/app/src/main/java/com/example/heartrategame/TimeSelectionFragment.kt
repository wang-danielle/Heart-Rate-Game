package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
        binding.minutesPicker.maxValue = 59
        binding.secondsPicker.maxValue = 59
        return binding.root
    }

}