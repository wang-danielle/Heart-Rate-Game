package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding

    companion object {
        fun newInstance() = AccountFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)

        val auth = (activity as MainActivity).auth
        val currentUser = auth.currentUser!!
        val username = currentUser.email!!.dropLast("@heartrategame.com".length)
        binding.welcomeTextView.text = "Welcome, $username!"
        currentUser.photoUrl?.let {
            Glide
                .with(context as MainActivity)
                .load(it)
                .into(binding.userImageView)
        }

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        binding.signOutButton.setOnClickListener {
            auth.signOut()
            view?.findNavController()?.popBackStack()
        }

        return binding.root
    }
}