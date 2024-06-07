package com.entre.gethub.ui.home.mygethub

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityHomeKelolaMyGetHubGantiDesignPreviewWebviewBinding

class HomeKelolaMyGethubGantiDesignPreviewWebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeKelolaMyGetHubGantiDesignPreviewWebviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKelolaMyGetHubGantiDesignPreviewWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra("URL") ?: ""

        binding.iconBack.setOnClickListener {
            finish()
        }
        // Enable JavaScript
        binding.webView.settings.javaScriptEnabled = true

        // Set WebViewClient
        binding.webView.webViewClient = WebViewClient()

        binding.webView.loadUrl(url)
    }

}
