package com.veritas.TMapp.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.veritas.TMapp.databinding.FragmentOptionBinding
import com.veritas.TMapp.server.MyRisk
import com.veritas.TMapp.server.ServerSetting.fcmAPIS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OptionFragment : Fragment()
{
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        val binding = FragmentOptionBinding.inflate(inflater, container, false)
        var risk: Float

        fcmAPIS.getRisk().enqueue(object: Callback<MyRisk>{
            override fun onResponse(call: Call<MyRisk>, response: Response<MyRisk>) {
                if (response.code() == 200){
                    risk = response.body()?.risk!!.get(0)
                    Log.d("RISK", "$risk")
                }
            }

            override fun onFailure(call: Call<MyRisk>, t: Throwable) {
                Log.e("RISK", "서버와의 연결에 실패: ${t.message.toString()}")
            }
        })
        return binding.root
    }
}