package com.assignment.sveltetech.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceId: String,
    val sender: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)