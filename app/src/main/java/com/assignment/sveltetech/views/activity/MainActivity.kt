package com.assignment.sveltetech.views.activity

import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelProvider
import com.assignment.sveltetech.listener.CommonListener
import com.assignment.sveltetech.utils.SharedpreferenceUtility
import com.assignment.sveltetech.viewmodelfactory.ChatViewModelFactory
import com.assignment.sveltetech.viewmodels.ChatViewModel
import com.assignment.sveltetech.views.chatbox.ChatScreen
import com.assignment.sveltetech.views.ipscreen.IPScreen

class MainActivity : ComponentActivity(), CommonListener {
    private lateinit var viewModel: ChatViewModel
    private lateinit var factory: ChatViewModelFactory
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
        viewModel.ConnectToServer()
    }

    fun initialize() {
        factory = ChatViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory).get(ChatViewModel::class.java)
        val loggedIn = SharedpreferenceUtility.getInstance(this)?.getBoolean("logged_in")
        viewModel.loggedIn.value = loggedIn?:false
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
}