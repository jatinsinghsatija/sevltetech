package com.assignment.sveltetech.manager

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import android.util.Log
import android.widget.Toast
import com.assignment.sveltetech.db.Message
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

object SocketManager {
    private lateinit var socket: Socket

    fun connect(ctx:Context) {
        val ipAddress = getLocalIpAddress(ctx)
        socket = IO.socket("http://$ipAddress:3000") // Replace with your server's port
        socket.connect()

        socket.on(Socket.EVENT_CONNECT) {
            Log.d("SocketManager", "Connected")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            Log.d("SocketManager", "Disconnected")
        }
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.d("SocketManager", it[0].toString())
        }
    }

    fun onMessageRecieve(onReceive:(message: Message)->Unit){
        socket.on("chatMessage") { args ->
            // Parse the incoming message and handle it
            val message = args[0] as JSONObject
            val id = message.getInt("id")
            val deviceId = message.getString("deviceId")
            val sender = message.getString("sender")
            val content = message.getString("content")
            val timestamp = message.getLong("timestamp")
            onReceive.invoke(Message(id=id,deviceId=deviceId, sender=sender, content=content, timestamp=timestamp))
        }
    }

    fun getSocket() = socket

    private fun getLocalIpAddress(context:Context): String {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInfo = wifiManager.connectionInfo
        val ipAddress = wifiInfo.ipAddress
        return Formatter.formatIpAddress(ipAddress)
    }
}