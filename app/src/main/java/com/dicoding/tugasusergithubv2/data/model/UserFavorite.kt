package com.dicoding.tugasusergithubv2.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFavorite (
     var id: Int = 0,
     var login: String? = null,
     var avatarUrl: String? = null
) : Parcelable
