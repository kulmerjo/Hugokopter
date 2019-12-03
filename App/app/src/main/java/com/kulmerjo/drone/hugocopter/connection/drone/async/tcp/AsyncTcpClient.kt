package com.kulmerjo.drone.hugocopter.connection.drone.async.tcp

import com.kulmerjo.drone.hugocopter.connection.drone.async.models.DroneControlData

interface AsyncTcpClient {

     fun isConnected() : Boolean

     fun sendControlDataAsync(controlData : DroneControlData)
}
