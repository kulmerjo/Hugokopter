package com.kulmerjo.drone.hugocopter.helper

import android.content.Context


interface ResourcesHelper {

    companion object {
        const val droneAddressPropertyName = "drone.connection.address"
        const val dronePortPropertyName = "drone.connection.port"
    }

    fun getConfigValueAsString(context: Context, configFile: Int, propertyName : String) : String

    fun getConfigValueAsInt(context: Context, configFile: Int, propertyName : String) : Int

}