package com.veritas.TMapp.sign

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Button
import com.veritas.TMapp.R

class FindAddressActivity : AppCompatActivity() {
    private var browser: WebView? = null
    private var textViewSimpleAddress: TextView? = null

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_address)
        browser = findViewById<View>(R.id.webView) as WebView
        textViewSimpleAddress = findViewById<View>(R.id.textViewSimpleAddress) as TextView
        val buttonSearchAddress = findViewById<View>(R.id.buttonSearchAddress) as Button
        browser!!.settings.javaScriptEnabled = true
        browser!!.addJavascriptInterface(MyJavaScriptInterface(), "Android")
        browser!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                browser!!.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        browser!!.loadUrl("http://www.inspond.com/daum.html")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            SEARCH_ADDRESS_ACTIVITY -> if (resultCode == RESULT_OK) {
                val data = intent!!.extras?.getString("data")
                if (data != null) browser!!.text = data
            }
        }
    }

    companion object {
        private const val SEARCH_ADDRESS_ACTIVITY = 10000
    }
}