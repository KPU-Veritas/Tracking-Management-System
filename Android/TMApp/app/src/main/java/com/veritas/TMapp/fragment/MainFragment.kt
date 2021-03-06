package com.veritas.TMapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.R
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
    private lateinit var beaconSensorManager: BeaconSensorManager
    private var flag: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        beaconSensorManager = BeaconSensorManager()
        activity?.let {
            beaconSensorManager.init(formatUUIDForBeacon(processedUuid!!), it.applicationContext)
        }
        // 비콘 센서 제어
        binding.btnStart.setOnClickListener {
            if (flag) {
                beaconSensorManager.sensorControl(true)
                beaconManager.startMonitoring(beaconScannerApplication.region)
                beaconManager.startRangingBeacons(beaconScannerApplication.region)
                binding.tvThread.text = "비콘 활성화 상태"
                binding.btnStart.setImageResource(R.drawable.btn_on)
                flag = false
            } else {
                beaconSensorManager.sensorControl(false)
                beaconManager.stopMonitoring(beaconScannerApplication.region)
                beaconManager.stopRangingBeacons(beaconScannerApplication.region)
                binding.tvThread.text = "비콘 중지 상태"
                binding.btnStart.setImageResource(R.drawable.btn_off)
                flag = true
                dbController.sendAllContacts(db)
            }
        }
        return binding.root
    }
}