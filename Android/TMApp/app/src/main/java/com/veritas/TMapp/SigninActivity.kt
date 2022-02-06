package com.veritas.TMapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.veritas.TMapp.databinding.ActivitySigninBinding
import com.veritas.TMapp.server.APIS
import com.veritas.TMapp.server.ResponseSigninModel
import com.veritas.TMapp.server.SigninModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SigninActivity : AppCompatActivity() {
    private var signinBinding: ActivitySigninBinding? = null
    private val binding get() = signinBinding!!
    var user: ResponseSigninModel? = null
    private var api = APIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signinBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignin.setOnClickListener {
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            val data = SigninModel(email,password)
            val dialog = AlertDialog.Builder(this@SigninActivity)

            api.requestSingin(data).enqueue(object: Callback<ResponseSigninModel> {
                override fun onResponse(
                    call: Call<ResponseSigninModel>,
                    response: Response<ResponseSigninModel>
                ) {
                    user = response.body()

                    if(response.code() == 200){
                        Log.d("SIGNIN", "로그인 성공")
                        Log.d("SIGNIN", "token : ${user?.token} \nuuid : ${user?.uuid} \nusername : ${user?.username}")
                        val intent = Intent(this@SigninActivity,MainActivity::class.java)
                        intent.putExtra("user", user)
                        startActivity(intent)
                    }
                    else{
                        Log.d("SIGNIN", "response.code(): ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ResponseSigninModel>, t: Throwable) {
                    Log.e("SIGNIN", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }
            })
        }

        binding.buttonSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}