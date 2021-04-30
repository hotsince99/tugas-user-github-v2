package com.dicoding.tugasusergithubv2.ui.detail.tabs

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = null

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment.newInstance(username as String)
            1 -> fragment = FollowingFragment.newInstance(username as String)
        }
        return fragment as Fragment
    }
}