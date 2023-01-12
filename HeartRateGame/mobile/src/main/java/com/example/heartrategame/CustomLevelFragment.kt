package com.example.heartrategame

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentCustomLevelBinding
import com.example.heartrategame.room.LevelDatabase

class CustomLevelFragment : Fragment() {

    private lateinit var binding: FragmentCustomLevelBinding
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri: Uri? = result.data?.data
                sharedViewModel.setImage(imageUri)
                binding.levelImageButton.setImageURI(imageUri)
            }
        }

    companion object {
        fun newInstance() = CustomLevelFragment()
    }

    private lateinit var viewModel: CustomLevelViewModel
    private lateinit var sharedViewModel: SharedCustomLevelViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(CustomLevelViewModel::class.java)
        val application = requireNotNull(context as MainActivity).application
        val database = LevelDatabase.getInstance(application)
        val factory = SharedCustomLevelViewModel.Factory(database)
        sharedViewModel = ViewModelProvider(requireActivity(), factory).get(SharedCustomLevelViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_level, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
            sharedViewModel.resetLevel()
        }
        sharedViewModel.levelData.value?.imageUri?.let {
            binding.levelImageButton.setImageURI(it)
        }
        binding.levelImageButton.setOnClickListener {
            val imgIntent = Intent(Intent.ACTION_GET_CONTENT)
            imgIntent.setType("image/*")
            resultLauncher.launch(imgIntent)
        }

        binding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.exercisesUpdate.observe(viewLifecycleOwner, Observer { exercisesUpdate ->
            if (exercisesUpdate == true) {
                binding.exerciseRecyclerView.adapter = viewModel.exercisesItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForExercises(context)

        sharedViewModel.levelData.observe(viewLifecycleOwner, Observer { levelData ->
            viewModel.exercises = levelData.exercises
            viewModel.listenForExercises(context)
        })

        binding.createButton.setOnClickListener {
            val levelName = binding.levelNameEditText.text.toString()
            if (levelName == "") {
                Toast.makeText(
                    context,
                    "Enter a level name",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (sharedViewModel.levelData.value?.exercises?.isEmpty() == true) {
                Toast.makeText(
                    context,
                    "You must add exercises",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            sharedViewModel.saveLevel(levelName)
            view?.findNavController()?.popBackStack()
            sharedViewModel.resetLevel()
        }

        return binding.root
    }
}