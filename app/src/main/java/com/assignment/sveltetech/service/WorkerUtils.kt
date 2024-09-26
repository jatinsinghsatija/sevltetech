package com.assignment.sveltetech.service

import android.app.Activity
import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import androidx.lifecycle.viewModelScope
import com.assignment.sveltetech.db.ChatDatabaseClient
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.manager.SocketManager
import com.assignment.sveltetech.utils.SharedpreferenceUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object WorkerUtils {
    val socketManager=SocketManager()
    const val port=1030

    fun Context.connectToServer(onServerConnect:(()->Unit)?=null,onNewMessage:((msg:Message)->Unit)?=null) {
        val rIP = SharedpreferenceUtility.getInstance(this)?.getString("receiver_ip")
        if(!rIP.isNullOrEmpty() && socketManager.getSocket()?.connected() != true) {
            socketManager.connect("http://$rIP:$port")
            socketManager.onNewMessage { message, senderIp ->
                val msgs=Message(senderIp = senderIp, message = message, type = 0)
                onNewMessage?.invoke(msgs)
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        insertMessage(msgs)
                    }
                }
            }
            onServerConnect?.invoke()
        }
    }

    suspend fun Context.insertMessage(message: Message) {
            ChatDatabaseClient(this).getFileAppDatabase().chatDao()?.insertMessage(message)
    }

    fun Context.getMyIP():String{
        val wm = this.getSystemService(Activity.WIFI_SERVICE) as WifiManager
        return Formatter.formatIpAddress(wm.connectionInfo.getIpAddress())
    }
}