package com.dicoding.temansaya

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val listFavoriteUsers = MutableLiveData<ArrayList<UserFavorite>>()

    fun setUsers(listUser: ArrayList<UserFavorite>) {
        listFavoriteUsers.postValue(listUser)
    }

    fun getUsers(): LiveData<ArrayList<UserFavorite>> {
        return listFavoriteUsers
    }
}