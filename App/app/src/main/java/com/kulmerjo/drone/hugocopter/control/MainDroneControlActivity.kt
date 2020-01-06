package com.kulmerjo.drone.hugocopter.control

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.google.android.material.button.MaterialButton
import com.kulmerjo.drone.hugocopter.R
import com.kulmerjo.drone.hugocopter.connection.drone.ConnectionService
import com.kulmerjo.drone.hugocopter.connection.drone.VideoReceiverService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_drone_control.*
import org.koin.android.ext.android.inject

class MainDroneControlActivity : AppCompatActivity() {

    private val connectionService : ConnectionService by inject()

    private val videoReceiverService: VideoReceiverService by inject()

    private val speed  = 5.0

    private val stableMoveType = "stable"

    private fun getButtonsList(): List<MaterialButton> {
        return listOf(
            button_move_forward,
            button_move_backward,
            button_move_left,
            button_move_right
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_drone_control)
        videoReceiverService.setImageView(findViewById(R.id.imageView))
        setButtonOnClickListeners()
        setButtonsOnClickReleaseListeners()
    }

    private fun setButtonsOnClickReleaseListeners() {
        getButtonsList().forEach { button ->
            button.setOnTouchListener {
                    view, event -> testIfButtonIsReleased(view, event)
            }
        }
    }

    private fun testIfButtonIsReleased(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_BUTTON_RELEASE) {
            onButtonRelease(v)
        }
        return true
    }

    private fun onButtonRelease(view: View) {
        connectionService.sendControlDataToDrone(stableMoveType, 0.0)
    }

    private fun setButtonOnClickListeners() {
        button_exit.setOnClickListener { onDisconnectClick() }
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
