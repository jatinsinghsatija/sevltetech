package com.assignment.sveltetech.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val deviceId: String,
    val name: String
)