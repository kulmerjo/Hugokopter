package com.kulmerjo.drone.hugocopter.connection.async.models

data class DroneControlData(
    val dataType : String,
    val controlType: String,
    val speed : Double
)