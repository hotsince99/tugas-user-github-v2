package com.dicoding.tugasusergithubv2.ui.detail.tabs.followers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasusergithubv2.BuildConfig
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersViewModel : ViewModel() {

    val listFollowers = MutableLiveData<ArrayList<UserItem>>()

    fun setFollowers(query: String) {

        val listUser = ArrayList<UserItem>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ${BuildConfig.API_KEY}")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$query/followers"

        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d("Josua",  result)

                try {
                    val items = JSONArray(result)

                    for (i in 0 until items.length()) {
                        Log.d("Josua", i.toString())

                        val id = items.getJSONObject(i).getInt("id")
                        val login = items.getJSONObject(i).getString("login")
                        val avatarUrl = items.getJSONObject(i).getString("avatar_url")

                        val user = UserItem(
                                id,
                                login,
                                avatarUrl,
                        )
                        listUser.add(user)
                    }

                    listFollowers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Josua", e.message.toString())
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Log.d("Josua", errorMessage)
            }

        })
    }

    fun getFollowers(): LiveData<ArrayList<UserItem>> {
        return listFollowers
    }
}