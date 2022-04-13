package com.veritas.TMapp.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(processedUuid != null){
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        }

        binding.buttonSignin.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val data = SigninModel(email,password)
            val dialog = AlertDialog.Builder(this@SigninActivity)

            signApi.requestSignin(data).enqueue(object: Callback<ResponseSigninModel> { // 로그인 시도 (서버에 요청)
                override fun onResponse(
                    call: Call<ResponseSigninModel>,
                    response: Response<ResponseSigninModel>
                ) {
                    user = response.body()!!

                    if(response.code() == 200){ // 응답 성공 시
                        Log.d("SIGNIN", "로그인 성공")
                        Log.d("SIGNIN", "token : ${user.token} \nuuid : ${user.uuid} \nusername : ${user.username}")
                        setUserInfo(user.uuid!!, user.token!!, user.username!!)
                        val intent = Intent(this@SigninActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{   // 서버 및 클라이언트 에러 시
                        Log.e("SIGNIN", "response.code(): ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseSigninModel>, t: Throwable) { // 서버와 응답 오류
                    Log.e("SIGNIN", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }
            })
        }

        binding.buttonSignup.setOnClickListener {   // 회원가입 버튼 클릭 시 실행
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}