package com.entre.gethub.ui.home.mygethub.tentangsaya

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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.params.UpdateUserProfileParams
import com.entre.gethub.data.remote.response.ml.ScanCardResponse
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubEditTentangsayaBinding
import com.entre.gethub.ui.completeprofile.CompleteProfileViewModel
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.uriToFile
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeKelolaMyGethubEditTentangSayaActivity : AppCompatActivity() {

    private val binding: ActivityHomeKelolaMyGethubEditTentangsayaBinding by lazy {
        ActivityHomeKelolaMyGethubEditTentangsayaBinding.inflate(layoutInflater)
    }
    private val completeProfileViewModel by viewModels<CompleteProfileViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null
    private var imageUrl: String? = null

    private lateinit var edFullname: EditText
    private lateinit var edProfession: EditText
    private lateinit var edPhone: EditText
    private lateinit var edEmail: EditText
    private lateinit var edWebsite: EditText
    private lateinit var edAddress: EditText
    private lateinit var edAbout: EditText

    private var scanCardResponse: ScanCardResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (intent != null) {
            scanCardResponse = intent.getParcelableExtra(SCAN_CARD_RESULT_EXTRA)
        }

        setupView()
    }

    private fun setupView() {
        edFullname = binding.nameTextField.editText!!
        edProfession = binding.profesiTextField.editText!!
        edPhone = binding.phoneTextField.editText!!
        edEmail = binding.emailTextField.editText!!
        edWebsite = binding.websiteTextField.editText!!
        edAddress = binding.alamatTextField.editText!!
        edAbout = binding.aboutTextField.editText!!

        with(binding) {
            tvUploadPhoto.setOnClickListener {
                selectImage()
            }

            iconBack.setOnClickListener {
                finish()
            }

            btnSimpan.setOnClickListener {
                val fullname = edFullname.text.toString()
                val profesi = edProfession.text.toString()
                val phone = edPhone.text.toString()
                val email = edEmail.text.toString()
                val website = edWebsite.text.toString()
                val alamat = edAddress.text.toString()
                val about = edAbout.text.toString()

                val updateUserProfileParams = UpdateUserProfileParams(
                    fullname,
                    profesi,
                    email,
                    phone,
                    website,
                    alamat,
                    about,
                    imageUrl // Use existing imageUrl if no new image is uploaded
                )

                updateUserProfile(updateUserProfileParams)
            }

            setUpEditText()
        }
    }

    private fun setUpEditText() {
        binding.apply {
            edFullname.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edFullname.error = getString(R.string.name_field_couldn_be_empty)
                } else {
                    edFullname.error = null
                }
            }

            edProfession.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edProfession.error = getString(R.string.profession_field_couldn_be_empty)
                } else {
                    edProfession.error = null
                }
            }


            edPhone.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edPhone.error = getString(R.string.phone_field_couldn_be_empty)
                } else {
                    edPhone.error = null
                }
            }

            edAddress.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edAddress.error = getString(R.string.address_field_couldn_be_empty)
                } else {
                    edAddress.error = null
                }
            }


            // Observe the user profile data
            completeProfileViewModel.getUserProfile().observe(this@HomeKelolaMyGethubEditTentangSayaActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val userProfile = result.data.data
                            edFullname.setText(userProfile?.fullName ?: "")
                            edProfession.setText(userProfile?.profession ?: "")
                            edPhone.setText(userProfile?.phone ?: "")
                            edEmail.setText(userProfile?.email ?: "")
                            edWebsite.setText(userProfile?.web ?: "")
                            edAddress.setText(userProfile?.address ?: "")
                            edAbout.setText(userProfile?.about ?: "")

                            // Load the profile picture using Glide
                            userProfile?.photo?.let {
                                imageUrl = it // Capture the existing imageUrl
                                Glide.with(this@HomeKelolaMyGethubEditTentangSayaActivity)
                                    .load(it)
                                    .placeholder(R.drawable.profilepic1) // Add a placeholder image
                                    .into(binding.ivProfilePicture)
                            }
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

            if (scanCardResponse != null) {
                val userProfile = scanCardResponse?.data
                edFullname.setText(userProfile?.name ?: "")
                edProfession.setText(userProfile?.profession ?: "")
                edPhone.setText(userProfile?.phone ?: "")
                edEmail.setText(userProfile?.email ?: "")
                edWebsite.setText(userProfile?.web ?: "")
                edAddress.setText(userProfile?.address ?: "")
            }
        }
    }


    private fun updateUserProfile(updateUserProfileParams: UpdateUserProfileParams) {
        completeProfileViewModel.updateUserProfile(updateUserProfileParams)
            .observe(this@HomeKelolaMyGethubEditTentangSayaActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true                        )
                        is Result.Success -> {
                            showLoading(false)
                            // Intent to move to MainActivity
                            startActivity(
                                Intent(
                                    this@HomeKelolaMyGethubEditTentangSayaActivity,
                                    HomeKelolaMyGethubActivity::class.java
                                )
                            )
                            // Close the CompleteProfileActivity
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
                    uploadProfilePicture()
                } else {
                    showToast(getString(R.string.image_unavailable))
                }
            }
        }

    private fun uploadProfilePicture() {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imagefile = uriToFile(uri, this)
                completeProfileViewModel.uploadProfilePhoto(imagefile)
                    .observe(this@HomeKelolaMyGethubEditTentangSayaActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    imageUrl = result.data.data
                                    // Load the new profile picture using Glide
                                    Glide.with(this@HomeKelolaMyGethubEditTentangSayaActivity)
                                        .load(imageUrl)
                                        .placeholder(R.drawable.profilepic1) // Add a placeholder image
                                        .into(binding.ivProfilePicture)
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
            }
        }
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
        currentImageUri?.let {
            binding.ivProfilePicture.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@HomeKelolaMyGethubEditTentangSayaActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val SCAN_CARD_RESULT_EXTRA = "scan_card_result_extra"
    }
}

