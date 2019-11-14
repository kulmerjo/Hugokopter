package com.kulmerjo.drone.hugocopter.helper

import android.content.Context

/**
 * @author Artur Kulig.
 * Helper which can read all resource files and return their properties
 * Bean is declared in AppModule and can be injected anywhere.
 */
interface ResourcesHelper {


    /**
     * All declared properties names
     */
    companion object {
        const val droneAddressPropertyName = "drone.connection.address"
        const val dronePortPropertyName = "drone.connection.port"
    }

    /**
     * @param context - global context of the app
     * @param configFile - file which will be read
     * @param propertyName - property which will be read from configFile
     */
    fun getConfigValueAsString(context: Context, configFile: Int, propertyName : String) : String

    /**
     * @param context - global context of the app
     * @param configFile - file which will be read
     * @param propertyName - property which will be read from configFile
     */
    fun getConfigValueAsInt(context: Context, configFile: Int, propertyName : String) : Int

}