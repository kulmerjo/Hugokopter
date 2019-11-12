package com.kulmerjo.drone.hugocopter.connection.impl

import android.util.Log
import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import java.net.Socket

class ConnectionServiceTcp(ipAddress: String, port : Int) : ConnectionService {

    private val client = Socket(ipAddress, port)

    override fun isConnectedToDrone(): Boolean {
        Log.println(Log.INFO, null, "Adres = ${client.localAddress} port = ${client.port}")
        return false
    }

}