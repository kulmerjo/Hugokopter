package com.kulmerjo.drone.hugocopter.connection.drone

import com.kulmerjo.drone.hugocopter.control.models.control.DroneControlData
import com.kulmerjo.drone.hugocopter.control.models.info.DroneInfoData


interface ConnectionService {

    fun isConnectedToDrone(): Boolean

    fun sendControlDataToDrone(moveType: String, velocity: Double)

    fun sendConnectedInfoData()

    fun sendDisconnectedInfoData()


}