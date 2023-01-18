package com.example.heartrategame

import android.app.Activity
import android.content.Intent
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
import com.example.heartrategame.databinding.FragmentLoginProfileBinding

class LoginProfileFragment : Fragment() {
    private lateinit var binding: FragmentLoginProfileBinding
    var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                viewModel.imageUri = imageUri
                binding.userImageButton.setImageURI(imageUri)
            }
        }

    companion object {
        fun newInstance() = LoginProfileFragment()
    }

    private lateinit var viewModel: LoginProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_profile, container, false)
        val auth = (activity as MainActivity).auth
        val factory = LoginProfileViewModel.Factory(auth)
        viewModel = ViewModelProvider(this, factory).get(LoginProfileViewModel::class.java)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        binding.userImageButton.setOnClickListener {
            val imgIntent = Intent(Intent.ACTION_GET_CONTENT)
            imgIntent.type = "image/*"
            resultLauncher.launch(imgIntent)
        }

        binding.signInButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.signIn(username, password)
        }

        binding.signUpButton.setOnClickListener {
            binding.signUpButton.isClickable = false
            binding.signInButton.isClickable = false
            binding.userImageButton.visibility = View.INVISIBLE
            binding.loadingWheel.visibility = View.VISIBLE
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.signUp(username, password)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { msg ->
            if (msg == null) {
                view?.findNavController()?.popBackStack()
            } else {
                binding.signUpButton.isClickable = true
                binding.signInButton.isClickable = false
                binding.userImageButton.visibility = View.VISIBLE
                binding.loadingWheel.visibility = View.INVISIBLE
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}