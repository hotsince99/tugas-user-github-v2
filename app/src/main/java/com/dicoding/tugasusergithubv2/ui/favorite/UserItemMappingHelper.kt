package com.dicoding.tugasusergithubv2.ui.favorite

import android.database.Cursor
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract
import com.dicoding.tugasusergithubv2.data.model.UserItem

object UserItemMappingHelper {
    fun mapCursorToArrayList(favoriteCursor: Cursor?): ArrayList<UserItem> {
        val favoriteList = ArrayList<UserItem>()
        favoriteCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns._ID))
                val login = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.LOGIN))
                val avatarUrl = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR_URL))
                favoriteList.add(UserItem(id, login, avatarUrl))
            }
        }
        return favoriteList
    }
}