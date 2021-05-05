package com.dicoding.temansaya.ui

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.temansaya.*
import com.dicoding.temansaya.data.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.dicoding.temansaya.data.MappingHelper
import com.dicoding.temansaya.data.UserFavorite
import com.dicoding.temansaya.databinding.ActivityMainBinding
import com.dicoding.temansaya.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = ListUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.setHasFixedSize(true)

        adapter.setCallback(object : ListUserAdapter.Callback {
            override fun onItemClick(user: UserFavorite) {
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
        ).get(MainViewModel::class.java)
        showProgressBar(true)

        loadAsync()


        viewModel.getUsers().observe(this, {
            if (it != null) {
                adapter.setData(it)
                showProgressBar(false)
                binding.githubImage.visibility = View.GONE
            }
        })
    }

    private fun loadAsync() {
        GlobalScope.launch(Dispatchers.Main) {

            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favoriteList = deferredFavorite.await()

            if (favoriteList.size > 0) {
                viewModel.setUsers(favoriteList)
            } else {
                showSnackBarMessage()
            }

        }
    }

    private fun showSnackBarMessage() {
        Snackbar.make(binding.rvFavorite, getString(R.string.no_data), Snackbar.LENGTH_SHORT).show()
    }

    private fun showSelectedUser(user: UserFavorite) {
        Toast.makeText(this, getString(R.string.you_choose) + " " + user.login, Toast.LENGTH_SHORT).show()

    }

    private fun showProgressBar(isEnabled: Boolean) {
        if (isEnabled) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}