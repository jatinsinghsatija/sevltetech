package com.assignment.sveltetech.utils

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.viewmodels.ChatViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket
import java.util.Calendar

class User(
    private val message: String,
    private val serverIpAddress: String,
    private val messageArray: SnapshotStateList<Message>,
    private val viewModel: ChatViewModel
) {

    private val TAG = "User"

    fun sendMessage() {
        // Launch a coroutine in the main scope
        CoroutineScope(Dispatchers.IO).launch {
            var result: String? = null
            try {
                val clientSocket = Socket(serverIpAddress, 1030)
                val outToServer: OutputStream = clientSocket.getOutputStream()
                val output = PrintWriter(outToServer)
                output.println(message)
                output.flush()
                clientSocket.close()
                result = message // Return the message sent
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Update the UI on the main thread
            withContext(Dispatchers.Main) {
                result?.let { onMessageSent(it) }
            }
        }
    }

    private fun onMessageSent(result: String) {
        Log.i(TAG, "on post execution result => $result")
        val msg = Message(0, result, Calendar.getInstance().timeInMillis, 0)
        messageArray.add(msg)
        viewModel.insertMessage(msg)
    }
}