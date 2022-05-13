package com.veritas.TMapp.sign

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
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

        binding.editTextPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val passwd = binding.editTextPassword.text.toString()
                if (passwd.length < 8){
                    binding.textViewPasswordGuide.text = "비밀번호는 최소 8자리 이상이어야 합니다."
                    binding.textViewPasswordGuide.setTextColor(Color.RED)
                }
                else {
                    binding.textViewPasswordGuide.text = "사용가능한 비밀번호 입니다."
                    binding.textViewPasswordGuide.setTextColor(Color.BLUE)
                }
            }
        })

        binding.editTextCheckPassword.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val passwd = binding.editTextPassword.text.toString()
                val checkPasswd = binding.editTextCheckPassword.text.toString()
                if (passwd != checkPasswd){
                    binding.textViewCheckPasswordGuide.text = "비밀번호가 일치하지 않습니다."
                    binding.textViewCheckPasswordGuide.setTextColor(Color.RED)
                }
                else {
                    binding.textViewCheckPasswordGuide.text = "비밀번호가 일치합니다."
                    binding.textViewCheckPasswordGuide.setTextColor(Color.BLUE)
                }
            }
        })


        binding.buttonSignup.setOnClickListener {   // 회원가입 버튼 클릭 시
            val dialog = AlertDialog.Builder(this@SignupActivity)
            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()
            val checkPW = binding.editTextCheckPassword.text.toString()
            if(password != checkPW){
                Toast.makeText(applicationContext, "비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val simpleAddress = binding.textViewSimpleAddress.text.toString()
            val detailAddress = binding.editTextDetailAddress.text.toString()

            if (username == "" || email == "" || password == "" || phoneNumber == "" || simpleAddress == "" || detailAddress == ""){
                Toast.makeText(applicationContext, "모든 정보를 입력해주세요.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!binding.checkboxInfo.isChecked || !binding.checkboxVeritas.isChecked){
                Toast.makeText(applicationContext, "이용약관에 동의가 필요합니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = SignupModel(username,email,password,phoneNumber, simpleAddress, detailAddress)

            signApi.requestSignup(data).enqueue(object: Callback<ResponseMsg>{  // 서버에 회원 정보 등록 요청
                override fun onResponse(
                    call: Call<ResponseMsg>,
                    response: Response<ResponseMsg>
                ) {
                    responseMsg = response.body()

                    if(response.code() == 200){
                        Log.d(TAG, "회원가입 성공")
                        dialog.setTitle("회원가입 완료")
                        dialog.setMessage("${username}님 회원가입 완료되었습니다.")
                        dialog.setPositiveButton("확인"){ _: DialogInterface, _: Int ->
                            finish()
                        }
                        dialog.show()
                    }
                }

                override fun onFailure(call: Call<ResponseMsg>, t: Throwable) {
                    Log.e(SigninActivity.TAG, "서버와의 연결에 실패: ${t.message.toString()}")
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
                if(data!=null)
                    binding.textViewSimpleAddress.text = address
            }
        }
    }

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 10000
        const val TAG = "회원가입"
    }
}