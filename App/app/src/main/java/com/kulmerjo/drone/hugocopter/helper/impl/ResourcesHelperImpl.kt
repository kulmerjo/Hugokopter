package com.kulmerjo.drone.hugocopter.helper.impl

import android.content.Context
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper
import java.util.*

class ResourcesHelperImpl : ResourcesHelper {


    override fun getDroneWifiSsid(context: Context): String {
        return getConfigValueAsString(context, R.raw.drone, ResourcesHelper.droneSsid)
    }

    override fun getConfigValueAsString(context: Context, configFile: Int, propertyName : String) : String {
        return getConfig(context, configFile, propertyName)
    }

    override fun getConfigValueAsInt(context: Context, configFile: Int, propertyName : String) : Int {
        return getConfig(context, configFile, propertyName).toInt()
    }

    private fun getConfig(context: Context, configFile: Int, propertyName : String) : String {
        val rawResource = context.resources.openRawResource(configFile)
        val property = Properties()
        property.load(rawResource)
        return property.getProperty(propertyName)
    }

}