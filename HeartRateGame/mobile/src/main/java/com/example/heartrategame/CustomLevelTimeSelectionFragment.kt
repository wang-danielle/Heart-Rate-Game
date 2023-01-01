package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CustomLevelTimeSelectionFragment : Fragment() {

    companion object {
        fun newInstance() = CustomLevelTimeSelectionFragment()
    }

    private lateinit var viewModel: CustomLevelTimeSelectionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_custom_level_time_selection, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomLevelTimeSelectionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}