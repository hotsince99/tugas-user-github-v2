package com.dicoding.tugasusergithubv2.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.tugasusergithubv2.data.model.UserItem

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItem>>()
}