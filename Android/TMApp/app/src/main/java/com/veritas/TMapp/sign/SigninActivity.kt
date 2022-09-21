package com.veritas.TMapp.sign

import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.veritas.TMapp.MainActivity
import com.veritas.TMapp.databinding.ActivitySigninBinding
import com.veritas.TMapp.server.ResponseSigninModel
import com.veritas.TMapp.server.ServerSetting.processedUuid
import com.veritas.TMapp.server.ServerSetting.setUserInfo
import com.veritas.TMapp.server.ServerSetting.signApi
import com.veritas.TMapp.server.SigninModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {    // 로그인 Activity
    private var signinBinding: ActivitySigninBinding? = null
    private val binding get() = signinBinding!!
    lateinit var user: ResponseSigninModel
    private var bluetoothAdapter : BluetoothAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(bluetoothAdapter!=null){
            // Device doesn't support Bluetooth
            if(bluetoothAdapter?.isEnabled==false){
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }else{
            Log.d("블루투스","기기가 블루투스를 지원하지 않습니다.")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("애플리케이션 이용 불가")
                .setMessage("사용중인 기기가 블루투스를 지원하지 않습니다.")
                .setPositiveButton("확인") { dialogInterface : DialogInterface, i : Int -> }
                .setOnDismissListener { finish() }
            builder.show()
        }

        if(processedUuid != null){
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        binding.buttonSignin.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val data = SigninModel(email,password)

            signApi.requestSignin(data).enqueue(object: Callback<ResponseSigninModel> { // 로그인 시도 (서버에 요청)
                override fun onResponse(
                    call: Call<ResponseSigninModel>,
                    response: Response<ResponseSigninModel>
                ) {
                    if(response.code() == 200){ // 로그인 성공 시
                        user = response.body()!!

                        Log.d(TAG, "로그인 성공")
                        Log.d(TAG, "token : ${user.token} \nuuid : ${user.uuid} \nusername : ${user.username}")
                        setUserInfo(user.uuid, user.token, user.username)
                        val intent = Intent(this@SigninActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{   // 로그인 실패 시
                        Log.d(TAG, "로그인 실패")
                        Log.e(TAG, "response.code(): ${response.code()}")
                        Toast.makeText(applicationContext, "로그인에 실패했습니다.\n아이디 혹은 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseSigninModel>, t: Throwable) { // 서버와 응답 오류
                    Log.e(TAG, "서버와의 연결에 실패: ${t.message.toString()}")
                }
            })
        }

        binding.buttonSignup.setOnClickListener {   // 회원가입 버튼 클릭 시 실행
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    companion object {
        const val TAG = "로그인"
    }
}