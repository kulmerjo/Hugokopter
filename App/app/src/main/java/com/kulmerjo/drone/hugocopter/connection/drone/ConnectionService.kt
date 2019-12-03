package com.kulmerjo.drone.hugocopter.connection.drone

import com.kulmerjo.drone.hugocopter.connection.drone.async.models.DroneControlData


interface ConnectionService {

    fun isConnectedToDrone(): Boolean

    fun sendDataToDrone(droneControlData: DroneControlData)

}