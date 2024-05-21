package com.bangkit.gethub.ui.completeprofile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bangkit.gethub.R
import com.bangkit.gethub.databinding.ActivityCompleteProfileBinding

class CompleteProfileActivity : AppCompatActivity() {

    private val binding: ActivityCompleteProfileBinding by lazy {
        ActivityCompleteProfileBinding.inflate(
            layoutInflater
        )
    }
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            tvUploadPhoto.setOnClickListener {
                selectImage()
            }
        }

        setUpEditText()
    }

    private fun setUpEditText() {
        binding.apply {
            nameTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    nameTextField.error = getString(R.string.name_field_couldn_be_empty)
                }
            }

            profesiTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    profesiTextField.error = getString(R.string.profession_field_couldn_be_empty)
                }
            }

            websiteTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    websiteTextField.error = getString(R.string.website_field_couldn_be_empty)
                }
            }

            phoneTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    phoneTextField.error = getString(R.string.phone_field_couldn_be_empty)
                }
            }

            alamatTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    alamatTextField.error = getString(R.string.address_field_couldn_be_empty)
                }
            }
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                binding.ivProfilePicture.setImageBitmap(imageBitmap)
            }
        }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data?.data != null) {
                    currentImageUri = result.data?.data
                    showImage()
//                    uploadProfilePicture()
                } else {
                    showToast(getString(R.string.image_unavailable))
                }
            }
        }

    private fun selectImage() {
        val options = arrayOf<CharSequence>(
            getString(R.string.take_picture),
            getString(R.string.choose_form_gallery), getString(R.string.cancel)
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Foto")
        builder.setItems(options) { dialog, item ->
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

                options[item] == getString(R.string.cancel) -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun showImage() {
        currentImageUri.let {
            binding.ivProfilePicture.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@CompleteProfileActivity, message, Toast.LENGTH_SHORT).show()
    }
}