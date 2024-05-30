package com.entre.gethub.ui.gethub

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator

class GethubAddPartnerScanQRActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT // Set orientation to portrait
        initiateScan()
    }

    private fun initiateScan() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Scan a QR code")
        integrator.setCameraId(0) // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.setCaptureActivity(CaptureActivityPortrait::class.java)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                finish() // Handle cancel
            } else {
                val scannedString = result.contents
                processScannedData(scannedString)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processScannedData(scannedString: String) {
        // Handle the scanned string
        val intent = Intent(this, GethubAddPartnerFormActivity::class.java)
        intent.putExtra("QR_CODE", scannedString)
        startActivity(intent)
        finish()
    }
}
