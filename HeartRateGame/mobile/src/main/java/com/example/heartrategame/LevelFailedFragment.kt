package com.example.heartrategame

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.heartrategame.databinding.FragmentLevelFailedBinding

class LevelFailedFragment : Fragment() {
    private lateinit var binding: FragmentLevelFailedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_level_failed, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.levelSelectionFragment, false)
        }
        binding.quitButton.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.levelSelectionFragment, false)
        }

        val args: LevelFailedFragmentArgs by navArgs()
        binding.failReasonText.text = args.failReason

        return binding.root
    }
}