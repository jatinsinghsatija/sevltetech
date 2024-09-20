package com.assignment.sveltetech.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.assignment.sveltetech.db.ChatDatabase
import com.assignment.sveltetech.db.ChatDatabaseClient
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.db.User
import com.assignment.sveltetech.manager.SocketManager
import kotlinx.coroutines.launch

class ChatViewModel(context: Context) : ViewModel() {
    val chatDao = ChatDatabaseClient.getInstance(context)?.getFileAppDatabase()?.chatDao()
    val messages: LiveData<List<Message>>? = chatDao?.getAllMessages()

    private var userId: String? = null

    fun setUser(deviceId: String, name: String) {
        viewModelScope.launch {
            val user = User(deviceId, name)
            chatDao?.insertUser(user)
            userId = deviceId
        }
    }

    fun sendMessage(content: String) {
       if (SocketManager.getSocket().connected()){
            userId?.let { deviceId ->
                val message = Message(deviceId = deviceId, sender = deviceId, content = content)
                viewModelScope.launch {
                    chatDao?.insertMessage(message)
                    // Emit the message through Socket.IO
                    SocketManager.getSocket().emit("chatMessage", message)
                }
            }
        }
    }

    fun receiveMessage(msg: Message) {
        if (SocketManager.getSocket().connected()) {
            viewModelScope.launch {
                chatDao?.insertMessage(msg)
            }
        }
    }

    fun getUser(deviceId: String): LiveData<User?> {
        return liveData {
            emit(chatDao?.getUserById(deviceId))
        }
    }
}