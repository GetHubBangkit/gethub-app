package com.entre.gethub.ui.project

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.R
import com.entre.gethub.databinding.ActivityDigitalContractBinding

class DigitalContractActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDigitalContractBinding.inflate(layoutInflater) }
    private var redirectUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        redirectUrl = intent.getStringExtra(EXTRA_REDIRECT_URL).toString()
        setupWebView(redirectUrl)

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(redirectUrl: String) {
        binding.wvPayment.apply {
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(this@DigitalContractActivity), "Android")
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    showLoading(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    showLoading(false)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    return false
                }
            }

            webChromeClient = WebChromeClient()


            loadUrl(redirectUrl)
        }
    }

    private fun createWebPrintJob(webView: WebView) {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("MyDocument")
        val jobName = getString(R.string.app_name) + " Document"

        printManager.print(
            jobName,
            printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    class WebAppInterface(private val activity: DigitalContractActivity) {
        @JavascriptInterface
        fun printPage() {
            activity.runOnUiThread {
                activity.createWebPrintJob(activity.binding.wvPayment)
            }
        }
    }

    companion object {
        const val EXTRA_REDIRECT_URL = "extra_redirect_url"
    }
}