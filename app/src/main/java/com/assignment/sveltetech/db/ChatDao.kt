package com.assignment.sveltetech.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatDao {
    @Insert
    suspend fun insertMessage(message: Message)

    @Query("SELECT * FROM messages ORDER BY sentAt ASC")
    fun getAllMessages(): List<Message>

    @Query("DELETE FROM messages")
    suspend fun deleteAllMessages()
}