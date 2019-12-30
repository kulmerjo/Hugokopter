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
    private val packageHeaderSize: Int = 4
    private val imageReadRetryDelay: Long = 100
    private val imageReadMaxRetryAttempts: Int = 50

    override fun reconnect(): Boolean{
        try {
            disconnect()
            socket = Socket(ipAddress, port)
            reader = DataInputStream(BufferedInputStream(socket!!.getInputStream()))
        } catch (e: Exception){
            socket = null
            reader = null
            return false
        }
        return true
    }

    override fun run() {
        super.run()
        reconnect()
        receiveVideo()
        disconnect()
    }

    private fun disconnect() {
        socket?.close()
    }

    private fun showFrame() {
        try {
            val imageSize = readImageSize()
            val image = readImage(imageSize)
            val bitMap: Bitmap? = convertByteArrayToBitmap(image)
            updateImageView(bitMap)
        }catch (e: Exception){
            reconnect()
        }
    }

    private fun updateImageView(bitMap: Bitmap?){
        if (bitMap != null){
            imageView?.post{
                imageView?.setImageBitmap(Bitmap.createScaledBitmap(bitMap, imageView!!.width, imageView!!.height, false))
            }
        }
    }

    private fun convertByteArrayToBitmap(image: ByteArray?): Bitmap?{
        return if (image != null) BitmapFactory.decodeByteArray(image, 0, image.size) else null
    }

    private fun readImageSize(): Int?{
        return if (reader != null || reader!!.available() >= packageHeaderSize) reader?.readInt() else null
    }

    private fun readImage(size: Int?): ByteArray?{
        if (size == null || !isImageAvailable(size)){
            return null
        }
        val byteArray = ByteArray(size)
        reader?.readFully(byteArray, 0, size)
        return if (byteArray.isNotEmpty()) byteArray else null
    }

    private fun isImageAvailable(size: Int): Boolean{
        var numberOfAttempts = 0
        while (reader!!.available() < size) {
            numberOfAttempts++
            if (numberOfAttempts > imageReadMaxRetryAttempts) return false
            sleep(imageReadRetryDelay)
        }
        return true
    }

    private fun receiveVideo() {
        while (!isStopped) {
            showFrame()
            if (isConnected() == false || isClosed() == true || socket == null) {
                reconnect()
            }
        }
    }

    private fun isConnected(): Boolean? {
        return socket?.isConnected
    }

    private fun isClosed(): Boolean? {
        return socket?.isClosed
    }


    override fun stopThread() {
        isStopped = true
    }

    override fun setImageView(imageView: ImageView) {
        this.imageView = imageView
    }
}