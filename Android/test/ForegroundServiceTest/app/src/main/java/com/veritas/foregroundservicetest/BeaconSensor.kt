package com.veritas.foregroundservicetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.Nullable
import androidx.core.app.NotificationCompat

class BeaconSensor: Service() {
    @Nullable
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel =NotificationChannel("channel", "play!!", NotificationManager.IMPORTANCE_DEFAULT)

            val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.createNotificationChannel(channel)

            val notification = NotificationCompat.Builder(applicationContext, "channel")
            notification.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("현재 실행중인 앱이름")
                .setContentIntent(pendingIntent)
                .setContentText("")
            mNotificationManager.notify(1, notification.build())
            startForeground(1, notification.build())
        }
        return START_STICKY
    }
}