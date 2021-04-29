package com.dicoding.tugasusergithubv2.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.model.UserDetail
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_LOGIN = "extra_login"
        const val EXTRA_AVATAR = "extra_avatar"
    }

    private lateinit var binding: ActivityDetailBinding
    private var isFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeOnClickListener()
        setFavoriteDrawable()

        val login = intent.getStringExtra(EXTRA_LOGIN)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR)
        binding.apply {

            tvUsername.text = login
            /*tvLocation.text = item.location
            tvCompany.text = item.company
            tvFollowers.text = item.followers.toString()
            tvFollowing.text = item.following.toString()
            tvRepositories.text = item.public_repos.toString()*/

            Glide.with(this@DetailActivity)
                .load(avatarUrl)
                .placeholder(R.drawable.ic_placeholder_avatar)
                .into(imgAvatar)
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