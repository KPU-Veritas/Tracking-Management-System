package com.veritas.TMapp.sign

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.veritas.TMapp.MainActivity
import com.veritas.TMapp.R

class FindAddressActivity : AppCompatActivity() {   // WebView에 카카오 주소 Api 등록
    private var browser: WebView? = null

    internal inner class MyJavaScriptInterface {    // 주소 선택 시 MainActivity로 address 데이터 반환
        @JavascriptInterface
        fun processDATA(data: String?) {
            var outIntent = Intent(applicationContext, MainActivity::class.java)
            outIntent.putExtra("address", data.toString())
            setResult(Activity.RESULT_OK, outIntent)
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {    // WebView 등록
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_address)
        browser = findViewById<View>(R.id.webView) as WebView
        browser!!.settings.javaScriptEnabled = true
        browser!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        browser!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                browser!!.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        browser!!.loadUrl("http://www.inspond.com/daum.html")
    }
}