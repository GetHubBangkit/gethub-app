package com.entre.gethub.ui.home.mygethub.certification

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubEditSertifikasiBinding
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.uriToFile

class HomeKelolaMyGethubEditSertifikasiActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGethubEditSertifikasiBinding.inflate(layoutInflater) }
    private val homeKelolaMyGetHubEditSertifikasiViewModel by viewModels<HomeKelolaMyGethubEditSertifikasiViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var certificationId: String? = null
    private var currentImageUri: Uri? = null
    private var image: String? = null
    private var initialCategoryId: String? = null
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        certificationId = intent.getStringExtra(EXTRA_CERTIFICATION_ID)

        setupView()
    }

    private fun setupView() {
        getCertificationDetail()

        with(binding) {
            iconBack.setOnClickListener {
                finish()
            }

            titleTextField.editText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    titleTextField.editText?.setError("Judul produk wajib diisi")
                } else {
                    titleTextField.editText?.error = null
                }
            }

            kategoriTextField.editText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    kategoriTextField.editText?.setError("Kategori wajib diisi")
                } else {
                    kategoriTextField.editText?.error = null
                }
            }

            tvOpenGallery.setOnClickListener {
                startGallery()
            }

            btnSimpan.setOnClickListener {
                val title = titleTextField.editText?.text.toString()
                val kategori = kategoriTextField.editText?.text.toString()

                if (title.isNotEmpty() || kategori.isNotEmpty() || image != null) {
                    editCertification(title, kategori, image.toString(), selectedCategoryId.toString())
                }
            }
        }

    }

    private fun getCategories() {
        homeKelolaMyGetHubEditSertifikasiViewModel.getCategories()
            .observe(this@HomeKelolaMyGethubEditSertifikasiActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val categories = result.data.data
                            val adapter = CategoryAdapter(
                                this@HomeKelolaMyGethubEditSertifikasiActivity,
                                categories
                            )
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.spinnerKategori.adapter = adapter

                            val initialPosition =
                                categories.indexOfFirst { it.id == initialCategoryId }

                            if (initialPosition >= 0) {
                                binding.spinnerKategori.setSelection(initialPosition)
                            }

                            binding.spinnerKategori.onItemSelectedListener =
                                object : AdapterView.OnItemSelectedListener {
                                    override fun onItemSelected(
                                        parent: AdapterView<*>?,
                                        view: View?,
                                        position: Int,
                                        id: Long
                                    ) {
                                        selectedCategoryId = adapter.getCategoryId(position)
                                    }

                                    override fun onNothingSelected(p0: AdapterView<*>?) {
                                        // Nothing
                                    }

                                }
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(getString(com.entre.gethub.R.string.failed_to_fetch_categories))
                        }

                        else -> {
                            //
                        }
                    }
                }
            }
    }

    private fun editCertification(
        title: String,
        kategori: String,
        image: String,
        categoryId: String
    ) {
        homeKelolaMyGetHubEditSertifikasiViewModel.editCertification(
            certificationId.toString(),
            title,
            image,
            categoryId,
        ).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        startActivity(
                            Intent(
                                this@HomeKelolaMyGethubEditSertifikasiActivity,
                                HomeKelolaMyGethubActivity::class.java
                            )
                        )
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

    private fun getCertificationDetail() {
        homeKelolaMyGetHubEditSertifikasiViewModel.getCertificationDetail(certificationId.toString())
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val certification = result.data.data

                            initialCategoryId = certification?.categoryId
                            image = certification?.image
                            getCategories()

                            with(binding) {
                                titleTextField.editText?.setText(certification?.title)
                                kategoriTextField.editText?.setText(certification?.categoryId)
                                Glide.with(this@HomeKelolaMyGethubEditSertifikasiActivity)
                                    .load(certification?.image)
                                    .placeholder(R.drawable.ic_image)
                                    .into(ivCertificationImage)
                            }
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

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
            uploadCertificationImage()
        } else {
            showToast("Gambar tidak ada")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivCertificationImage.setImageURI(it)
        }
    }

    private fun uploadCertificationImage() {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imagefile = uriToFile(uri, this)
                homeKelolaMyGetHubEditSertifikasiViewModel.uploadProfilePhoto(imagefile)
                    .observe(this@HomeKelolaMyGethubEditSertifikasiActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    image = result.data.data
                                    showToast("Gambar berhasil diunggah")
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


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_CERTIFICATION_ID = "extra_certification_id"
    }
}