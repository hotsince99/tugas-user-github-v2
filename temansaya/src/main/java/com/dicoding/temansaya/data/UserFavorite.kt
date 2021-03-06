package com.dicoding.temansaya.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserFavorite (
     var id: Int = 0,
     var login: String? = null,
     var avatarUrl: String? = null
) : Parcelable
