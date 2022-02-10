package com.veritas.TMapp.sign

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.veritas.TMapp.databinding.ActivitySignupBinding
import com.veritas.TMapp.server.SignAPIS
import com.veritas.TMapp.server.ResponseSignupModel
import com.veritas.TMapp.server.SignupModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private var signupBinding: ActivitySignupBinding? = null
    private val binding get() = signupBinding!!
    var user: ResponseSignupModel? = null
    private var api = SignAPIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearchAddress.setOnClickListener {
            val intent = Intent(this@SignupActivity, FindAddressActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }
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

            api.requestSignup(data).enqueue(object: Callback<ResponseSignupModel>{
                override fun onResponse(
                    call: Call<ResponseSignupModel>,
                    response: Response<ResponseSignupModel>
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

                override fun onFailure(call: Call<ResponseSignupModel>, t: Throwable) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == RESULT_OK){
                val data = intent.extras?.getString("data")
                if(data!=null)
                    binding.textViewSimpleAddress.text = data
            }
        }
    }

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 10000
    }
}