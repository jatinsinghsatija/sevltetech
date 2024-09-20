package com.assignment.sveltetech.views.user

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.assignment.sveltetech.viewmodels.ChatViewModel
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import java.util.UUID

@Composable
fun UsernameScreen(context: Context, viewModel: ChatViewModel, onNavigate: (String) -> Unit) {
    val deviceId = getDeviceId(context)
    var username by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
        TextField(value = username, onValueChange = { username = it }, label = { Text("Enter your username") })
        Button(onClick = {
            viewModel.setUser(deviceId, username)
            onNavigate(username)
        }) {
            Text("Join Chat")
        }
    }
}

private fun getDeviceId(context: Context): String {
    // Fetch the device ID (e.g., from Google Play Services)
    return UUID.randomUUID().toString()
}