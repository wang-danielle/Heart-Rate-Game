package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.heartrategame.databinding.FragmentCustomLevelBinding

class CustomLevelFragment : Fragment() {

    private lateinit var binding: FragmentCustomLevelBinding

    companion object {
        fun newInstance() = CustomLevelFragment()
    }

    private lateinit var viewModel: CustomLevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_level, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_customLevelFragment_to_selectActivityFragment)
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CustomLevelViewModel::class.java)
        // TODO: Use the ViewModel
    }

}