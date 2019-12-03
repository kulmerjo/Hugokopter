package com.kulmerjo.drone.hugocopter

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.drone.async.models.DroneControlData
import com.kulmerjo.drone.hugocopter.connection.drone.async.tcp.impl.AsyncTcpClientImpl
import com.kulmerjo.drone.hugocopter.connection.wifi.WifiService
import com.kulmerjo.drone.hugocopter.control.MainDroneControlActivity
import com.kulmerjo.drone.hugocopter.notification.NotConnectedToDroneActivity
import com.kulmerjo.drone.hugocopter.permission.PermissionHelper
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


/**
 * @author hunteerq
 * Method provides logo and button which starts the control mode */
class MainActivity : AppCompatActivity() {

    private val connectionService : ConnectionService by inject()

    private val wifiService : WifiService by inject()

    private val permissionHelper : PermissionHelper by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissionHelper.checkAllStartPermissions(this)
        startTcpConnection()
        startAnimations()
    }

    private fun startTcpConnection() {
        val serverTcp = Intent(this, AsyncTcpClientImpl::class.java)
        startForegroundService(serverTcp)
    }

    /**
     * Method starts all animations for button and logo
     */
    private fun startAnimations() {
        val duration = 700L
        val slideInLeftAnimation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        setAnimation(duration, slideInLeftAnimation)
    }

    private fun setAnimation(duration: Long, animation: Animation) {
        animation.duration = duration
        logo_full.startAnimation(animation)
        material_unelevated_button.startAnimation(animation)
    }

    /**
     * Start button listener
     */
    fun onClick(view: View) {
        val nextIntent = intentDependedOnConnection()
        startActivity(nextIntent)
    }

    private fun intentDependedOnConnection(): Intent {
        return if (/*wifiService.isWifiCorrect(applicationContext) && */connectionService.isConnectedToDrone()) {
            val droneData = DroneControlData("Dupa", "Dupa", 2222.0)
            connectionService.sendDataToDrone(droneData)
            Intent(this, MainDroneControlActivity::class.java) }
        else {
            Intent(this, NotConnectedToDroneActivity::class.java)
        }
    }

}
