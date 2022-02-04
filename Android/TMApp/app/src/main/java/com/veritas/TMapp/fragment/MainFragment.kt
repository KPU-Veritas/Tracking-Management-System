package com.veritas.TMapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.beacon.BeaconSensorManager
import com.veritas.TMapp.databinding.FragmentMainBinding
class MainFragment : Fragment()
{
    private lateinit var beaconSensorManager: BeaconSensorManager
    private val uuid = "19980930-0010-2307-2441-000000000005"
    private var flag : Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentMainBinding.inflate(inflater, container, false)

        beaconSensorManager = BeaconSensorManager()
        activity?.let { beaconSensorManager.init(uuid, it.applicationContext) }
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
        return binding.root
    }
}