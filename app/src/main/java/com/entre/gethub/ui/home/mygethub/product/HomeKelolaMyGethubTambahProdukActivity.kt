package com.entre.gethub.ui.home.mygethub.product

import android.R
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubTambahProdukBinding
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.uriToFile

class HomeKelolaMyGethubTambahProdukActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeKelolaMyGethubTambahProdukBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var homeKelolaMyGetHubTambahProdukViewModel: HomeKelolaMyGethubTambahProdukViewModel
    private var currentImageUri: Uri? = null
    private var imageUrl: String? = null
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViewModel()
        setupView()
    }

    private fun setupView() {
        getCategories()
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

            descriptionTextField.editText?.doOnTextChanged { text, start, before, count ->
                if (text.toString().isEmpty()) {
                    descriptionTextField.editText?.setError("Deskripsi produk wajib diisi")
                } else {
                    descriptionTextField.editText?.error = null
                }
            }

            tvOpenGallery.setOnClickListener {
                startGallery()
            }

            btnSimpan.setOnClickListener {
                val name = titleTextField.editText?.text.toString()
                val description = descriptionTextField.editText?.text.toString()

                if (name.isNotEmpty() || description.isNotEmpty() || imageUrl != null || selectedCategoryId != null) {
                    addProduct(name, description, imageUrl.toString(), selectedCategoryId.toString())
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
            uploadProductImage()
        } else {
            showToast("Gambar tidak ada")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivProductImage.setImageURI(it)
        }
    }


    private fun initViewModel() {
        val factory = ViewModelFactory.getInstance(this)
        homeKelolaMyGetHubTambahProdukViewModel =
            ViewModelProvider(this, factory)[HomeKelolaMyGethubTambahProdukViewModel::class.java]
    }

    private fun getCategories() {
        homeKelolaMyGetHubTambahProdukViewModel.getCategories()
            .observe(this@HomeKelolaMyGethubTambahProdukActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val categories = result.data.data
                            val adapter = CategoryAdapter(
                                this@HomeKelolaMyGethubTambahProdukActivity,
                                categories
                            )
                            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
                            binding.spinnerKategori.adapter = adapter

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

    private fun uploadProductImage() {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imagefile = uriToFile(uri, this)
                homeKelolaMyGetHubTambahProdukViewModel.uploadProfilePhoto(imagefile)
                    .observe(this@HomeKelolaMyGethubTambahProdukActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    imageUrl = result.data.data
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

    private fun addProduct(name: String, description: String, imageUrl: String, category: String) {
        homeKelolaMyGetHubTambahProdukViewModel.addProduct(name, description, imageUrl, category)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message.toString())
                            startActivity(
                                Intent(
                                    this@HomeKelolaMyGethubTambahProdukActivity,
                                    HomeKelolaMyGethubActivity::class.java
                                )
                            )
                            finish()
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}