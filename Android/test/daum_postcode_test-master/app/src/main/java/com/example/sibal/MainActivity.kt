package com.example.sibal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var et_address: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        et_address = findViewById<View>(R.id.et_address) as TextView
        val btn_search = findViewById<View>(R.id.button) as Button
        btn_search?.setOnClickListener {
            val i = Intent(this@MainActivity, WebViewActivity::class.java)
            startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY)
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == RESULT_OK) {
                val data = intent!!.extras?.getString("data")
                if (data != null) et_address!!.text = data
            }
        }
    }

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 10000
    }
}