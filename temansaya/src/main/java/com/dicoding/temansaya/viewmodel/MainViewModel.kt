package com.dicoding.temansaya.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temansaya.data.UserFavorite

class MainViewModel : ViewModel() {

    private val listFavoriteUsers = MutableLiveData<ArrayList<UserFavorite>>()

    fun setUsers(listUser: ArrayList<UserFavorite>) {
        listFavoriteUsers.postValue(listUser)
    }

    fun getUsers(): LiveData<ArrayList<UserFavorite>> {
        return listFavoriteUsers
    }
}