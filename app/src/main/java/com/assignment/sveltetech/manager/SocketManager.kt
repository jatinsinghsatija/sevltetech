package com.assignment.sveltetech.manager

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket

class SocketManager {
    private var socket: Socket? = null

    fun connect(serverUrl: String) {
        socket = IO.socket(serverUrl)
        socket?.connect()
        socket?.on(Socket.EVENT_CONNECT) {
            Log.e("Connection",it[0].toString())
        }?.on(Socket.EVENT_CONNECT_ERROR){
            Log.e("Connection",it[0].toString())
        }?.on(Socket.EVENT_DISCONNECT){
            Log.e("Connection",it[0].toString())
        }
    }

    fun getSocket():Socket?{
        return socket
    }

    fun sendMessage(message: String, senderIp: String) {
        socket?.emit("new_message", message, senderIp)
    }

    fun onNewMessage(callback: (String, String) -> Unit) {
        socket?.on("new_message") { args ->
            val message = args[0] as String
            val senderIp = args[1] as String
            callback(message, senderIp)
        }
    }

    fun disconnectSocket(){
        socket?.disconnect()
        socket=null
    }
}