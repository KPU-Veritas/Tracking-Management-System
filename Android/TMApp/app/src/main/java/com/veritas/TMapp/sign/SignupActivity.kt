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
    var flag = false
    var flag2 = false
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
                Toast.makeText(applicationContext, "이용약관 동의가 필요합니다.",Toast.LENGTH_SHORT).show()
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

        binding.checkboxVeritas.setOnClickListener{
            binding.checkboxVeritas.isChecked = false
            if (!flag) {
                val dialog = AlertDialog.Builder(this@SignupActivity)
                dialog.setTitle("TMApp 이용약관 동의")
                dialog.setMessage(
                    "제1장 총칙\n" +
                            "제1조 (목적)\n" +
                            "본 약관은 베리타스㈜(이하 ’회사’라 함)가 제공하는 ‘TMApp’에 관한 모든 제품 및 서비스(이하 ‘서비스’라 함)를 이용함에 있어 이용자의 권리_의무 및 책임에 관한 사항을 규정함을 목적으로 합니다.\n"
                )
                dialog.setPositiveButton("동의") { _: DialogInterface, _: Int ->
                    binding.checkboxVeritas.isChecked = true
                    flag = true
                }
                dialog.setNegativeButton("취소") { _: DialogInterface, _: Int ->
                    binding.checkboxVeritas.isChecked = false
                }
                dialog.show()
            }
            else{
                binding.checkboxVeritas.isChecked = false
                flag = false
            }
        }

        binding.checkboxInfo.setOnClickListener {
            binding.checkboxInfo.isChecked = false
            if (!flag2) {
                val dialog = AlertDialog.Builder(this@SignupActivity)
                dialog.setTitle("개인정보 수집 및 이용 동의")
                dialog.setMessage(
                    "개인정보보호법에 따라 TMApp에 회원가입 신청하시는 분께 수집하는 개인정보의 항목, 개인정보의 수집 및 이용목적, 개인정보의 보유 및 이용기간, 동의 거부권 및 동의 거부 시 불이익에 관한 사항을 안내 드리오니 자세히 읽은 후 동의하여 주시기 바랍니다.\n" +
                            "\n" +
                            "1. 수집하는 개인정보\n" +
                            "회원가입 시점에 TMApp이 이용자로부터 수집하는 개인정보는 아래와 같습니다.\n" +
                            "- 회원 가입 시에 ‘아이디, 비밀번호, 이름, 생년월일, 성별, 휴대전화번호’를 필수항목으로 수집합니다. 만약 이용자가 입력하는 생년월일이 만14세 미만 아동일 경우에는 법정대리인 정보(법정대리인의 이름, 생년월일, 성별, 중복가입확인정보(DI), 휴대전화번호)를 추가로 수집합니다. 그리고 선택항목으로 이메일 주소를 수집합니다.\n" +
                            "- 단체아이디로 회원가입 시 단체아이디, 비밀번호, 단체이름, 이메일주소, 휴대전화번호를 필수항목으로 수집합니다. 그리고 단체 대표자명을 선택항목으로 수집합니다.\n" +
                            "서비스 이용 과정에서 이용자로부터 수집하는 개인정보는 아래와 같습니다.\n" +
                            "- 회원정보 또는 개별 서비스에서 프로필 정보(별명, 프로필 사진)를 설정할 수 있습니다. 회원정보에 별명을 입력하지 않은 경우에는 마스킹 처리된 아이디가 별명으로 자동 입력됩니다.\n" +
                            "\n" +
                            "- TMApp 내의 개별 서비스 이용, 이벤트 응모 및 경품 신청 과정에서 해당 서비스의 이용자에 한해 추가 개인정보 수집이 발생할 수 있습니다. 추가로 개인정보를 수집할 경우에는 해당 개인정보 수집 시점에서 이용자에게 ‘수집하는 개인정보 항목, 개인정보의 수집 및 이용목적, 개인정보의 보관기간’에 대해 안내 드리고 동의를 받습니다.\n" +
                            "\n" +
                            "서비스 이용 과정에서 IP 주소, 쿠키, 서비스 이용 기록, 기기정보, 위치정보가 생성되어 수집될 수 있습니다. 또한 이미지 및 음성을 이용한 검색 서비스 등에서 이미지나 음성이 수집될 수 있습니다.\n" +
                            "구체적으로 1) 서비스 이용 과정에서 이용자에 관한 정보를 자동화된 방법으로 생성하여 이를 저장(수집)하거나,\n" +
                            "2) 이용자 기기의 고유한 정보를 원래의 값을 확인하지 못 하도록 안전하게 변환하여 수집합니다. 서비스 이용 과정에서 위치정보가 수집될 수 있으며,\n" +
                            "TMApp에서 제공하는 위치기반 서비스에 대해서는 'TMApp 위치정보 이용약관'에서 자세하게 규정하고 있습니다.\n" +
                            "이와 같이 수집된 정보는 개인정보와의 연계 여부 등에 따라 개인정보에 해당할 수 있고, 개인정보에 해당하지 않을 수도 있습니다.\n" +
                            "\n" +
                            "2. 수집한 개인정보의 이용\n" +
                            "TMApp 및 TMApp 관련 제반 서비스(모바일 웹/앱 포함)의 회원관리, 서비스 개발・제공 및 향상, 안전한 인터넷 이용환경 구축 등 아래의 목적으로만 개인정보를 이용합니다.\n" +
                            "\n" +
                            "- 회원 가입 의사의 확인, 연령 확인 및 법정대리인 동의 진행, 이용자 및 법정대리인의 본인 확인, 이용자 식별, 회원탈퇴 의사의 확인 등 회원관리를 위하여 개인정보를 이용합니다.\n" +
                            "- 콘텐츠 등 기존 서비스 제공(광고 포함)에 더하여, 인구통계학적 분석, 서비스 방문 및 이용기록의 분석, 개인정보 및 관심에 기반한 이용자간 관계의 형성, 지인 및 관심사 등에 기반한 맞춤형 서비스 제공 등 신규 서비스 요소의 발굴 및 기존 서비스 개선 등을 위하여 개인정보를 이용합니다.\n" +
                            "- 법령 및 TMApp 이용약관을 위반하는 회원에 대한 이용 제한 조치, 부정 이용 행위를 포함하여 서비스의 원활한 운영에 지장을 주는 행위에 대한 방지 및 제재, 계정도용 및 부정거래 방지, 약관 개정 등의 고지사항 전달, 분쟁조정을 위한 기록 보존, 민원처리 등 이용자 보호 및 서비스 운영을 위하여 개인정보를 이용합니다.\n" +
                            "- 유료 서비스 제공에 따르는 본인인증, 구매 및 요금 결제, 상품 및 서비스의 배송을 위하여 개인정보를 이용합니다.\n" +
                            "- 이벤트 정보 및 참여기회 제공, 광고성 정보 제공 등 마케팅 및 프로모션 목적으로 개인정보를 이용합니다.\n" +
                            "- 서비스 이용기록과 접속 빈도 분석, 서비스 이용에 대한 통계, 서비스 분석 및 통계에 따른 맞춤 서비스 제공 및 광고 게재 등에 개인정보를 이용합니다.\n" +
                            "- 보안, 프라이버시, 안전 측면에서 이용자가 안심하고 이용할 수 있는 서비스 이용환경 구축을 위해 개인정보를 이용합니다.\n" +
                            "3. 개인정보의 보관기간\n" +
                            "회사는 원칙적으로 이용자의 개인정보를 회원 탈퇴 시 지체없이 파기하고 있습니다.\n" +
                            "단, 이용자에게 개인정보 보관기간에 대해 별도의 동의를 얻은 경우, 또는 법령에서 일정 기간 정보보관 의무를 부과하는 경우에는 해당 기간 동안 개인정보를 안전하게 보관합니다.\n" +
                            "\n" +
                            "이용자에게 개인정보 보관기간에 대해 회원가입 시 또는 서비스 가입 시 동의를 얻은 경우는 아래와 같습니다.\n" +
                            "- 부정 가입 및 이용 방지\n" +
                            "부정 이용자의 가입인증 휴대전화번호 또는 DI (만14세 미만의 경우 법정대리인DI) : 탈퇴일로부터 6개월 보관\n" +
                            "탈퇴한 이용자의 휴대전화번호(복호화가 불가능한 일방향 암호화(해시처리)) : 탈퇴일로부터 6개월 보관\n" +
                            "휴대전화번호:등록/수정/삭제 요청 시로부터 최대1년\n" +
                            "암호화처리(해시처리)한DI :혜택 제공 종료일로부터6개월 보관\n" +
                            "전자상거래 등에서의 소비자 보호에 관한 법률, 전자문서 및 전자거래 기본법, 통신비밀보호법 등 법령에서 일정기간 정보의 보관을 규정하는 경우는 아래와 같습니다. TMApp은 이 기간 동안 법령의 규정에 따라 개인정보를 보관하며, 본 정보를 다른 목적으로는 절대 이용하지 않습니다.\n" +
                            "- 전자상거래 등에서 소비자 보호에 관한 법률\n" +
                            "계약 또는 청약철회 등에 관한 기록: 5년 보관\n" +
                            "대금결제 및 재화 등의 공급에 관한 기록: 5년 보관\n" +
                            "소비자의 불만 또는 분쟁처리에 관한 기록: 3년 보관\n" +
                            "- 전자문서 및 전자거래 기본법\n" +
                            "공인전자주소를 통한 전자문서 유통에 관한 기록 : 10년 보관\n" +
                            "- 전자서명 인증 업무\n" +
                            "인증서와 인증 업무에 관한 기록: 인증서 효력 상실일로부터 10년 보관\n" +
                            "- 통신비밀보호법\n" +
                            "로그인 기록: 3개월\n" +
                            "\n" +
                            "참고로 TMApp은 ‘개인정보 유효기간제’에 따라 1년간 서비스를 이용하지 않은 회원의 개인정보를 별도로 분리 보관하여 관리하고 있습니다.\n" +
                            "\n" +
                            "4. 개인정보 수집 및 이용 동의를 거부할 권리\n" +
                            "이용자는 개인정보의 수집 및 이용 동의를 거부할 권리가 있습니다. 회원가입 시 수집하는 최소한의 개인정보, 즉, 필수 항목에 대한 수집 및 이용 동의를 거부하실 경우, 회원가입이 어려울 수 있습니다."
                )
                dialog.setPositiveButton("동의") { _: DialogInterface, _: Int ->
                    binding.checkboxInfo.isChecked = true
                    flag2 = true
                }
                dialog.setNegativeButton("취소") { _: DialogInterface, _: Int ->
                    binding.checkboxInfo.isChecked = false
                }
                dialog.show()
            }
            else{
                binding.checkboxInfo.isChecked = false
                flag2 = false
            }
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