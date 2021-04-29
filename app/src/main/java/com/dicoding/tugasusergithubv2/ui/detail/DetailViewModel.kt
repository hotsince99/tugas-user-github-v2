package com.dicoding.tugasusergithubv2.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasusergithubv2.data.model.UserDetail
import com.dicoding.tugasusergithubv2.data.model.UserItem
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class DetailViewModel : ViewModel() {
    val userProfile = MutableLiveData<UserDetail>()

    fun setProfile(username: String) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d("Josua",  result)
                try {
                    val profile = JSONObject(result)

                    val id = profile.getInt("id")
                    val login = profile.getString("login")
                    val avatarUrl = profile.getString("avatar_url")
                    val name = profile.getString("name")
                    val location = profile.getString("location")
                    val company = profile.getString("company")
                    val followers = profile.getInt("followers")
                    val following = profile.getInt("following")
                    val public_repos = profile.getInt("public_repos")

                    val user = UserDetail(
                        id,
                        login,
                        avatarUrl,
                        name,
                        followers,
                        following,
                        location,
                        company,
                        public_repos
                    )
                    userProfile.postValue(user)

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

    fun getProfile(): LiveData<UserDetail> {
        return userProfile
    }
}