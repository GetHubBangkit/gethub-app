package com.entre.gethub.ui.project.postedproject.payment

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityOwnerPaymentWebViewBinding

class OwnerPaymentWebViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOwnerPaymentWebViewBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupView() {
        with(binding) {
            wvPayment.apply {
                settings.javaScriptEnabled = true
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                        super.onPageStarted(view, url, favicon)
                        showLoading(true)
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        showLoading(false)
                    }
                }
                loadUrl("https://google.com")
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}