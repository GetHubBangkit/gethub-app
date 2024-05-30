package com.entre.gethub.ui.gethub

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.AddPartnerParams
import com.entre.gethub.databinding.ActivityGethubAddPartnerFormBinding
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.uriToFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GethubAddPartnerFormActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGethubAddPartnerFormBinding.inflate(layoutInflater) }
    private lateinit var getHubPartnerFormViewModel: GethubAddPartnerFormViewModel
    private var currentImageUri: Uri? = null
    private var imageUrl: String? = null

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                currentImageUri = result.data?.data
                showImage()
                uploadProfilePicture()
            }
        }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                currentImageUri = result.data?.data
                showImage()
                uploadProfilePicture()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()

        // Get the QR code from the intent
        val qrCode = intent.getStringExtra("QR_CODE")
        if (qrCode != null) {
            addPartnerUsingQRCode(qrCode)
        }

        with(binding) {
            tvUploadPhoto.setOnClickListener {
                selectImage()
            }

            btnSimpan.setOnClickListener {
                val fullname = nameTextField.editText?.text.toString()
                val profesi = profesiTextField.editText?.text.toString()
                val phone = phoneTextField.editText?.text.toString()
                val email = emailTextField.editText?.text.toString()
                val website = websiteTextField.editText?.text.toString()
                val alamat = alamatTextField.editText?.text.toString()

                val addPartnerParams = AddPartnerParams(
                    fullname,
                    profesi,
                    email,
                    phone,
                    website,
                    alamat,
                    photo = imageUrl,
                    image = null
                )

                addPartner(addPartnerParams)
            }

            iconBack.setOnClickListener {
                finish()
            }
        }

    }

    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        getHubPartnerFormViewModel =
            ViewModelProvider(this, factory)[GethubAddPartnerFormViewModel::class.java]
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_picture),
            getString(R.string.choose_form_gallery)
        )

        val materialDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Pilih Foto")
            .setItems(options) { dialog, item ->
                when {
                    options[item] == getString(R.string.take_picture) -> {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        takePicture.launch(takePictureIntent)
                    }

                    options[item] == getString(R.string.choose_form_gallery) -> {
                        val pickPhotoIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        pickImage.launch(pickPhotoIntent)
                    }

                }
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()

        materialDialog.show()
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivProfilePic.setImageURI(it)
        }
    }

    private fun uploadProfilePicture() {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imagefile = uriToFile(uri, this)
                getHubPartnerFormViewModel.uploadProfilePhoto(imagefile)
                    .observe(this@GethubAddPartnerFormActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    imageUrl = result.data.data
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showToast(result.error)
                                }

                                else -> {
                                    showLoading(false)
                                    showToast("Terjadi kesalahan")
                                }
                            }
                        }
                    }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun addPartner(addPartnerParams: AddPartnerParams) {
        getHubPartnerFormViewModel.addPartner(addPartnerParams).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        finish()
                        showToast(result.data.message.toString())
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    else -> {
                        showLoading(false)
                        showToast("Terjadi kesalahan")
                    }
                }
            }

        }

    }

    private fun addPartnerUsingQRCode(qrCode: String) {
        getHubPartnerFormViewModel.addPartnerQR(qrCode).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        finish()
                        showToast(result.data.message.toString())
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    else -> {
                        showLoading(false)
                        showToast("Terjadi kesalahan")
                    }
                }
            }
        }
    }
}
