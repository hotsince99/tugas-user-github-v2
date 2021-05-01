package com.dicoding.tugasusergithubv2.data.local

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract.FavoriteColumns.Companion.LOGIN
import com.dicoding.tugasusergithubv2.data.local.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME
import kotlin.jvm.Throws

class FavoriteHelper(context: Context) {

    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        // inisiasi database
        private var INSTANCE: FavoriteHelper? = null // ganti jadi db??
        fun getInstance(context: Context): FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen) {
            database.close()
        }
    }

    // CRUD
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "_ID ASC"
        )
    }

    fun queryByUsername(username: String): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            "$LOGIN = ?",
            arrayOf(username),
            null,
            null,
            null,
            null
        )
    }

    fun addToFavorite(values: ContentValues?): Long {
        return database.insert(
            DATABASE_TABLE,
            null,
            values
        )
    }

    fun removeFromFavorite(username: String): Int {
        return database.delete(
            DATABASE_TABLE,
            "$LOGIN = '$username'",
            null
        )
    }
}