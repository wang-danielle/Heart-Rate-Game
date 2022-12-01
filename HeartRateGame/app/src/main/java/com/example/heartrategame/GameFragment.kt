package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentGameBinding
import java.util.*

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private var timer = Timer()
    private var time: Long? = null
    private val MILLIS_PER_SEC = 1000L

    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)

        binding.quitButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_gameFragment_to_selectActivityFragment)
        }

        val args: GameFragmentArgs by navArgs()
        val levelData = args.levelData
        binding.activityName.text = levelData.name
        Glide
            .with(this)
            .load(levelData.imageUri)
            .into(binding.activityImage)
        binding.bpmTextView.text = "0"
        time = levelData.time
        time?.let { binding.timeLeftTextView.text = formatTime(it * MILLIS_PER_SEC) }

        return binding.root
    }

    private fun formatTime(msLeft: Long): String {
        val secsLeft = msLeft / MILLIS_PER_SEC
        val mins = secsLeft / 60
        val secs = secsLeft % 60
        return "$mins:${secs.toString().padStart(2, '0')}"
    }

    override fun onResume() {
        super.onResume()
        time?.let {
            object : CountDownTimer(it*MILLIS_PER_SEC, MILLIS_PER_SEC) {
                override fun onTick(msLeft: Long) {
                    binding.timeLeftTextView.text = formatTime(msLeft)
                }

                override fun onFinish() {

                }
            }.start()
        }

    }
}