package com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.impl

import com.kulmerjo.drone.hugocopter.connection.drone.async.models.DroneControlData
import com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.AsyncTcpClient
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.net.Socket
import java.util.concurrent.CompletableFuture

class AsyncTcpClientImpl : Thread(), AsyncTcpClient {

    private var serverSocket : Socket? = null
    private var ipAddress : String = "192.168.42.1"
    private var port : Int = 8080

    override fun run() {
        super.run()
        serverSocket = Socket(ipAddress, port)
    }

    override fun isConnected() : Boolean {
        serverSocket?.getInputStream()?.read()
        return serverSocket?.isConnected ?: false
    }

    @ImplicitReflectionSerializer
    override fun sendControlDataAsync(controlData : DroneControlData) {
        CompletableFuture.runAsync {
            sendControlData(controlData)
        }
    }

    @ImplicitReflectionSerializer
    private fun sendControlData(controlData: DroneControlData) {
        serverSocket?.getOutputStream()?.write(Json.stringify(controlData).toByteArray())

    }


}
