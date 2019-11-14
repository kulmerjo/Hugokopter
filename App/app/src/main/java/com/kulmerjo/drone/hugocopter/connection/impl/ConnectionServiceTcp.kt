package com.kulmerjo.drone.hugocopter.connection.impl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import java.net.Socket

class ConnectionServiceTcp(private val ipAddress: String, private val port : Int) : Service(), ConnectionService {

    private var client : Socket = Socket(ipAddress, port)

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun isConnectedToDrone(): Boolean {
        Log.println(Log.INFO, null, "Adres = ${client.localAddress} port = ${client.localPort}")
        return false
    }

}