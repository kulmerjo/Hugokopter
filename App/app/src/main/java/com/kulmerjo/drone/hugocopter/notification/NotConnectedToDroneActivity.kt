package com.kulmerjo.drone.hugocopter.notification

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.connection.ConnectionService
import com.kulmerjo.drone.hugocopter.controll.MainDroneControlActivity
import com.kulmerjo.drone.hugocopter.helper.ResourcesHelper
import kotlinx.android.synthetic.main.activity_not_connected_to_drone.*
import org.koin.android.ext.android.inject

class NotConnectedToDroneActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_not_connected_to_drone)
    }

    fun onClick(view: View) {
        if (true) {
            val droneControlIntent = Intent(this, MainDroneControlActivity::class.java)
            startActivity(droneControlIntent)
        } else {
            still_no_connection_text.visibility = View.VISIBLE
        }
    }

}

