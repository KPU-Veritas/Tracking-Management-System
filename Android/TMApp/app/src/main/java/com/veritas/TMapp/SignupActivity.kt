package com.veritas.TMapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.veritas.TMapp.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {
    private var signupBinding: ActivitySignupBinding? = null
    private val binding get() = signupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }
}