package com.veritas.TMapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.veritas.TMapp.Foreground
import com.veritas.TMapp.beacon.BeaconScannerApplication
import com.veritas.TMapp.beacon.BeaconSensorManager
import com.veritas.TMapp.database.AppDatabase
import com.veritas.TMapp.database.DBController
import com.veritas.TMapp.databinding.FragmentMainBinding
import com.veritas.TMapp.server.ServerSetting.formatUUIDForBeacon
import com.veritas.TMapp.server.ServerSetting.processedUuid
import org.altbeacon.beacon.BeaconManager

class MainFragment(
    private var beaconScannerApplication: BeaconScannerApplication,
    private var beaconManager: BeaconManager,
    private var db:AppDatabase,
    private var dbController: DBController) : Fragment() {
    //private lateinit var beaconSensorManager: BeaconSensorManager
    private var flag : Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        //beaconSensorManager = BeaconSensorManager()
        //activity?.let { beaconSensorManager.init(formatUUIDForBeacon(processedUuid), it.applicationContext) }

        binding.btnStart.setOnClickListener {
            if (flag) {
                view?.let { it1 -> serviceStart(it1) }
                //beaconSensorManager.scannerChange(true)
                beaconManager.startMonitoring(beaconScannerApplication.region)
                beaconManager.startRangingBeacons(beaconScannerApplication.region)
                binding.tvThread.text = "비콘 활성화 상태"
                flag = false
            }
            else{
                view?.let { it1 -> serviceStop(it1) }
                //beaconSensorManager.scannerChange(false)
                beaconManager.stopMonitoring(beaconScannerApplication.region)
                beaconManager.stopRangingBeacons(beaconScannerApplication.region)
                binding.tvThread.text = "비콘 중지 상태"
                flag = true
                dbController.sendAllContacts(db)
            }
        }

        return binding.root
    }
    fun serviceStart(view: View){
        val intent = Intent(activity, Foreground::class.java)
        activity?.let { ContextCompat.startForegroundService(it, intent) }
    }
    fun serviceStop(view:View){
        val intent = Intent(activity, Foreground::class.java)
        activity?.stopService(intent)
    }


}