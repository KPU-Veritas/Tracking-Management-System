package com.veritas.TMapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.beacon.BeaconScannerApplication
import com.veritas.TMapp.beacon.BeaconSensorManager
import com.veritas.TMapp.databinding.FragmentMainBinding
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.RegionViewModel

class MainFragment(
    uuid: String,
    beaconScannerApplication: BeaconScannerApplication,
    beaconManager: BeaconManager,) : Fragment() {
    private lateinit var beaconSensorManager: BeaconSensorManager
    private var flag : Boolean = true
    private var scanflag : Boolean = true
    var UUID = uuid
    var beaconScannerApplication = beaconScannerApplication
    var beaconManager= beaconManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        beaconSensorManager = BeaconSensorManager()
        activity?.let { beaconSensorManager.init(formatingUUID(UUID), it.applicationContext) }

        binding.btnStart.setOnClickListener {
            if (flag) {
                beaconSensorManager.scannerChange(true)
                binding.tvThread.text = "센서 작동 중"
                flag = false
            }
            else{
                beaconSensorManager.scannerChange(false)
                binding.tvThread.text = "센서 중지 상태"
                flag = true
            }
        }
        binding.btnScan.setOnClickListener {
            if (scanflag) {
                beaconManager.startMonitoring(beaconScannerApplication.region)
                beaconManager.startRangingBeacons(beaconScannerApplication.region)
                binding.tvScan.text = "스캔 작동 중"
                scanflag = false
            }
            else{
                beaconManager.stopMonitoring(beaconScannerApplication.region)
                beaconManager.stopRangingBeacons(beaconScannerApplication.region)
                binding.tvScan.text = "스캔 중지 상태"
                scanflag = true
            }
        }
        return binding.root
    }

    fun formatingUUID(uuid:String) : String{
        return "${uuid.substring(0,8)}-${uuid.substring(8,12)}-${uuid.substring(12,16)}-${uuid.substring(16,20)}-${uuid.substring(20)}"
    }
}