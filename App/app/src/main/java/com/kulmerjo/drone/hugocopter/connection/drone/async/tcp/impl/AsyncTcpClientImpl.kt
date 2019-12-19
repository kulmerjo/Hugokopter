package com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.impl

import com.kulmerjo.drone.hugocopter.control.models.control.DroneControlData
import com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.AsyncTcpClient
import com.kulmerjo.drone.hugocopter.control.models.info.DroneInfoData
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.stringify
import java.net.Socket
import java.util.concurrent.CompletableFuture

class AsyncTcpClientImpl(private val ipAddress: String, private val port: Int)
    : Thread(), AsyncTcpClient {

    private var serverSocket : Socket? = null

    override fun run() {
        super.run()
        serverSocket = Socket(ipAddress, port)
    }

    override fun isConnected() : Boolean {
        serverSocket?.getInputStream()?.read()
        return serverSocket?.isConnected ?: false
    }

    @ImplicitReflectionSerializer
    override fun  sendControlDataAsync(controlData : DroneControlData) {
        CompletableFuture.runAsync {
            sendData(controlData)
        }
    }

    @ImplicitReflectionSerializer
    override fun sendInfoDataAsync(infoData: DroneInfoData) {
        CompletableFuture.runAsync{
            sendData(infoData)
        }
    }

    @ImplicitReflectionSerializer
    private fun sendData(controlData: Any) {
        serverSocket?.getOutputStream()?.write(Json.stringify(controlData).toByteArray())
    }

}
