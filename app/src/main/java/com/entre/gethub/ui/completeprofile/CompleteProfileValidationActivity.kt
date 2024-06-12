package com.entre.gethub.ui.completeprofile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.databinding.ActivityCompleteProfileValidationBinding
import com.entre.gethub.ui.auth.LoginActivity
import com.entre.gethub.ui.completeprofile.CompleteProfileActivity
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationViewModel
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.getImageUri
import com.entre.gethub.utils.reduceFileImage
import com.entre.gethub.utils.uriToFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CompleteProfileValidationActivity : AppCompatActivity() {

    private val binding: ActivityCompleteProfileValidationBinding by lazy {
        ActivityCompleteProfileValidationBinding.inflate(
            layoutInflater
        )
    }

    private val completeProfileValidationViewModel by viewModels<CompleteProfileValidationViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    private var loginResponse: LoginResponse? = null
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loginResponse = intent.getParcelableExtra(EXTRA_USER)

        setupView()
    }

    private fun setupView() {

        if (loginResponse != null) {
            showSnackBar("Selamat Datang, ${loginResponse?.user?.username}")
            if (loginResponse?.user?.isCompleteProfile == true) {
                startActivity(
                    Intent(
                        this@CompleteProfileValidationActivity,
                        MainActivity::class.java
                    )
                )
                finish()
            }
        } else {
            getUserProfile()
        }

        with(binding) {
            ivScan.setOnClickListener {
                showImageSourceOptions()
            }

            ivManual.setOnClickListener {
                startActivity(
                    Intent(
                        this@CompleteProfileValidationActivity,
                        CompleteProfileActivity::class.java
                    )
                )
            }
        }
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Kamera", "Galeri")
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> requestCameraPermission()
                1 -> pickPhotoFromGallery()
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private val pickPhotoFromGalleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            completeProfileValidationViewModel.scanCard(
                uriToFile(
                    currentImageUri!!,
                    this@CompleteProfileValidationActivity
                ).reduceFileImage()
            ).observe(this@CompleteProfileValidationActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message.toString())
                            val intent = Intent(
                                this@CompleteProfileValidationActivity,
                                CompleteProfileActivity::class.java
                            ).apply {
                                putExtra(
                                    CompleteProfileActivity.SCAN_CARD_RESULT_EXTRA,
                                    result.data
                                )
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

    private fun pickPhotoFromGallery() {
        pickPhotoFromGalleryLauncher.launch("image/*")
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
        currentImageUri = getImageUri(this@CompleteProfileValidationActivity)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            completeProfileValidationViewModel.scanCard(
                uriToFile(
                    currentImageUri!!,
                    this@CompleteProfileValidationActivity
                ).reduceFileImage()
            ).observe(this@CompleteProfileValidationActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message.toString())
                            val intent = Intent(
                                this@CompleteProfileValidationActivity,
                                CompleteProfileActivity::class.java
                            ).apply {
                                putExtra(
                                    CompleteProfileActivity.SCAN_CARD_RESULT_EXTRA,
                                    result.data
                                )
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

    private fun getUserProfile() {
        completeProfileValidationViewModel.getUserProfile()
            .observe(this@CompleteProfileValidationActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val user = result.data.data
                            showSnackBar("Selamat Datang, ${user?.username}")

                            if (user?.isCompleteProfile == true) {
                                startActivity(
                                    Intent(
                                        this@CompleteProfileValidationActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showDialog(
                                this@CompleteProfileValidationActivity,
                                getString(R.string.something_went_wrong),
                                getString(
                                    R.string.please_login_again
                                )
                            )
                        }

                        else -> {
                            //
                        }
                    }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                navigateToLoginActivity()
            }
            .setOnDismissListener {
                navigateToLoginActivity()
            }
            .show()
    }

    private fun navigateToLoginActivity() {
        val intent =
            Intent(this@CompleteProfileValidationActivity, LoginActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}

