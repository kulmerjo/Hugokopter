package com.kulmerjo.drone.hugocopter.connection.drone.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.ImageView
import com.kulmerjo.drone.hugocopter.connection.drone.VideoReceiverService
import java.io.BufferedInputStream
import java.io.DataInputStream
import java.lang.Exception
import java.net.Socket
import java.util.*

class VideoReceiverServiceTcp(private val ipAddress: String, private val port: Int) :Thread(), VideoReceiverService{

    private var imageView: ImageView? = null
    private var reader: DataInputStream? = null
    private var isStopped: Boolean = false
    private var socket: Socket? = null

    override fun reconnect(): Boolean{
        try {
            socket = Socket(ipAddress, port)
            reader = DataInputStream(BufferedInputStream(socket!!.getInputStream()))
        } catch (e: Exception){
            socket = null
            reader = null
            return false
        }
        return true
    }

    override fun run(){
        super.run()
        reconnect()
        receiveVideo()
        disconnect()
    }

    private fun disconnect(){
        socket?.close()
    }

    private fun showFrame(){
        if(reader == null) return
        if(reader!!.available() < 4) return
        val imageSize = reader?.readInt()
        val frame = readImage(imageSize!!)
        val bitMap: Bitmap = BitmapFactory.decodeByteArray(frame, 0, frame.size)
        imageView?.post{
            imageView?.setImageBitmap(Bitmap.createScaledBitmap(bitMap, imageView!!.width, imageView!!.height, false))
        }
    }

    private fun readImage(size: Int): ByteArray{
        while(reader!!.available() < size){
            sleep(100)
        }
        val byteArray = ByteArray(size)
        reader?.readFully(byteArray, 0, size)
        return byteArray
    }

    private fun receiveVideo(){
        while(!isStopped){
            showFrame()
            if(isConnected() == false || isClosed() == true){
                reconnect()
            }
        }
    }

    private fun isConnected(): Boolean?{
        return socket?.isConnected
    }

    private fun isClosed(): Boolean?{
        return socket?.isClosed
    }

    override fun stopThread(){
        isStopped = true
    }

    override fun setImageView(imageView: ImageView) {
        this.imageView = imageView
    }
}