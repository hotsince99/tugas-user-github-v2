package com.dicoding.tugasusergithubv2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class UserDetail(
    var id: Int,
    var login: String,
    var avatar_url: String,
    var name: String,
    var followers: Int,
    var following: Int,
    var location: String,
    var company: String,
    var public_repos: Int
    )
