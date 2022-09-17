package com.veritas.TMapp.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.databinding.FragmentRiskBinding
import com.veritas.TMapp.server.MyRisk
import com.veritas.TMapp.server.ServerSetting.fcmAPIS
import com.veritas.TMapp.server.ServerSetting.username
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiskFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentRiskBinding.inflate(inflater, container, false)
        var risk = 0.0F

        fcmAPIS.getRisk().enqueue(object: Callback<MyRisk>{
            override fun onResponse(call: Call<MyRisk>, response: Response<MyRisk>) {
                if (response.code() == 200){
                    risk = response.body()?.risk!![0]
                    Log.d("RISK", "$risk")
                }
            }

            override fun onFailure(call: Call<MyRisk>, t: Throwable) {
                Log.e("RISK", "서버와의 연결에 실패: ${t.message.toString()}")
            }
        })

        binding.tvUsername.text = username
        binding.tvRisk.text = risk.toString()
        if (0 <= risk && risk < 26.0){
            binding.tvRisk.setTextColor(Color.parseColor("#9DD84B"))
        }else if(26.0 <= risk && risk < 51.0){
            binding.tvRisk.setTextColor(Color.parseColor("#FFD400"))
        }else if(51.0 <= risk && risk < 76.0){
            binding.tvRisk.setTextColor(Color.parseColor("#FF7F00"))
        }else{
            binding.tvRisk.setTextColor(Color.parseColor("#FF0000"))
        }

        return binding.root
    }
}