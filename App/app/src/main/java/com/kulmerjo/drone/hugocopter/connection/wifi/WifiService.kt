package com.kulmerjo.drone.hugocopter.connection.wifi

import android.content.Context
import android.net.wifi.WifiManager
import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper

class WifiService (private val resourcesHelper : ResourcesHelper) {


    fun isWifiCorrect (context: Context) : Boolean {
        return getConnectedWifiSsid(context).equals(
            resourcesHelper.getDroneWifiSsid(context)
        )
    }

    private fun getConnectedWifiSsid(context:  Context): String? {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.connectionInfo.ssid
    }
}
