package com.example.heartrategame

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heartrategame.databinding.FragmentAddFriendBinding

class AddFriendFragment : Fragment() {
    private lateinit var binding: FragmentAddFriendBinding

    companion object {
        fun newInstance() = AddFriendFragment()
    }

    private lateinit var viewModel: AddFriendViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_friend, container, false)
        val username = (activity as MainActivity).auth.currentUser!!.displayName!!
        val factory = AddFriendViewModel.Factory(username)
        viewModel = ViewModelProvider(this, factory).get(AddFriendViewModel::class.java)

        binding.backButton.setOnClickListener {
            view?.findNavController()?.popBackStack()
        }
        binding.sendRequestButton.setOnClickListener {
            val targetUsername = binding.usernameEditText.text.toString()
            viewModel.sendFriendRequest(targetUsername)
        }

        binding.pendingRequestsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.sentRequestsUpdate.observe(viewLifecycleOwner, Observer { sentRequestsUpdate ->
            if (sentRequestsUpdate == true) {
                binding.pendingRequestsRecyclerView.adapter = viewModel.sentRequestsItemAdapter
                viewModel.resetUpdate()
            }
        })
        viewModel.listenForSentRequests(context)

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message == null) {
                viewModel.listenForSentRequests(context)
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })

        return binding.root
    }
}