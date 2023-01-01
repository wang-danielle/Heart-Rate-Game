package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentGameBinding
import com.google.android.gms.wearable.Wearable
import com.google.android.gms.wearable.Wearable.getDataClient

class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private var totalTime: Long = -1
    private lateinit var timer: CountDownTimer

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        viewModel.dataClient = Wearable.getDataClient(activity as AppCompatActivity)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        binding.quitButton.setOnClickListener {
            view?.findNavController()?.popBackStack(R.id.levelSelectionFragment, false)
            timer.cancel()
            viewModel.sendQuitToWear()
        }

        val args: GameFragmentArgs by navArgs()
        val levelData = args.levelData
        viewModel.level = levelData

        totalTime = levelData.totalTime
        binding.timeLeftTextView.text = viewModel.formatTime(totalTime * viewModel.MILLIS_PER_SEC)

        viewModel.currentExercise.observe(viewLifecycleOwner, Observer { exercise ->
            binding.activityName.text = exercise.title
            binding.activityImage.setImageResource(exercise.imageResource)
        })
        viewModel.setFirstExercise()

        viewModel.heartRate.observe(viewLifecycleOwner, Observer { heartRate ->
            binding.bpmTextView.text = heartRate.toString()
        })
        viewModel.minHeartRate.observe(viewLifecycleOwner, Observer { minHR ->
            if (minHR == Int.MAX_VALUE) return@Observer
            binding.minHrTextView.text = "MIN: $minHR bpm"
        })
        viewModel.maxHeartRate.observe(viewLifecycleOwner, Observer { maxHR ->
            if (maxHR == 0) return@Observer
            binding.maxHrTextView.text = "MAX: $maxHR bpm"
        })
        viewModel.avgHeartRate.observe(viewLifecycleOwner, Observer { avgHR ->
            binding.avgHrTextView.text = "AVG: $avgHR bpm"
        })
        getDataClient(activity as MainActivity).addListener(viewModel)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timer = object : CountDownTimer(totalTime*viewModel.MILLIS_PER_SEC, viewModel.MILLIS_PER_SEC) {
            override fun onTick(msLeft: Long) {
                val timeLeftText = viewModel.formatTime(msLeft)
                binding.timeLeftTextView.text = timeLeftText
                viewModel.sendTimeToWear(msLeft)
            }

            override fun onFinish() {
                viewModel.sendResultsToWear()
                view?.findNavController()?.navigate(R.id.action_gameFragment_to_resultsFragment)
            }
        }.start()
    }
}