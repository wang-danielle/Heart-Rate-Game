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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.heartrategame.databinding.FragmentFriendProfileBinding
import com.google.firebase.storage.FirebaseStorage

class FriendProfileFragment : Fragment() {

    private lateinit var binding: FragmentFriendProfileBinding

    companion object {
        fun newInstance() = FriendProfileFragment()
    }

    private lateinit var viewModel: FriendProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_profile, container, false)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        val args: FriendProfileFragmentArgs by navArgs()
        val username = args.username
        binding.profileCard.username.text = username
        binding.profileCard.leftSpace.setBackgroundColor(R.color.pink_600)
        binding.profileCard.rightSpace.setBackgroundColor(R.color.pink_600)
        val factory = FriendProfileViewModel.Factory(username)
        viewModel = ViewModelProvider(this, factory).get(FriendProfileViewModel::class.java)

        val storageRef = FirebaseStorage.getInstance().reference
        val profileImageRef = storageRef.child("ProfileImages/$username")
        profileImageRef.downloadUrl.addOnSuccessListener {
            Glide
                .with(context as MainActivity)
                .load(it)
                .into(binding.profileCard.profilePicture)
        }

        binding.scoresRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.scoresUpdate.observe(viewLifecycleOwner, Observer { scoresUpdate ->
            if (scoresUpdate == true) {
                binding.scoresRecyclerView.adapter = viewModel.scoresItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForScores(context)

        return binding.root
    }
}