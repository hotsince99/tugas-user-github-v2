package com.dicoding.tugasusergithubv2.data.local

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite_users"
            const val _ID = "_id"
            const val LOGIN = "login"
            const val AVATAR_URL = "avatar_url"
        }
    }
}