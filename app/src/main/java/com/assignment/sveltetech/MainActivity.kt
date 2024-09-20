package com.assignment.sveltetech

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.assignment.sveltetech.manager.SocketManager
import com.assignment.sveltetech.ui.theme.SveltetechTheme
import com.assignment.sveltetech.viewmodelfactory.ChatViewModelFactory
import com.assignment.sveltetech.viewmodels.ChatViewModel
import com.assignment.sveltetech.views.chatbox.ChatScreen
import com.assignment.sveltetech.views.user.UsernameScreen

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var factory: ChatViewModelFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()

        setContent {
            var username by remember { mutableStateOf("") }
            if (username.isEmpty()) {
                UsernameScreen(this,viewModel) { name -> username = name }
            } else {
                ChatScreen(viewModel, username)
            }
        }
    }

    fun initialize(){
        factory= ChatViewModelFactory(this)
        viewModel = ViewModelProvider(this,factory).get(ChatViewModel::class.java)
        SocketManager.connect(this)
        SocketManager.onMessageRecieve {
            viewModel.receiveMessage(it)
        }
    }
}