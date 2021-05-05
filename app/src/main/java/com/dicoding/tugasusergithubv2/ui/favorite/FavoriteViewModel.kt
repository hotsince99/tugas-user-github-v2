package com.dicoding.tugasusergithubv2.ui.favorite

import android.app.Application
import android.database.ContentObserver
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasusergithubv2.data.local.FavoriteHelper
import com.dicoding.tugasusergithubv2.data.model.UserItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    private val listFavoriteUsers = MutableLiveData<ArrayList<UserItem>>()

    fun setUsers(listUser: ArrayList<UserItem>) {
        listFavoriteUsers.postValue(listUser)
    }

    fun getUsers(): LiveData<ArrayList<UserItem>> {
        return listFavoriteUsers
    }
}