package com.dicoding.tugasusergithubv2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityMainBinding
import com.dicoding.tugasusergithubv2.ui.detail.DetailActivity
import com.dicoding.tugasusergithubv2.ui.favorite.FavoriteActivity
import com.dicoding.tugasusergithubv2.ui.settings.SettingsActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecyclerView()
        initializeViewModel()
        showRecyclerList()
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.app_name)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = "Masukkan kata"
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchUserFromGitHub(query)
                binding.githubImage.visibility = View.GONE
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.btn_favorite_users -> {
                val intent = Intent(this, FavoriteActivity::class.java)
                startActivity(intent)
                }
            R.id.btn_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)
    }

    private fun initializeRecyclerView() {
        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.rvSearch.adapter = adapter
        binding.rvSearch.setHasFixedSize(true)

        adapter.setCallback(object : ListUserAdapter.Callback {
            override fun onItemClick(user: UserItem) {
                showSelectedUser(user)
            }
        })
    }

    private fun showRecyclerList() {
        viewModel.getUsers().observe(this, {
            if (it != null) {
                adapter.setData(it)
                showProgressBar(false)
            }
        })
    }

    private fun searchUserFromGitHub(query: String) {
        showProgressBar(true)
        viewModel.setUsers(query)
    }

    private fun showSelectedUser(user: UserItem) {
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
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