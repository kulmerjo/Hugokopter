package com.kulmerjo.drone.hugocopter.connection.drone.impl

import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.drone.async.models.DroneControlData
import com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.AsyncTcpClient


class ConnectionServiceTcp(private val tcpClient : AsyncTcpClient) : ConnectionService {


    override fun sendDataToDrone(droneControlData: DroneControlData) {
        tcpClient.sendControlDataAsync(droneControlData)
    }

    override fun isConnectedToDrone(): Boolean {
        return tcpClient.isConnected()
    }


}