package com.assignment.sveltetech.views.chatbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.listener.CommonListener
import com.assignment.sveltetech.viewmodels.ChatViewModel

@Composable
fun ChatScreen(viewModel: ChatViewModel, listener: CommonListener) {
    val messages = remember { viewModel.messages }
    var messageText by remember { mutableStateOf("") }
    var receiverIP by remember { viewModel.receiverIP }
    var conText by remember { viewModel.connectionText }
    var conTextColor by remember { viewModel.connectionTextColor }

    Column(modifier=Modifier.fillMaxSize().imePadding()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(10.dp)
        ) {
            Text(modifier = Modifier.weight(1f), text = "Connected to $receiverIP")
            Image(
                modifier = Modifier.clickable {
                    listener.onLogOut()
                },
                painter = painterResource(id = android.R.drawable.ic_lock_power_off),
                contentDescription = null
            )
        }
        val color = if (conTextColor != null) Color(conTextColor!!) else Color.Black
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = color)
                .padding(5.dp),
            text = conText,
            color = Color.White
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            reverseLayout = true
        ) {
            items(messages) { message ->
                ChatMessageItem(message)

            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp),
                value = messageText,
                onValueChange = { messageText = it },
                label = { Text("Type a message") }
            )
            Text(
                text = "Send",
                color = Color.White,
                modifier = Modifier
                    .background(Color.Black, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .padding(10.dp)
                    .clickable {
                        listener.onSendMessage(messageText)
                        messageText = ""
                    }
            )
        }
    }
}

@Composable
fun ChatMessageItem(message: Message) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val mod = if (message.isSent()) Modifier
            .align(Alignment.TopEnd)
            .padding(5.dp)
            .background(
                Color.LightGray,
                RoundedCornerShape(5.dp, 0.dp, 5.dp, 5.dp)
            ).padding(10.dp) else Modifier
            .align(Alignment.TopStart)
            .padding(5.dp)
            .background(
                Color.Black,
                RoundedCornerShape(0.dp, 5.dp, 5.dp, 5.dp)
            ).padding(10.dp)
        Text(
            modifier = mod,
            text = message.message,
            color = if (message.isSent()) Color.Black else Color.White
        )
    }
}