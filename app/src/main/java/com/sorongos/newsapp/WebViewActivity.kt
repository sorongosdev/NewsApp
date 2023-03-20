package com.sorongos.newsapp

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sorongos.newsapp.databinding.ActivityWebviewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("url")
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        //url null
        if(url.isNullOrEmpty()){
            Toast.makeText(this,"Wrong Url", Toast.LENGTH_SHORT).show()
            finish()
        }else{
            binding.webView.loadUrl(url)
        }
    }
}