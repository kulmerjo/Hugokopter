package com.kulmerjo.drone.hugocopter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import com.kulmerjo.drone.hugocopter.controll.MainDroneControlActivity
import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper
import com.kulmerjo.drone.hugocopter.notification.NotConnectedToDroneActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject


/**
 * @author hunteerq
 * Method provides logo and button which starts the control mode
 */
class MainActivity : AppCompatActivity() {

    private val connectionService : ResourcesHelper by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startAnimations()
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
        val port = connectionService.getConfigValueAsInt(this, R.raw.drone, ResourcesHelper.dronePortPropertyName)
        Log.println(Log.INFO, null, "XDDDDDDDDD $port")
        return if (true) {
            Intent(this, MainDroneControlActivity::class.java)
        } else {
            Intent(this, NotConnectedToDroneActivity::class.java)
        }
    }

}
