package com.assignment.sveltetech.views.chatbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.assignment.sveltetech.viewmodels.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, username: String) {
    val messages = viewModel.messages?.observeAsState(emptyList())
    var messageText by remember { mutableStateOf("") }

    Column {

        LazyColumn(modifier=Modifier.fillMaxWidth().weight(1f)) {
            messages?.value?.let {
                items(it) { message ->
                    Text("${message.sender}: ${message.content}")
                }
            }

        }
        TextField(value = messageText, onValueChange = { messageText = it }, label = { Text("Type a message") })
        Button(onClick = {
            viewModel.sendMessage(messageText)
            messageText = ""
        }) {
            Text("Send")
        }
    }
}