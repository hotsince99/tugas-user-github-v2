package com.dicoding.tugasusergithubv2.ui.detail.tabs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.FragmentFollowingBinding
import com.dicoding.tugasusergithubv2.ui.detail.DetailActivity
import com.dicoding.tugasusergithubv2.ui.main.ListUserAdapter
import com.dicoding.tugasusergithubv2.ui.main.MainViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FollowingViewModel

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
        binding = FragmentFollowingBinding.bind(view)

        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFollowing.layoutManager = LinearLayoutManager(activity)
        binding.rvFollowing.adapter = adapter
        binding.rvFollowing.setHasFixedSize(true)

        adapter.setCallback(object : ListUserAdapter.Callback {
            override fun onItemClick(user: UserItem) {
                showSelectedUser(user)
            }
        })

        showProgressBar(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowingViewModel::class.java)
        viewModel.setFollowing(username)
        viewModel.getFollowing().observe(viewLifecycleOwner, {
            if (it != null) {
                adapter.setData(it)
                showProgressBar(false)
            }
        })


    }

    private fun showSelectedUser(user: UserItem) {
        //Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(activity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_LOGIN, user.login)
        startActivity(intent)
    }

    private fun showProgressBar(isEnabled: Boolean) {
        if (isEnabled) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}















