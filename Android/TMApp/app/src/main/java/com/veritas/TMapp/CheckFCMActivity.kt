package com.veritas.TMapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.veritas.TMapp.databinding.ActivityCheckFcmBinding
import com.veritas.TMapp.recyclerview.FCMAdapter
import com.veritas.TMapp.recyclerview.FCMs
import com.veritas.TMapp.server.FCMInfo
import com.veritas.TMapp.server.FCMInfoList
import com.veritas.TMapp.server.ServerSetting.fcmAPIS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckFCMActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckFcmBinding
    var fcmInfos: List<FCMInfo>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        binding = ActivityCheckFcmBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)
        fcmAPIS.getFcmList().enqueue(object: Callback<FCMInfoList> {
            override fun onResponse(
                call: Call<FCMInfoList>,
                response: Response<FCMInfoList>
            ) {
                if (response.code() == 200) {
                    fcmInfos = response.body()?.fcmInfoList
                    Log.d("알림전송받음", "${fcmInfos}")
                } else {
                    Log.e("FCM", "response.code(): ${response.code()}")
                }
            }
            override fun onFailure(call: Call<FCMInfoList>, t: Throwable) {
                Log.e("FCM", t.message.toString())
                val dialog = AlertDialog.Builder(this@CheckFCMActivity)
                dialog.setTitle("에러")
                dialog.setMessage("호출실패했습니다.")
                dialog.show()
            }
        })
        //try {
            var id = fcmInfos?.get(0)?.id
            var uuid = fcmInfos?.get(0)?.uuid
            var date = fcmInfos?.get(0)?.date
            var time = fcmInfos?.get(0)?.time
            var title = fcmInfos?.get(0)?.title
            var body = fcmInfos?.get(0)?.body
            var risk = fcmInfos?.get(0)?.risk
            var contactDegree = fcmInfos?.get(0)?.contactDegree
            val fcmList = arrayListOf(
                FCMs(R.drawable.fcm, id, uuid, date, time, title, body, risk, contactDegree),
            )

/*            for (i in fcmInfos?.indices!!) {
                var id = fcmInfos?.get(i)?.id
                var uuid = fcmInfos?.get(i)?.uuid
                var date = fcmInfos?.get(i)?.date
                var time = fcmInfos?.get(i)?.time
                var title = fcmInfos?.get(i)?.title
                var body = fcmInfos?.get(i)?.body
                var risk = fcmInfos?.get(i)?.risk
                var contactDegree = fcmInfos?.get(i)?.contactDegree
                if (i != 0) {
                    fcmList.add(FCMs(R.drawable.fcm, id, uuid, date, time, title, body, risk, contactDegree))
                }
            }*/
            binding.rvFcm.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvFcm.setHasFixedSize(true)
            binding.rvFcm.adapter = FCMAdapter(fcmList)
        //}catch (e : Exception){
        //    Toast.makeText(this,"수신한 알림이 없습니다.",Toast.LENGTH_SHORT).show()
        //}
    }
}