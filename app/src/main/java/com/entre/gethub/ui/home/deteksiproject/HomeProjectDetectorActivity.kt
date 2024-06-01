package com.entre.gethub.ui.home.deteksiproject

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ActivityHomeProjectDetectorBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.utils.ViewModelFactory
import java.io.File

class HomeProjectDetectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProjectDetectorBinding
    private lateinit var viewModel: HomeProjectDetectorViewModel

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.ivFrame.setImageURI(uri)
            binding.ivDeteksi.setImageDrawable(null)
            selectedImageUri = uri
            selectedImageBitmap = null
        }
    }

    private val capturePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            binding.ivFrame.setImageBitmap(bitmap)
            binding.ivDeteksi.setImageDrawable(null)
            selectedImageBitmap = bitmap
            selectedImageUri = null
        }
    }

    private var selectedImageUri: Uri? = null
    private var selectedImageBitmap: Bitmap? = null

    fun getRealPathFromURI(context: Context, uri: Uri): String? {
        var filePath: String? = null
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context.contentResolver.query(uri, proj, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val column_index = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(column_index)
            }
        }
        return filePath
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProjectDetectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(HomeProjectDetectorViewModel::class.java)

        binding.ivFrame.setOnClickListener {
            showImageSourceOptions()
        }

        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnSimpan.setOnClickListener {
            when {
                selectedImageUri != null -> {
                    val filePath = getRealPathFromURI(this, selectedImageUri!!)
                    Log.d("HomeProjectDetector", "File Path: $filePath")
                    if (filePath != null) {
                        val imageFile = File(filePath)
                        Log.d("HomeProjectDetector", "Image File: ${imageFile.absolutePath}")
                        viewModel.scanProject(imageFile).observe(this) { result ->
                            handleScanResult(result)
                        }
                    } else {
                        showToast("Invalid image file")
                    }
                }
                selectedImageBitmap != null -> {
                    val tempFile = createTempFile("image", ".jpg")
                    tempFile.outputStream().use { outputStream ->
                        selectedImageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    Log.d("HomeProjectDetector", "Temp File: ${tempFile.absolutePath}")
                    viewModel.scanProject(tempFile).observe(this) { result ->
                        handleScanResult(result)
                    }
                }
                else -> {
                    showToast("No image selected")
                }
            }
        }
    }

    private fun handleScanResult(result: Result<ProjectDetectorResponse>) {
        when (result) {
            is Result.Success -> {
                startActivity(Intent(this, HomeProjectDetectorDetailActivity::class.java).apply {
                    putExtra("imageUri", selectedImageUri?.toString())
                    putExtra("imageBitmap", selectedImageBitmap)
                    putExtra("fraudDetectionResult", result.data)
                })
            }
            is Result.Error -> {
                showLoading(false)
                showToast(result.error)
                Log.e("HomeProjectDetector", "Error: ${result.error}")
            }
            is Result.Loading -> {
                showLoading(true)
            }
            else -> {
                showLoading(false)
                showToast("Terjadi kesalahan")
                Log.e("HomeProjectDetector", "Unknown error")
            }
        }
    }

    private fun showImageSourceOptions() {
        val options = arrayOf("Galeri", "Kamera")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> pickPhoto.launch("image/*")
                1 -> capturePhoto.launch(null)
            }
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
