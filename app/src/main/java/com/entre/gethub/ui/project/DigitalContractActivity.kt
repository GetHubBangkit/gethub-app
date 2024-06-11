package com.entre.gethub.ui.project

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.databinding.ActivityDigitalContractBinding


class DigitalContractActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDigitalContractBinding.inflate(layoutInflater) }
    private var redirectUrl: String = ""
    private var contractId: String = ""
    private var printWeb: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        redirectUrl = intent.getStringExtra(EXTRA_REDIRECT_URL).toString()
        contractId = intent.getStringExtra(EXTRA_CONTRACT_ID).toString()

        setupWebView(redirectUrl)
        setupPdfDownloadButton()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView(redirectUrl: String) {
        binding.wvContract.apply {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    Log.d("DigitalContractActivity", "onPageStarted: $url")
                    super.onPageStarted(view, url, favicon)
                    showLoading(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    Log.d("DigitalContractActivity", "onPageFinished: $url")
                    super.onPageFinished(view, url)
                    showLoading(false)
                    printWeb = binding.wvContract
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    Log.d("DigitalContractActivity", "shouldOverrideUrlLoading: ${request?.url}")
                    return false
                }
            }

            webChromeClient = WebChromeClient()

            loadUrl(redirectUrl)
        }
    }

    private fun setupPdfDownloadButton() {
        binding.fabSave.setOnClickListener {
            if (printWeb != null) {
                printTheWebPage(printWeb!!);
            } else {
                Toast.makeText(this, "WebPage not fully loaded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // object of print job
    var printJob: PrintJob? = null

    // a boolean to check the status of printing
    var printBtnPressed = false

    private fun printTheWebPage(webView: WebView) {

        // set printBtnPressed true
        printBtnPressed = true

        // Creating  PrintManager instance
        val printManager = this
            .getSystemService(PRINT_SERVICE) as PrintManager

        // setting the name of job
        val jobName = "contract"

        // Creating  PrintDocumentAdapter instance
        val printAdapter = webView.createPrintDocumentAdapter(jobName)
        assert(printManager != null)
        printJob = printManager.print(
            jobName, printAdapter,
            PrintAttributes.Builder().build()
        )
    }

    override fun onResume() {
        super.onResume()
        if (printJob != null && printBtnPressed) {
            if (printJob!!.isCompleted()) {
                Toast.makeText(this, "Print dokumen berhasil", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isCancelled()) {
                Toast.makeText(this, "Print dokumen batal", Toast.LENGTH_SHORT).show()
            } else if (printJob!!.isFailed()) {
                Toast.makeText(this, "Print dokumen gagal", Toast.LENGTH_SHORT).show()
            }
            printBtnPressed = false
        }
    }


    private fun downloadAndSaveContent(url: String) {
        Log.d("DigitalContractActivity", "downloadAndSaveContent: $url")
        val request = DownloadManager.Request(Uri.parse(url))
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("PDF Download")
        request.setDescription("Downloading PDF file")
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "contract.pdf")

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_REDIRECT_URL = "extra_redirect_url"
        const val EXTRA_CONTRACT_ID = "extra_contract_url"
    }
}