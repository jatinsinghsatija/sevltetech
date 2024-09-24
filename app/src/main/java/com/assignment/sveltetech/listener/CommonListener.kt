package com.assignment.sveltetech.listener

interface CommonListener {

    fun onLogOut(){}
    fun onConnectToIP(ip:String){}
    fun onSendMessage(message:String){}
}