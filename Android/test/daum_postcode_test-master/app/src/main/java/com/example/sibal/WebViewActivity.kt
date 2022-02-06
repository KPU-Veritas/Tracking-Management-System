package com.example.sibal

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.sibal.WebViewActivity.MyJavaScriptInterface

class WebViewActivity : AppCompatActivity() {
    private var browser: WebView? = null

    internal inner class MyJavaScriptInterface {
        @JavascriptInterface
        fun processDATA(data: String?) {
            val extra = Bundle()
            val intent = Intent()
            extra.putString("data", data)
            intent.putExtras(extra)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        browser = findViewById<View>(R.id.webView) as WebView
        browser!!.settings.javaScriptEnabled = true
        browser!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        browser!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                browser!!.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //browser!!.loadUrl("file:///android_asset/daum.html");
        //browser.loadUrl("http://www.daddyface.com/public/daum.html");
        browser!!.loadUrl("http://www.inspond.com/daum.html")
        // 경고! 위 주소대로 서비스에 사용하시면 파일이 삭제됩니다.
        // 꼭 자신의 웹 서버에 해당 파일을 복사해서 주소를 변경해 사용하세요.
    }
}