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

    @Query("SELECT * FROM messages ORDER BY timestamp ASC")
    fun getAllMessages(): LiveData<List<Message>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE deviceId = :deviceId LIMIT 1")
    suspend fun getUserById(deviceId: String): User?
}