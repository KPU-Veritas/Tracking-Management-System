package com.veritas.TMapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.veritas.TMapp.databinding.ActivityCheckFcmBinding
import com.veritas.TMapp.recyclerview.FCMAdapter
import com.veritas.TMapp.recyclerview.FCMs

class CheckFCMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckFcmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        binding = ActivityCheckFcmBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        val fcmList = arrayListOf(
            FCMs(R.drawable.fcm, "알림 1"),
            FCMs(R.drawable.fcm, "알림 2"),
            FCMs(R.drawable.fcm, "알림 3"),
            FCMs(R.drawable.fcm, "알림 4"),
            FCMs(R.drawable.fcm, "알림 5"),
            FCMs(R.drawable.fcm, "알림 6"),
            FCMs(R.drawable.fcm, "알림 7"),
            FCMs(R.drawable.fcm, "알림 8"),
            FCMs(R.drawable.fcm, "알림 9"),
            FCMs(R.drawable.fcm, "알림 10"),
            FCMs(R.drawable.fcm, "알림 11"),
            FCMs(R.drawable.fcm, "알림 12"),
            FCMs(R.drawable.fcm, "알림 13"),
            FCMs(R.drawable.fcm, "알림 14"),
            FCMs(R.drawable.fcm, "알림 15"),
        )
        binding.rvFcm.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvFcm.setHasFixedSize(true)

        binding.rvFcm.adapter = FCMAdapter(fcmList)
    }
}