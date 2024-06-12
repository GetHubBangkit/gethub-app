package com.entre.gethub.ui.gethub

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityGethubAddPartnerBinding
import com.entre.gethub.ui.completeprofile.CompleteProfileActivity
import com.entre.gethub.utils.getImageUri
import com.entre.gethub.utils.reduceFileImage
import com.entre.gethub.utils.uriToFile
import com.entre.gethub.utils.ViewModelFactory
import java.io.File

class GethubAddPartnerActivity : AppCompatActivity() {

    private val binding by lazy { ActivityGethubAddPartnerBinding.inflate(layoutInflater) }
    private var currentImageUri: Uri? = null

    private lateinit var gethubAddPartnerViewModel: GethubAddPartnerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val viewModelFactory = ViewModelFactory.getInstance(this)
        gethubAddPartnerViewModel = ViewModelProvider(this, viewModelFactory).get(GethubAddPartnerViewModel::class.java)

        // Menambahkan onClickListener pada ImageView manual
        binding.ivManual.setOnClickListener {
            // Intent untuk berpindah ke GethubAddPartnerFormActivity
            val intent = Intent(this, GethubAddPartnerFormActivity::class.java)
            startActivity(intent)
        }

        binding.ivScanqr.setOnClickListener {
            // Intent untuk berpindah ke GethubAddPartnerScanQRActivity
            val intent = Intent(this, GethubAddPartnerScanQRActivity::class.java)
            startActivity(intent)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }

        binding.ivScan.setOnClickListener {
            requestCameraPermission()
        }
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Fitur kamera tidak diizinkan",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                startCamera()
            }

            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this@GethubAddPartnerActivity)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            gethubAddPartnerViewModel.scanCard(
                uriToFile(
                    currentImageUri!!,
                    this@GethubAddPartnerActivity
                ).reduceFileImage()
            ).observe(this@GethubAddPartnerActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message.toString())
                            val intent = Intent(this@GethubAddPartnerActivity, GethubAddPartnerFormActivity::class.java).apply {
                                putExtra(GethubAddPartnerFormActivity.SCAN_CARD_RESULT_EXTRA, result.data)
                            }
                            startActivity(intent)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }

                        else -> {
                            //
                        }
                    }
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBarAddPartner.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        "Scan Card Tidak berhasil"
    }
}
