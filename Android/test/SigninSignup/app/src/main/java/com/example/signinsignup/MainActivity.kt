package com.example.signinsignup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.signinsignup.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    var user: PostModel? = null
    var api = APIS.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSignin.setOnClickListener {
            var email = binding.editTextEmailAddress.text.toString()
            var password = binding.editTextPassword.text.toString()
            var data = UserModel(email,password)
            var dialog = AlertDialog.Builder(this@MainActivity)
            api.requestLogin(data).enqueue(object : Callback<PostModel> {
                override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                    user  = response.body()
                    Log.d("LOGIN", "user: " + response.code())
                    Log.d("LOGIN", "token : " + user?.token)
                    Log.d("LOGIN", "uuid : " + user?.uuid)
                    Log.d("LOGIN", "email : " + user?.email)
                    dialog.setTitle(user?.email)
                    dialog.setMessage("token: " + user?.token + "\nuuid: " + user?.uuid)
                    dialog.show()
                }

                override fun onFailure(call: Call<PostModel>, t: Throwable) {
                    Log.e("LOGIN", t.message.toString())
                    dialog.setTitle("에러")
                    dialog.setMessage("호출실패했습니다.")
                    dialog.show()
                }
            })
        }
    }
}