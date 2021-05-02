package com.dicoding.tugasusergithubv2.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasusergithubv2.data.local.FavoriteHelper
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityFavoriteBinding
import com.dicoding.tugasusergithubv2.databinding.ActivityMainBinding
import com.dicoding.tugasusergithubv2.ui.detail.DetailActivity
import com.dicoding.tugasusergithubv2.ui.main.ListUserAdapter
import com.dicoding.tugasusergithubv2.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel
    private lateinit var favoriteHelper: FavoriteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecyclerView()
        initializeViewModel()
        initializeFavoriteDatabase()
        showRecyclerList()
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    private fun initializeFavoriteDatabase() {

        GlobalScope.launch(Dispatchers.Main) {
            favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                UserItemMappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteList = deferredFavorite.await()
            viewModel.setUsers(favoriteList)
        }
    }

    private fun initializeRecyclerView() {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.setHasFixedSize(true)

        adapter.setCallback(object : ListUserAdapter.Callback {
            override fun onItemClick(user: UserItem) {
                showSelectedUser(user)
            }
        })
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel::class.java)
        showProgressBar(true)
    }

    private fun showSelectedUser(user: UserItem) {
        //Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_LOGIN, user.login)
        startActivity(intent)
    }

    private fun showRecyclerList() {
        viewModel.getUsers().observe(this, {
            if (it != null) {
                adapter.setData(it)
                showProgressBar(false)
            }
        })
    }

    private fun showProgressBar(isEnabled: Boolean) {
        if (isEnabled) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}