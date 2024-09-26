package com.assignment.sveltetech.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room.databaseBuilder
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


class ChatDatabaseClient(mCtx: Context) {
    private val fileAppDatabase: ChatDatabase

    init {
        fileAppDatabase =
            databaseBuilder(mCtx, ChatDatabase::class.java, "chat_database").build()
    }

    fun getFileAppDatabase(): ChatDatabase {
        return fileAppDatabase
    }

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE message ADD COLUMN new_column_name TEXT")
            }
        }
    }
}