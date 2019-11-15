package com.kulmerjo.drone.hugocopter.connection.impl

import android.util.Log
import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.async.tcp.AsyncTcpClient


class ConnectionServiceTcp(private val tcpClient : AsyncTcpClient) : ConnectionService {

    init {
        startTcpServer()
    }

    private fun startTcpServer() {
        tcpClient.start()
    }

    override fun isConnectedToDrone(): Boolean {
        Log.println(Log.INFO, null, "Adres = ${tcpClient.getConnectedServerIpAddress()}" +
                " port = ${tcpClient.getConnectedServerPort()}")
        return false
    }

}