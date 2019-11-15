package com.kulmerjo.drone.hugocopter.connection.async.tcp

import java.net.Socket

class AsyncTcpClient(private val ipAddress: String, private val port : Int) : Thread() {

    private var serverSocket : Socket? = null

    override fun run() {
        serverSocket = Socket(ipAddress, port)
        super.run()
    }

    fun getConnectedServerIpAddress() : String? {
        return serverSocket?.inetAddress?.hostAddress
    }

    fun getConnectedServerPort() : Int? {
        return serverSocket?.port
    }

}
