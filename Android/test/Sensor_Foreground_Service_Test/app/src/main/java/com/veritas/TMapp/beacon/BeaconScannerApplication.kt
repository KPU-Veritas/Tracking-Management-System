package com.veritas.TMapp.beacon

import android.app.*
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Observer
import com.veritas.TMapp.MainActivity
import com.veritas.TMapp.database.AppDatabase
import com.veritas.TMapp.database.DBController
import org.altbeacon.beacon.*
import android.content.IntentFilter

import android.content.BroadcastReceiver
import android.widget.Toast


class BeaconScannerApplication: Application() {
    lateinit var region: Region
    var db: AppDatabase?= null
    private var dbController: DBController? = null
    private var scrOnReceiver: BroadcastReceiver? = null
    private var scrOffReceiver: BroadcastReceiver? = null
    private var scrOnFilter: IntentFilter? = null
    private var scrOffFilter: IntentFilter? = null
    override fun onCreate() {
        super.onCreate()

        db = AppDatabase.getInstance(this)
        dbController = DBController()

        var beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.clear()
        beaconManager.beaconParsers.add(
            BeaconParser().
            setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"))

        setupForegroundService()
        beaconManager.setEnableScheduledScanJobs(false)
        beaconManager.backgroundBetweenScanPeriod = 0
        beaconManager.backgroundScanPeriod = 1100

        region = Region("radius-uuid", null, null, null)

        val regionViewModel = BeaconManager.getInstanceForApplication(this).getRegionViewModel(region)
        regionViewModel.regionState.observeForever( centralMonitoringObserver)
        regionViewModel.rangedBeacons.observeForever( centralRangingObserver)
        scrOnReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, "SCREEN ON")
                Toast.makeText(applicationContext,"화면 ON",Toast.LENGTH_SHORT).show() }
        }
        scrOnFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        scrOffReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.d(TAG, "SCREEN OFF")
                Toast.makeText(applicationContext,"화면 OFF",Toast.LENGTH_SHORT).show()
            }
        }

        scrOffFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        registerReceiver(scrOnReceiver, scrOnFilter);
        registerReceiver(scrOffReceiver, scrOffFilter);
    }
    override fun onTerminate() {
        super.onTerminate()
        unregisterReceiver(scrOnReceiver)
        unregisterReceiver(scrOffReceiver)
    }
    // 포그라운드 서비스 환경설정
    private fun setupForegroundService() {
        val builder = Notification.Builder(this, "BeaconReferenceApp")
        builder.setSmallIcon(com.veritas.TMapp.R.drawable.beacon_background)
        builder.setContentTitle("Scanning for Beacons")
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(pendingIntent)
        val channel =  NotificationChannel("beacon-ref-notification-id",
            "My Notification Name", NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "My Notification Channel Description"
        val notificationManager =  getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
        builder.setChannelId(channel.id)
        BeaconManager.getInstanceForApplication(this).enableForegroundServiceScanning(builder.build(), 456)
    }

    private val centralMonitoringObserver = Observer<Int> { state ->
        if (state == MonitorNotifier.OUTSIDE) {
            Log.d(TAG, "outside beacon region: $region")
        }
        else {
            Log.d(TAG, "inside beacon region: $region")
            sendNotification()
        }
    }

    private val centralRangingObserver = Observer<Collection<Beacon>> { beacons ->
        Log.d(TAG, "Ranged: ${beacons.count()} beacons")
        for (beacon: Beacon in beacons) {
            if (beacon.distance <=3.0){
                dbController?.recordTime(db!!, beacon.id1.toString())
            }
            Log.d(TAG, "$beacon about ${beacon.distance} meters away")
        }
    }

    private fun sendNotification() {
        val builder = NotificationCompat.Builder(this, "beacon-ref-notification-id")
            .setContentTitle("Beacon Reference Application")// 알림 제목
            .setContentText("A beacon is nearby.")// 알림 본문 텍스트
            .setSmallIcon(com.veritas.TMapp.R.drawable.beacon_background) // 알림 아이콘
        val stackBuilder = TaskStackBuilder.create(this)
        stackBuilder.addNextIntent(Intent(this, MainActivity::class.java))
        val resultPendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_UPDATE_CURRENT + PendingIntent.FLAG_IMMUTABLE
        )
        builder.setContentIntent(resultPendingIntent)
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, builder.build())
    }

    companion object {
        const val TAG = "BeaconReference"
    }

}