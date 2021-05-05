package com.dicoding.tugasusergithubv2.ui.detail

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract
import com.dicoding.tugasusergithubv2.data.local.FavoriteHelper
import com.dicoding.tugasusergithubv2.data.local.MappingHelper
import com.dicoding.tugasusergithubv2.databinding.ActivityDetailBinding
import com.dicoding.tugasusergithubv2.ui.detail.tabs.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_LOGIN = "extra_login"

        private val TAB_TITLES = arrayOf("Followers", "Following")
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var favoriteHelper: FavoriteHelper

    private var isFavorite: Boolean = false
    private var username = ""
    private var id: Int = 0
    private var avatarUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = intent.getStringExtra(EXTRA_LOGIN) as String

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        initializeOnClickListener()
        getFavoriteStatusFromDB()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        viewModel.setProfile(username)
        viewModel.getProfile().observe(this, {
            if (it != null) {

                id = it.id
                avatarUrl = it.avatar_url

                binding.apply {

                    tvUsername.text = it.login
                    tvName.text = it.name
                    tvLocation.text = it.location
                    tvCompany.text = it.company
                    tvFollowers.text = it.followers.toString()
                    tvFollowing.text = it.following.toString()
                    tvRepositories.text = it.public_repos.toString()

                    Glide.with(this@DetailActivity)
                        .load(it.avatar_url)
                        .placeholder(R.drawable.ic_placeholder_avatar)
                        .into(imgAvatar)
                }
            }
        })

        val sectionsPagerAdapter = SectionsPagerAdapter(this@DetailActivity)
        sectionsPagerAdapter.username = username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab: TabLayout.Tab, i: Int ->
            tab.text = TAB_TITLES[i]
        }.attach()

        supportActionBar?.elevation = 0f
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.ktp)
    }

    override fun onDestroy() {
        super.onDestroy()

        favoriteHelper.close()
    }

    private fun getFavoriteStatusFromDB() {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryByUsername(username)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favorite = deferredFavorite.await()
            isFavorite = favorite.size > 0
            setFavoriteDrawable()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_favorite -> toggleOnOffFavorite()
            R.id.btn_share -> shareProfileAddress()
        }
    }


    private fun initializeOnClickListener() {
        binding.btnFavorite.setOnClickListener(this)
        binding.btnShare.setOnClickListener(this)
    }

    private fun toggleOnOffFavorite() {

        if (isFavorite) {
            GlobalScope.launch(Dispatchers.IO) {
                favoriteHelper.removeFromFavorite(username).toLong()
            }
        } else {

            GlobalScope.launch(Dispatchers.IO) {

                val values = ContentValues()
                values.put(DatabaseContract.FavoriteColumns._ID, id)
                values.put(DatabaseContract.FavoriteColumns.LOGIN, username)
                values.put(DatabaseContract.FavoriteColumns.AVATAR_URL, avatarUrl)

                favoriteHelper.addToFavorite(values)
            }
        }

        getFavoriteStatusFromDB()
    }

    private fun setFavoriteDrawable() {
        if (isFavorite) {
            binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_true)
        } else {
            binding.btnFavorite.setBackgroundResource(R.drawable.ic_favorite_false)
        }
    }

    private fun shareProfileAddress() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }


}