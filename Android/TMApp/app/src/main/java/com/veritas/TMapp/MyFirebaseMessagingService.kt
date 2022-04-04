package com.veritas.TMapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService(){
    private val TAG: String = this.javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage)
    {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.notification != null)
        {
            sendNotification(remoteMessage.notification?.title, remoteMessage.notification!!.body!!)
            if (remoteMessage.notification?.title != null) {
                Log.d("Message", remoteMessage.notification?.title!! + remoteMessage.notification!!.body!!)
                writeFile(remoteMessage.notification?.title!!, remoteMessage.notification!!.body!!)
            }
        }
    }
    override fun onNewToken(token: String)
    {
        Log.d(TAG, "Refreshed token : $token")
        super.onNewToken(token)
    }
    // 받은 알림을 기기에 표시하는 메서드
    private fun sendNotification(title: String?, body: String)
    {
        Log.d("확인", "하는중")

        val intent = Intent(this, CheckFCMActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = "my_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        Log.d("Content", body)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
    fun writeFile(title : String, content : String) {
        Log.d("확인", "안쪽")
        var dirPath = "${filesDir.absolutePath}/FCM"
        var myDir = File("$dirPath")//생성할 디렉토리의 경로를 설정한다.
        myDir.mkdir()
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a H시 m분", Locale.KOREA)
        val formatted = current.format(formatter)
        val file = File("$dirPath/$formatted $title.txt")
        val fos = FileOutputStream(file)
        var str = content
        fos.write(str.toByteArray())
        fos.close()
    }
}