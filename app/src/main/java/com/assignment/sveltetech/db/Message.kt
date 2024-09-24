package com.assignment.sveltetech.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "messages")
data class Message(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String,
    val sentAt: Long,
    val type: Int
) {
    fun isSent(): Boolean {
        return (type == 0)
    }
}