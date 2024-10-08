package com.assignment.sveltetech.views.ipscreen

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.assignment.sveltetech.listener.CommonListener
import com.assignment.sveltetech.viewmodels.ChatViewModel

@Composable
fun IPScreen(viewModel: ChatViewModel,listener:CommonListener) {
    var receiverIP by remember { viewModel.receiverIP }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                modifier=Modifier.weight(1f),
                value = receiverIP,
                onValueChange = { receiverIP = it },
                label = { Text("EnterIP Address") }
            )
            Button(onClick = {
                    listener.onConnectToIP(receiverIP)
            }) {
                Text("Connect")
            }
        }
    }
}