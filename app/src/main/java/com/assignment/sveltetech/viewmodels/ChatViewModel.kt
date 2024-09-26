package com.assignment.sveltetech.viewmodels

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.assignment.sveltetech.db.ChatDatabaseClient
import com.assignment.sveltetech.db.Message
import com.assignment.sveltetech.manager.SocketManager
import com.assignment.sveltetech.service.SocketWorker
import com.assignment.sveltetech.service.WorkerUtils
import com.assignment.sveltetech.service.WorkerUtils.connectToServer
import com.assignment.sveltetech.service.WorkerUtils.getMyIP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ChatViewModel(val ctx: Context) : ViewModel() {
    val messages = SnapshotStateList<Message>()
    val loggedIn = mutableStateOf(false)
    val receiverIP = mutableStateOf("")

    private val socketManager = SocketManager()

    fun ConnectToServer() {
        ctx.connectToServer({
            ctx.startWorker()
        },{
            messages.add(it)
        })
    }

    fun Context.startWorker(){
        val workRequest = PeriodicWorkRequestBuilder<SocketWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork("SocketWork", ExistingPeriodicWorkPolicy.KEEP, workRequest)
    }

    fun sendMessage(content: String) {
        socketManager.sendMessage(content, ctx.getMyIP())
    }

    fun fetchDataBaseMessages() {
        if(messages.size==0) {
            viewModelScope.launch(Dispatchers.IO) {
                ChatDatabaseClient(ctx).getFileAppDatabase().chatDao()?.getAllMessages()?.let {
                    messages.addAll(it)
                }
            }
        }
    }



    fun backToInitial() {
        messages.clear()
        deleteAllMessages()
        loggedIn.value=false
    }

    private fun deleteAllMessages() {
        viewModelScope.launch(Dispatchers.IO) {
            ChatDatabaseClient(ctx).getFileAppDatabase().chatDao()?.deleteAllMessages()
        }
    }
}