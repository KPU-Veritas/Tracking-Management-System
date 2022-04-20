package com.veritas.TMapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.veritas.TMapp.database.DBController
import com.veritas.TMapp.databinding.ActivitySendInfectionInfoBinding
import com.veritas.TMapp.server.PostInfectModel
import com.veritas.TMapp.server.ResponseMsg
import com.veritas.TMapp.server.ServerSetting.infectAPIS
import com.veritas.TMapp.server.ServerSetting.processedUuid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class SendInfectionInfoActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySendInfectionInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        binding = ActivitySendInfectionInfoBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        binding.btnSend.setOnClickListener {
            val judgmentDate:String = binding.etJudgmentDate.text.toString()
            val estimatedDate:String = binding.etEstimatedDate.text.toString()
            val detailSituation:String = binding.etDetailSituation.text.toString()
            val infectInfo = PostInfectModel(processedUuid,judgmentDate,estimatedDate,detailSituation)

            try{
                infectAPIS.postInfect(infectInfo).enqueue(object : Callback<ResponseMsg> {
                    override fun onResponse(
                        call: Call<ResponseMsg>,
                        response: Response<ResponseMsg>
                    ) {
                        if(response.code() == 200){
                            Log.d(DBController.TAG, "전송 성공")
                        }
                        else{
                            Log.d(DBController.TAG, "전송 실패")
                        }
                    }

                    override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                        Log.d("InfectPost", t.message.toString())
                    }

                })
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }



    }
}