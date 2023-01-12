package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentFriendsListBinding

class FriendsListFragment : Fragment() {

    private lateinit var binding: FragmentFriendsListBinding

    companion object {
        fun newInstance() = FriendsListFragment()
    }

    private lateinit var viewModel: FriendsListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friends_list, container, false)
        val username = (activity as MainActivity).auth.currentUser!!.displayName!!
        val factory = FriendsListViewModel.Factory(username)
        viewModel = ViewModelProvider(this, factory).get(FriendsListViewModel::class.java)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        binding.friendRequestsButton.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_friendsListFragment_to_friendRequestsFragment)
        }

        binding.friendsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.friendsUpdate.observe(viewLifecycleOwner, Observer { friendsUpdate ->
            if (friendsUpdate == true) {
                binding.friendsRecyclerView.adapter = viewModel.friendsItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForFriends(context)

        return binding.root
    }
}