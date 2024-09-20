package com.assignment.sveltetech.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.assignment.sveltetech.viewmodels.ChatViewModel

class ChatViewModelFactory(val ctx: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            return ChatViewModel(ctx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}