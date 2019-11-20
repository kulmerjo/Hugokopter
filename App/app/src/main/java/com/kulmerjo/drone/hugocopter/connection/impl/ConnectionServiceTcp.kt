package com.kulmerjo.drone.hugocopter.connection.impl

import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.async.tcp.AsyncTcpClient


class ConnectionServiceTcp(private val tcpClient : AsyncTcpClient) : ConnectionService {


    override fun isConnectedToDrone(): Boolean {
        return tcpClient.isConnected()
    }

}