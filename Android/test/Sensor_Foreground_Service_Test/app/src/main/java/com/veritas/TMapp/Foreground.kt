package com.veritas.TMapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.veritas.TMapp.beacon.BeaconSensorManager
import com.veritas.TMapp.server.ServerSetting
import kotlin.concurrent.thread
class Foreground : Service() {
    val CHANNEL_ID = "FG5153"
    val NOTI_ID = 153
    private lateinit var beaconSensorManager: BeaconSensorManager
    fun createNotification(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val serviceChannel = NotificationChannel(CHANNEL_ID, "Foreground", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()
        startForeground(NOTI_ID, notification)
        beaconSensorManager = BeaconSensorManager()
        beaconSensorManager.init(ServerSetting.formatUUIDForBeacon(ServerSetting.processedUuid), applicationContext)
        beaconSensorManager.scannerChange(true)
        //runBackground()
        return super.onStartCommand(intent, flags, startId)
    }
    fun runBackground(){
        thread(start=true){
            for (i in 0..100){
                Thread.sleep(1000)
                Log.d("서비스", "COUNT===>$i")
            }
        }
    }
    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}