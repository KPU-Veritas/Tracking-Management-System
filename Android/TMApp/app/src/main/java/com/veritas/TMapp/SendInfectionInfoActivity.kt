package com.veritas.TMapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import com.veritas.TMapp.databinding.ActivitySendInfectionInfoBinding
import com.veritas.TMapp.server.PostInfectModel
import com.veritas.TMapp.server.ResponseMsg
import com.veritas.TMapp.server.ServerSetting.infectAPIS
import com.veritas.TMapp.server.ServerSetting.processedUuid
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.util.*

class SendInfectionInfoActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySendInfectionInfoBinding
    private lateinit var datePickerDialog: DatePickerDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 자동 생성된 뷰 바인딩 클래스에서의 inflate라는 메서드를 활용하여 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        binding = ActivitySendInfectionInfoBinding.inflate(layoutInflater)
        // getRoot 메서드로 레이아웃 내부의 최상위 위치 뷰의 인스턴스를 활용하여 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        binding.tvJudgmentDate.setOnClickListener{
            val calendar:Calendar = Calendar.getInstance()
            val pYear = calendar.get(Calendar.YEAR)
            val pMonth = calendar.get(Calendar.MONTH)
            val pDay = calendar.get(Calendar.DAY_OF_MONTH)
            datePickerDialog = DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
                    val date:String = "%d-%02d-%02d".format(year%100, month+1,day)
                    binding.tvJudgmentDate.text = date
                }
            },pYear,pMonth,pDay)
            datePickerDialog.show()
        }
        binding.tvEstimatedDate.setOnClickListener {
            val calendar:Calendar = Calendar.getInstance()
            val pYear = calendar.get(Calendar.YEAR)
            val pMonth = calendar.get(Calendar.MONTH)
            val pDay = calendar.get(Calendar.DAY_OF_MONTH)
            datePickerDialog = DatePickerDialog(this, object: DatePickerDialog.OnDateSetListener{
                override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
                    val date:String = "%d-%02d-%02d".format(year%100, month+1,day)
                    binding.tvEstimatedDate.text = date
                }
            },pYear,pMonth,pDay)
            datePickerDialog.show()
        }
        binding.btnSend.setOnClickListener {
            val judgmentDate:String = binding.tvJudgmentDate.text.toString()
            val estimatedDate:String = binding.tvEstimatedDate.text.toString()
            val detailSituation:String = binding.etDetailSituation.text.toString()
            val infectInfo = PostInfectModel(processedUuid,judgmentDate,estimatedDate,detailSituation)

            try{
                infectAPIS.postInfect(infectInfo).enqueue(object : Callback<ResponseMsg> {
                    override fun onResponse(
                        call: Call<ResponseMsg>,
                        response: Response<ResponseMsg>
                    ) {
                        if(response.code() == 200){
                            Log.d(TAG, "전송 성공")
                        }
                        else{
                            Log.d(TAG, "전송 실패")
                        }
                    }

                    override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                        Log.e(TAG, "서버와의 연결에 실패: ${t.message.toString()}")
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
    companion object{
        const val TAG = "감염 정보"
    }
}
