package com.dicoding.tugasusergithubv2.ui.detail.tabs

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.dicoding.tugasusergithubv2.R

class FollowingFragment : Fragment() {

    lateinit var testFollowing: TextView
    lateinit var username: String

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var viewModel: FollowingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        username = arguments?.getString(ARG_USERNAME) as String
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        testFollowing = view.findViewById(R.id.test_following)
        testFollowing.text = username + " following"
    }
}