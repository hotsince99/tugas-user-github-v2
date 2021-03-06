package com.dicoding.tugasusergithubv2.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract.FavoriteColumns.Companion.AUTHORITY
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import com.dicoding.tugasusergithubv2.data.local.FavoriteHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val URI_CODE = 1
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteHelper: FavoriteHelper

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, URI_CODE)
        }
    }

    override fun onCreate(): Boolean {
        favoriteHelper = FavoriteHelper.getInstance(context as Context)
        favoriteHelper.open()
        Log.d("Josua", "onCreate() FavoriteProvider")
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            URI_CODE -> favoriteHelper.queryAll()
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }
}