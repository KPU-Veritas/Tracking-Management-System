package com.veritas.TMapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.veritas.TMapp.databinding.ActivityCheckFcmBinding
import com.veritas.TMapp.recyclerview.FCMAdapter
import com.veritas.TMapp.recyclerview.FCMs
import java.io.File

class CheckFCMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckFcmBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        binding = ActivityCheckFcmBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        var dirPath = "${filesDir.absolutePath}/FCM"
        var fcmFiles = File(dirPath).listFiles()
        try {
            var name = fcmFiles[0].name.substring(0, fcmFiles[0].name.length - 4)
            val fcmList = arrayListOf(
                FCMs(R.drawable.fcm, name),
            )
            for (i in fcmFiles.indices) {
                name = fcmFiles[i].name.substring(0, fcmFiles[i].name.length - 4)
                if (i != 0) {
                    fcmList.add(FCMs(R.drawable.fcm, name))
                }
            }
            binding.rvFcm.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvFcm.setHasFixedSize(true)
            binding.rvFcm.adapter = FCMAdapter(fcmList)
        }catch (e : Exception){
            Toast.makeText(this,"수신한 알림이 없습니다.",Toast.LENGTH_SHORT).show()
        }
    }
}