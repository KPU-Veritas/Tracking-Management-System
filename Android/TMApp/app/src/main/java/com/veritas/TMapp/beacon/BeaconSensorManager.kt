package com.veritas.TMapp.beacon

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter

class BeaconSensorManager {
    var beacon: Beacon? = null
    private var beaconParser: BeaconParser? = null
    var beaconTransmitter: BeaconTransmitter? = null
    var handler: Handler? = null
    private var thread: Thread? = null
    var isThread = false

    fun init(uuid:String, context: Context){
        Log.d(TAG,"비콘 센서 초기화")
        beacon = Beacon.Builder()
            .setId1(uuid)
            .setId2("1")
            .setId3("2")
            .setManufacturer(76)
            .setTxPower(-59)
            .setDataFields(listOf(0L))
            .build()

        beaconParser = BeaconParser().setBeaconLayout(
            "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"
        )
        beaconTransmitter = BeaconTransmitter(context, beaconParser)

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                beaconTransmitter!!.startAdvertising(beacon, object : AdvertiseCallback() {
                    override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                        super.onStartSuccess(settingsInEffect)
                        Log.d(TAG, "스캔 시작")
                    }

                    override fun onStartFailure(errorCode: Int) {
                        super.onStartFailure(errorCode)
                        Log.e(TAG, "스캔 시작 에러: $errorCode")
                    }
                })
            }
        }
    }

    fun sensorControl(flag:Boolean){
        if (flag){
            isThread = true
            thread = object : Thread() {
                override fun run() {
                    if (isThread){
                        try{
                            handler?.sendEmptyMessage(0)
                            Log.d(TAG,"비콘 센서 기능 핸들러 실행")
                        }catch (e:InterruptedException){
                            Log.e(TAG,"비콘 센서 기능 핸들러 실행 오류: ${e.printStackTrace()}")
                        }
                    }
                }
            }
            (thread as Thread).start()
            Log.d(TAG, "비콘 센서 기능이 실행됬습니다.")
        }else{
            beaconTransmitter?.stopAdvertising()
            isThread = false
            Log.d(TAG, "비콘 센서 기능이 중지됬습니다.")
        }
    }


    companion object{
        const val TAG = "비콘 센서"
    }

}