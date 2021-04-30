package com.dicoding.tugasusergithubv2.ui.detail.tabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dicoding.tugasusergithubv2.R

class FollowersFragment : Fragment() {

    lateinit var testFollowers: TextView
    lateinit var username: String

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: FollowersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(ARG_USERNAME) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testFollowers = view.findViewById(R.id.test_followers)
        testFollowers.text = username + " followers"
    }
}