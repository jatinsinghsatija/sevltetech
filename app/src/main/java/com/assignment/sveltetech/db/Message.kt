package com.assignment.sveltetech.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val senderIp: String,
    val message: String,
    val type: Int,
    val sentAt: Long = System.currentTimeMillis()
) {
    fun isSent(): Boolean {
        return (type == 0)
    }
}