package com.assignment.sveltetech.db

import android.annotation.SuppressLint
import android.content.Context
import androidx.room.Room.databaseBuilder


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
        @SuppressLint("StaticFieldLeak")
        private var mInstance: ChatDatabaseClient? = null
        @Synchronized
        fun getInstance(mCtx: Context): ChatDatabaseClient? {
            if (mInstance == null) {
                mInstance = ChatDatabaseClient(mCtx)
            }
            return mInstance
        }
    }
}