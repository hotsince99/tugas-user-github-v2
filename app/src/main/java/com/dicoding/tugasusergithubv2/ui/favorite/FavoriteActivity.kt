package com.dicoding.tugasusergithubv2.ui.favorite

import android.content.ContentResolver
import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityFavoriteBinding
import com.dicoding.tugasusergithubv2.databinding.ActivityMainBinding
import com.dicoding.tugasusergithubv2.ui.detail.DetailActivity
import com.dicoding.tugasusergithubv2.ui.main.ListUserAdapter
import com.dicoding.tugasusergithubv2.ui.main.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)


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

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FavoriteViewModel::class.java)
        showProgressBar(true)

        loadAsync()


        viewModel.getUsers().observe(this, {
            if (it != null) {
                adapter.setData(it)
                showProgressBar(false)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.list_of_favorite_user)
    }

    private fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                UserItemMappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteList = deferredFavorite.await()

            if (favoriteList.size > 0) {
                viewModel.setUsers(favoriteList)
            } else {
                showSnackBarMessage("Tidak ada data saat ini")
            }

        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvFavorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showSelectedUser(user: UserItem) {
        //Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@FavoriteActivity, DetailActivity::class.java)
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