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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentFriendRequestsBinding

class FriendRequestsFragment : Fragment() {

    private  lateinit var binding: FragmentFriendRequestsBinding

    companion object {
        fun newInstance() = FriendRequestsFragment()
    }

    private lateinit var viewModel: FriendRequestsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend_requests, container, false)
        val username = (activity as MainActivity).auth.currentUser!!.displayName!!
        val factory = FriendRequestsViewModel.Factory(username)
        viewModel = ViewModelProvider(this, factory).get(FriendRequestsViewModel::class.java)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }

        binding.incomingRequestsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.friendRequestsUpdate.observe(viewLifecycleOwner, Observer { friendRequestsUpdate ->
            if (friendRequestsUpdate == true) {
                binding.incomingRequestsRecyclerView.adapter = viewModel.friendRequestsItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForFriendRequests(context)

        return binding.root
    }
}