package com.veritas.TMapp.sign

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.veritas.TMapp.databinding.ActivitySignupBinding
import com.veritas.TMapp.server.ResponseMsg
import com.veritas.TMapp.server.ServerSetting.signApi
import com.veritas.TMapp.server.SignupModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {    // 회원가입 Activity
    private var signupBinding: ActivitySignupBinding? = null
    private val binding get() = signupBinding!!
    var responseMsg: ResponseMsg? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSearchAddress.setOnClickListener {    // 주소 찾기 버튼 클릭 시
            val intent = Intent(this@SignupActivity, FindAddressActivity::class.java)
            startActivityForResult(intent, SEARCH_ADDRESS_ACTIVITY)
        }
        binding.buttonSignup.setOnClickListener {   // 회원가입 버튼 클릭 시
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

            signApi.requestSignup(data).enqueue(object: Callback<ResponseMsg>{  // 서버에 회원 정보 등록 요청
                override fun onResponse(
                    call: Call<ResponseMsg>,
                    response: Response<ResponseMsg>
                ) {
                    responseMsg = response.body()

                    if(response.code() == 200){
                        Log.d("SIGNIN", "회원가입 성공")
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

                override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                    Log.e("SIGNUP", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }
            })
        }

        binding.buttonCancel.setOnClickListener {   // 회원가입 취소 시
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {   // FindAddressActivity에서 주소 선택시 address 데이터 반환
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == Activity.RESULT_OK){
                val address = data!!.getStringExtra("address")
                Log.d("주소 데이터", address.toString())
                if(data!=null)
                    binding.textViewSimpleAddress.text = address
            }
        }
    }

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 10000
    }
}