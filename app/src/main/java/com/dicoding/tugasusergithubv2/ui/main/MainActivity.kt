package com.dicoding.tugasusergithubv2.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.tugasusergithubv2.R
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.dicoding.tugasusergithubv2.databinding.ActivityMainBinding
import com.dicoding.tugasusergithubv2.ui.detail.DetailActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter
    //private lateinit var viewModel: MainViewModel
    private val list = ArrayList<UserItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvSearch.setHasFixedSize(true)

        //list.addAll(getListUsers())
        //list.addAll(searchUserFromGitHub("query"))
        showRecyclerList()
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
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showRecyclerList() {
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        adapter = ListUserAdapter(list)
        binding.rvSearch.adapter = adapter

        adapter.setCallback(object : ListUserAdapter.Callback {
            override fun onItemClick(user: UserItem) {
                showSelectedUser(user)
            }
        })
    }

    fun getListUsers(): ArrayList<UserItem> {
        val id = resources.getStringArray(R.array.id)
        val login = resources.getStringArray(R.array.login)
        val avatarUrl = resources.getStringArray(R.array.avatar_url)

        val listUser = ArrayList<UserItem>()
        for (position in id.indices) {
            val user = UserItem(
                    id[position].toInt(),
                    login[position],
                    avatarUrl[position],
            )
            listUser.add(user)
        }
        return listUser
    }

    private fun searchUserFromGitHub(query: String): ArrayList<UserItem> {
        showProgressBar(true)

        val listUser = ArrayList<UserItem>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=hotsince"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                showProgressBar(false)

                val result = String(responseBody)
                Log.d("Josua",  result)
                try {
                    val responseObject = JSONObject(result)

                    val items = responseObject.getJSONArray("items")
                    Log.d("Josua items count", items.length().toString())

                    for (i in 0 until items.length()) {
                        Log.d("Josua", i.toString())
                        val id = items.getJSONObject(i).getInt("id")
                        val login = items.getJSONObject(i).getString("login")
                        val avatarUrl = items.getJSONObject(i).getString("avatar_url")

                        /*Log.d("Josua id", id.toString())
                        Log.d("Josua login", login.toString())
                        Log.d("Josua avatar", avatarUrl.toString())*/

                        val user = UserItem(
                                id,
                                login,
                                avatarUrl,
                        )
                        listUser.add(user)
                    }

                    adapter.setData(listUser)
                    Log.d("Josua listuser size", listUser.size.toString())
                    Log.d("Josua list size", list.size.toString())
                } catch (e: Exception) {
                    showToast(e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                showToast(errorMessage)
            }
        })
        return listUser
    }

    private fun showSelectedUser(user: UserItem) {
        //Toast.makeText(this, "Kamu memilih ${user.login}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this@MainActivity, DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_LOGIN, user.login)
        intent.putExtra(DetailActivity.EXTRA_AVATAR, user.avatar_url)
        startActivity(intent)
    }

    private fun showProgressBar(isEnabled: Boolean) {
        if (isEnabled) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_SHORT).show()
    }
}