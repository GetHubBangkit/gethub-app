package com.entre.gethub.ui.project.ownerpostedproject.payment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.databinding.ActivityOwnerPaymentWebViewBinding
import com.entre.gethub.ui.akun.paymenthistory.PaymentHistoryActivity

class OwnerPaymentWebViewActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOwnerPaymentWebViewBinding.inflate(layoutInflater) }
    private var redirectUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        redirectUrl = intent.getStringExtra(EXTRA_REDIRECT_URL).toString()
        setupWebView(redirectUrl)

        binding.iconBack.setOnClickListener {
            goToPaymentHistory()
        }
    }

    private fun goToPaymentHistory() {
        val intent = Intent(this, PaymentHistoryActivity::class.java).apply {
            putExtra(PaymentHistoryActivity.EXTRA_CODE_FROM_OTHER_ACTIVITY, 76)
        }
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        goToPaymentHistory()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(redirectUrl: String) {
        binding.wvPayment.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showLoading(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    showLoading(false)

                    url?.let {
                        if (it.startsWith("http://")) {
                            val intent = Intent(
                                this@OwnerPaymentWebViewActivity,
                                PaymentHistoryActivity::class.java
                            ).apply {
                                putExtra(PaymentHistoryActivity.EXTRA_CODE_FROM_OTHER_ACTIVITY, 76)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            loadUrl(redirectUrl)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_REDIRECT_URL = "extra_redirect_url"
    }
}