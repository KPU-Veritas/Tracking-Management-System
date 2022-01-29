package com.veritas.TMapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.veritas.TMapp.databinding.ActivitySignupBinding
import com.veritas.TMapp.server.APIS
import com.veritas.TMapp.server.SignupModel
import com.veritas.TMapp.server.responseSignupModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private var signupBinding: ActivitySignupBinding? = null
    private val binding get() = signupBinding!!
    var user: responseSignupModel? = null
    private var api = APIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignup.setOnClickListener {
            val dialog = AlertDialog.Builder(this@SignupActivity)
            val password = binding.editTextPassword.text.toString()
            val checkPW = binding.editTextCheckPassword.text.toString()
            if(password != checkPW){
                Log.e("SIGNUP", "비밀번호가 일치하지 않습니다.")
                dialog.setTitle("비밀번호 오류")
                dialog.setMessage("비밀번호가 일치하지 않습니다.")
                dialog.setPositiveButton("확인", null)
                dialog.show()

            }
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmail.text.toString()
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val simpleAddress = binding.textViewSimpleAddress.text.toString()
            val detailAddress = binding.editTextDetailAddress.text.toString()

            val data = SignupModel(username,email,password,phoneNumber, simpleAddress, detailAddress)

            api.requestSingup(data).enqueue(object: Callback<responseSignupModel>{
                override fun onResponse(
                    call: Call<responseSignupModel>,
                    response: Response<responseSignupModel>
                ) {
                    user = response.body()

                    if(response.code() == 200){
                        Log.d("SIGNIN", "회원가입 성공")
                        Log.d("SIGNIN", "email : ${user?.email}")
                        dialog.setTitle("회원가입 완료")
                        dialog.setMessage("${username}님 회원가입 완료되었습니다.")
                        dialog.setPositiveButton("확인"){ _: DialogInterface, _: Int ->
                            finish()
                        }
                        dialog.show()
                    }
                    else{
                        Log.d("SIGNIN", "response.code(): ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<responseSignupModel>, t: Throwable) {
                    Log.e("SIGNUP", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }
            })
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}