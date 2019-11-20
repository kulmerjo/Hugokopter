package com.kulmerjo.drone.hugocopter.connection.async.tcp

import android.app.IntentService
import android.content.Intent
import java.net.Socket

class AsyncTcpClient(private val ipAddress: String, private val port : Int)
    : IntentService("TCP_CLIENT") {

    private var serverSocket : Socket = Socket(ipAddress, port)

    override fun onHandleIntent(intent: Intent?) {
        serverSocket = Socket(ipAddress, port)
        return
    }

    fun isConnected() : Boolean {
        return serverSocket.isConnected
    }

    fun getConnectedServerIpAddress() : String {
        return serverSocket.inetAddress.hostAddress
    }

    fun getConnectedServerPort() : Int {
        return serverSocket.port
    }

}
