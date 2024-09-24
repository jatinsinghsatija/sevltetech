package com.assignment.sveltetech.views.activity

import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.assignment.sveltetech.db.ChatDatabaseClient
import com.assignment.sveltetech.utils.ChatThread
import com.assignment.sveltetech.listener.CommonListener
import com.assignment.sveltetech.utils.SharedpreferenceUtility
import com.assignment.sveltetech.utils.User
import com.assignment.sveltetech.viewmodelfactory.ChatViewModelFactory
import com.assignment.sveltetech.viewmodels.ChatViewModel
import com.assignment.sveltetech.views.chatbox.ChatScreen
import com.assignment.sveltetech.views.ipscreen.IPScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(), CommonListener {
    private lateinit var viewModel: ChatViewModel
    private lateinit var factory: ChatViewModelFactory
    private lateinit var s: ChatThread
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize()
        setContent {
            val session by remember { viewModel.loggedIn }
            if (session) {
                connectToIP()
                ChatScreen(viewModel, this)
            } else {
                backToInitials()
                IPScreen(viewModel, this)
            }
        }
    }

    fun backToInitials(){
        viewModel.backToInitial()
    }

    fun connectToIP() {
        val rIP = SharedpreferenceUtility.getInstance(this)?.getString("receiver_ip")
        viewModel.receiverIP.value = rIP ?: ""
        if (rIP != "") {
            s = ChatThread(
                viewModel.ownIP.value,
                viewModel.connectionText,
                viewModel.connectionTextColor,
                viewModel.messages,
                viewModel
            )
            s.start()
        }
        viewModel.fetchDataBaseMessages()
    }

    fun initialize() {
        factory = ChatViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        val wm = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
        viewModel.ownIP.value = Formatter.formatIpAddress(wm.connectionInfo.getIpAddress())
        val rIP = SharedpreferenceUtility.getInstance(this)?.getString("receiver_ip")
        val loggedIn = SharedpreferenceUtility.getInstance(this)?.getBoolean("logged_in")
        viewModel.loggedIn.value = loggedIn?:false
        viewModel.receiverIP.value = rIP ?: ""
    }

    override fun onLogOut() {
        SharedpreferenceUtility.getInstance(this)?.putString("receiver_ip", "")
        SharedpreferenceUtility.getInstance(this)?.putBoolean("logged_in", false)
        viewModel.loggedIn.value=false
    }

    override fun onConnectToIP(ip: String) {
        if (Patterns.IP_ADDRESS.matcher(ip).matches()) {
            SharedpreferenceUtility.getInstance(this)?.putString("receiver_ip", ip)
            SharedpreferenceUtility.getInstance(this)?.putBoolean("logged_in", true)
            viewModel.loggedIn.value=true
        } else {
            Toast.makeText(this, "Please Enter a Valid IP Address", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSendMessage(message: String) {
        if (!message.isEmpty()) {
            val user = User(message, viewModel.receiverIP.value, viewModel.messages,viewModel)
            user.sendMessage()
        } else {
            val toast =
                Toast.makeText(applicationContext, "Please write something", Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}