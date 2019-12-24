package com.kulmerjo.drone.hugocopter.control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.drone.VideoReceiverService
import org.koin.android.ext.android.inject

class MainDroneControlActivity : AppCompatActivity() {

    private val connectionService : ConnectionService by inject()
    private val videoReceiverService: VideoReceiverService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drone_control)
        val imageView: ImageView = findViewById(R.id.imageView)
        videoReceiverService.setImageView(imageView)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoReceiverService.stopThread()
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
