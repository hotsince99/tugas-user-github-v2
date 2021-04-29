package com.dicoding.tugasusergithubv2.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.model.UserDetail
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_LOGIN = "extra_login"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeOnClickListener()
        setFavoriteDrawable()

        val username = intent.getStringExtra(EXTRA_LOGIN) as String

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailViewModel::class.java)

        viewModel.setProfile(username)
        viewModel.getProfile().observe(this, {
            if (it != null) {
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
        isFavorite = !isFavorite
        setFavoriteDrawable()
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