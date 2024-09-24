package com.assignment.sveltetech.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.viewmodels.ChatViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import java.util.Calendar

class ChatThread : Thread {

    private val ownIp: String?
    private val text: MutableState<String>?
    private val textBackColor: MutableState<Int?>?
    private val messageArray: SnapshotStateList<Message>?
    private val viewModel: ChatViewModel?

    constructor(
        ownIp: String,
        text: MutableState<String>,
        textBackColor: MutableState<Int?>,
        messageArray: SnapshotStateList<Message>,
        viewModel: ChatViewModel?
    ) : super() {
        this.ownIp = ownIp
        this.text = text
        this.textBackColor = textBackColor
        this.messageArray = messageArray
        this.viewModel = viewModel
    }

    private val TAG = "CHATSERVER"
    private val port = 1030

    @SuppressLint("SetTextI18n")
    override fun run() {
        try {
            val initSocket = ServerSocket(port).apply {
                reuseAddress = true
            }
            text?.value = "Server Socket Started at IP: $ownIp and Port: $port"
            textBackColor?.value=Color.parseColor("#39FF14")
            Log.i(TAG, "Server started")

            while (!isInterrupted) {
                val connectSocket = initSocket.accept()
                // Launch a coroutine for handling received messages
                GlobalScope.launch(Dispatchers.IO) {
                    receiveTexts(connectSocket)
                }
            }
            initSocket.close()
        } catch (e: IOException) {
            text?.value = "Server Socket initialization failed. Port already in use."
            textBackColor?.value=Color.parseColor("#FF0800")
            e.printStackTrace()
        }
    }

    private suspend fun receiveTexts(socket: Socket) {
        var text: String? = null
        try {
            val input = BufferedReader(InputStreamReader(socket.getInputStream()))
            text = input.readLine()
            Log.i(TAG, "Received => $text")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        text?.let { result ->
            Log.d(TAG, "onPostExecute: Result $result")
            withContext(Dispatchers.Main) {
                val mm=Message(0,result, Calendar.getInstance().timeInMillis,1 )
                messageArray?.add(mm)
                viewModel?.insertMessage(mm)
            }
        }
    }
}