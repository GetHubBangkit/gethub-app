package com.bangkit.gethub.ui.completeprofile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bangkit.gethub.R
import com.bangkit.gethub.data.Result
import com.bangkit.gethub.data.remote.params.UpdateUserProfileParams
import com.bangkit.gethub.databinding.ActivityCompleteProfileBinding
import com.bangkit.gethub.utils.ViewModelFactory

class CompleteProfileActivity : AppCompatActivity() {

    private val binding: ActivityCompleteProfileBinding by lazy {
        ActivityCompleteProfileBinding.inflate(
            layoutInflater
        )
    }
    private val completeProfileViewModel by viewModels<CompleteProfileViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var currentImageUri: Uri? = null

    private lateinit var edFullname: EditText
    private lateinit var edProfession: EditText
    private lateinit var edPhone: EditText
    private lateinit var edEmail: EditText
    private lateinit var edWebsite: EditText
    private lateinit var edAddress: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        edFullname = binding.nameTextField.editText!!
        edProfession = binding.profesiTextField.editText!!
        edPhone = binding.phoneTextField.editText!!
        edEmail = binding.emailTextField.editText!!
        edWebsite = binding.websiteTextField.editText!!
        edAddress = binding.alamatTextField.editText!!

        with(binding) {
            tvUploadPhoto.setOnClickListener {
                selectImage()
            }

            btnSimpan.setOnClickListener {
                val fullname = edFullname.text.toString()
                val profesi = edProfession.text.toString()
                val phone = edPhone.text.toString()
                val email = edEmail.text.toString()
                val website = edWebsite.text.toString()
                val alamat = edAddress.text.toString()

                val updateUserProfileParams = UpdateUserProfileParams(
                    fullname,
                    profesi,
                    email,
                    phone,
                    website,
                    alamat,
                    photo = null
                )

                updateUserProfile(updateUserProfileParams)


            }

            setUpEditText()
        }
    }

    private fun setUpEditText() {
        binding.apply {

            completeProfileViewModel.getUserProfile()
                .observe(this@CompleteProfileActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> showLoading(true)
                            is Result.Success -> {
                                showLoading(false)
                                val userProfile = result.data.data
                                edFullname.setText(userProfile?.fullName ?: "")
                                edProfession.setText(
                                    userProfile?.profession ?: ""
                                )
                                edPhone.setText(userProfile?.phone ?: "")
                                edEmail.setText(userProfile?.email ?: "")
                                edWebsite.setText(userProfile?.web ?: "")
                                edAddress.setText(userProfile?.address ?: "")
                            }

                            is Result.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }

                            else -> {
                                showLoading(false)
                                showToast(getString(R.string.something_went_wrong))
                            }
                        }
                    }

                }

            edFullname.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    nameTextField.error = getString(R.string.name_field_couldn_be_empty)
                }
            }

            edProfession.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    profesiTextField.error =
                        getString(R.string.profession_field_couldn_be_empty)
                }
            }

            edWebsite.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    websiteTextField.error = getString(R.string.website_field_couldn_be_empty)
                }
            }

            edPhone.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    phoneTextField.error = getString(R.string.phone_field_couldn_be_empty)
                }
            }

            edAddress.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    alamatTextField.error = getString(R.string.address_field_couldn_be_empty)
                }
            }
        }
    }

    private fun updateUserProfile(updateUserProfileParams: UpdateUserProfileParams) {
        completeProfileViewModel.updateUserProfile(updateUserProfileParams)
            .observe(this@CompleteProfileActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            // Intent untuk berpindah ke MainActivity
//                            startActivity(
//                                Intent(
//                                    this@CompleteProfileActivity,
//                                    MainActivity::class.java
//                                )
//                            )
                            // Menutup Activity CompleteProfileActivity
                            finish()
                            showToast(result.data.message!!)
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)
                        }

                        else -> {
                            showLoading(false)
                            showToast("Terjadi Kesalahan")
                        }
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