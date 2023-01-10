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
import androidx.navigation.fragment.navArgs
import com.example.heartrategame.databinding.FragmentResultsBinding
import com.example.heartrategame.room.ScoreDatabase

class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    companion object {
        fun newInstance() = ResultsFragment()
    }

    private lateinit var viewModel: ResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_results, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.levelSelectionFragment, false)
        }
        binding.quitButton.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.levelSelectionFragment, false)
        }

        val args: ResultsFragmentArgs by navArgs()
        val scores = args.scores

        binding.scoreTextView.text = scores.totalScore.toString()
        binding.minHrTextView.text = "MIN: ${scores.minHeartrate} bpm"
        binding.maxHrTextView.text = "MAX: ${scores.maxHeartrate} bpm"
        binding.avgHrTextView.text = "AVG: ${scores.avgHeartrate} bpm"

        val application = requireNotNull(context as MainActivity).application
        val database = ScoreDatabase.getInstance(application)
        val factory = ResultsViewModel.Factory(database, args.levelId)
        viewModel = ViewModelProvider(this, factory).get(ResultsViewModel::class.java)
        viewModel.bestScore.observe(viewLifecycleOwner, Observer {
            binding.newBestScoreLabel.visibility =
                if (viewModel.isNewBestScore) View.VISIBLE else View.GONE
            binding.bestScoreTextView.text = it.toString()
        })
        viewModel.fetchAndUpdateBestScore(scores.totalScore)

        return binding.root
    }
}