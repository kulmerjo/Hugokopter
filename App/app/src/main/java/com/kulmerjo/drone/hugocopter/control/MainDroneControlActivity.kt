package com.kulmerjo.drone.hugocopter.control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import org.koin.android.ext.android.inject

class MainDroneControlActivity : AppCompatActivity() {

    private val connectionService : ConnectionService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drone_control)
    }

    fun onMoveClick(view: View) {
        val moveType = view.tag.toString()
        val moveSpeed = findViewById<SeekBar>(R.id.speed_bar).progress.toDouble()
        connectionService.sendControlDataToDrone(moveType, moveSpeed)
    }

    fun onDisconnectClick(view: View) {
        connectionService.sendDisconnectedInfoData()
    }
}
