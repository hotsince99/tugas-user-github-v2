package com.dicoding.tugasusergithubv2.data.local

import android.database.Cursor
import com.dicoding.tugasusergithubv2.data.model.UserFavorite

object MappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<UserFavorite> {
        val favoriteList = ArrayList<UserFavorite>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                favoriteList.add(UserFavorite(id, login, avatarUrl))
            }
        }
        return favoriteList
    }
}