package com.entre.gethub.ui.home.mygethub.product

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
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubEditProdukBinding
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.uriToFile

class HomeKelolaMyGethubEditProdukActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeKelolaMyGethubEditProdukBinding.inflate(layoutInflater) }
    private val homeKelolaMyGetHubEditProdukViewModel by viewModels<HomeKelolaMyGethubEditProdukViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var productId: String? = null
    private var currentImageUri: Uri? = null
    private var imageUrl: String? = null
    private var initialCategoryId: String? = null
    private var selectedCategoryId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productId = intent.getStringExtra(EXTRA_PRODUCT_ID)

        setupView()
    }

    private fun setupView() {
        getProductDetail()

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

                if (name.isNotEmpty() || description.isNotEmpty() || imageUrl != null) {
                    editProduct(name, description, imageUrl.toString(), selectedCategoryId.toString())
                }
            }
        }

    }

    private fun getCategories() {
        homeKelolaMyGetHubEditProdukViewModel.getCategories()
            .observe(this@HomeKelolaMyGethubEditProdukActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val categories = result.data.data
                            val adapter = CategoryAdapter(
                                this@HomeKelolaMyGethubEditProdukActivity,
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

    private fun editProduct(
        name: String,
        description: String,
        imageUrl: String,
        categoryId: String
    ) {
        homeKelolaMyGetHubEditProdukViewModel.editProduct(
            productId.toString(),
            name,
            description,
            imageUrl,
            categoryId,
        ).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        startActivity(
                            Intent(
                                this@HomeKelolaMyGethubEditProdukActivity,
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

    private fun getProductDetail() {
        homeKelolaMyGetHubEditProdukViewModel.getProductDetail(productId.toString())
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val product = result.data.data

                            initialCategoryId = product?.categoryId
                            imageUrl = product?.imageUrl
                            getCategories()

                            with(binding) {
                                titleTextField.editText?.setText(product?.name)
                                descriptionTextField.editText?.setText(product?.description)
                                Glide.with(this@HomeKelolaMyGethubEditProdukActivity)
                                    .load(product?.imageUrl)
                                    .placeholder(R.drawable.ic_image)
                                    .into(ivProductImage)
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

    private fun uploadProductImage() {
        if (currentImageUri != null) {
            currentImageUri?.let { uri ->
                val imagefile = uriToFile(uri, this)
                homeKelolaMyGetHubEditProdukViewModel.uploadProfilePhoto(imagefile)
                    .observe(this@HomeKelolaMyGethubEditProdukActivity) { result ->
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


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }
}