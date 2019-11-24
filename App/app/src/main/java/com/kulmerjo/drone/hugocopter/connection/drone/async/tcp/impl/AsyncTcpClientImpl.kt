package com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.impl

import android.app.IntentService
import android.content.Intent
import com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.AsyncTcpClient
import org.koin.android.ext.android.inject
import java.net.Socket

class AsyncTcpClientImpl : IntentService("TCP_CLIENT"), AsyncTcpClient {

    private val ipAddress: String by inject()
    private val port : Int by inject()
    private var serverSocket : Socket? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        serverSocket = Socket(ipAddress, port)
        return START_STICKY
    }

    override fun onHandleIntent(intent: Intent?) {
        TODO("Not implemented yet")
    }

    override fun isConnected() : Boolean {
        return serverSocket?.isConnected ?: false
    }

}
