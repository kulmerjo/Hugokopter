package com.kulmerjo.drone.hugocopter.control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.drone.VideoReceiverService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainDroneControlActivity : AppCompatActivity() {

    private val connectionService : ConnectionService by inject()

    private val videoReceiverService: VideoReceiverService by inject()

    private val imageView: ImageView = findViewById(R.id.imageView)

    private val speed  = 5.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drone_control)
        videoReceiverService.setImageView(imageView)
        setButtonOnClickListeners()

    }

    private fun setButtonOnClickListeners() {
        button_start.setOnClickListener { onDisconnectClick() }
    }

    override fun onDestroy() {
        super.onDestroy()
        videoReceiverService.stopThread()
    }

    fun onMoveClick(view: View) {
        val moveType = view.tag.toString()
        connectionService.sendControlDataToDrone(moveType, speed)
    }

    private fun onDisconnectClick() {
        connectionService.sendDisconnectedInfoData()
    }
}
