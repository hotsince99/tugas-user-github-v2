package com.dicoding.tugasusergithubv2.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItem>>()

    fun setUsers(query: String){

        val listUser = ArrayList<UserItem>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$query"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

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

                        val user = UserItem(
                            id,
                            login,
                            avatarUrl,
                        )
                        listUser.add(user)
                    }

                    listUsers.postValue(listUser)
                } catch (e: Exception) {
                    Log.d("Josua", e.message.toString())
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
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

    fun getUsers(): LiveData<ArrayList<UserItem>> {
        return listUsers
    }
}