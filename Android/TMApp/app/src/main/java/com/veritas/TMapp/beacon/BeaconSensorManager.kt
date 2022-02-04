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
        beacon = Beacon.Builder()
            .setId1(uuid)
            .setId2("1")
            .setId3("2")
            .setManufacturer(0x0118)
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
                        Log.d(TAG, "onStartSuccess: ")
                    }

                    override fun onStartFailure(errorCode: Int) {
                        super.onStartFailure(errorCode)
                        Log.d(TAG, "onStartFailure: $errorCode")
                    }
                })
            }
        }
    }

    fun scannerChange(flag:Boolean){
        if (flag){
            isThread = true
            thread = object : Thread() {
                override fun run() {
                    if (isThread){
                        try{
                            handler?.sendEmptyMessage(0)
                            Log.d(TAG,"Beacon 핸들러 실행")
                        }catch (e:InterruptedException){
                            Log.e(TAG,e.printStackTrace().toString())
                        }
                    }
                }
            }
            (thread as Thread).start()
            Log.d(TAG, "Beacon Scanner Started")
        }else{
            beaconTransmitter?.stopAdvertising()
            isThread = false
            Log.d(TAG, "Beacon Scanner Stoped")
        }
    }


    companion object{
        const val TAG = "BeaconScanner"
    }

}