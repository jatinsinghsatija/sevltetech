package com.assignment.sveltetech.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.assignment.sveltetech.db.ChatDatabaseClient
import com.assignment.sveltetech.db.Message
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel(val ctx: Context) : ViewModel() {
    val messages = SnapshotStateList<Message>()
    val ownIP = mutableStateOf("")
    val connectionText = mutableStateOf("")
    val connectionTextColor = mutableStateOf<Int?>(null)
    val receiverIP = mutableStateOf("")
    val loggedIn = mutableStateOf(false)

    fun fetchDataBaseMessages() {
        if(messages.size==0) {
            viewModelScope.launch(Dispatchers.IO) {
                ChatDatabaseClient(ctx).getFileAppDatabase().chatDao()?.getAllMessages()?.let {
                    messages.addAll(it)
                }
            }
        }
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            ChatDatabaseClient(ctx).getFileAppDatabase().chatDao()?.insertMessage(message)
        }
    }

    fun backToInitial() {
        messages.clear()
        deleteAllMessages()
        receiverIP.value = ""
    }

    private fun deleteAllMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            ChatDatabaseClient(ctx).getFileAppDatabase().chatDao()?.deleteAllMessages()
        }
    }
}