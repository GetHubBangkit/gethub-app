package com.entre.gethub.ui.home.mygethub.scanktp

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.ml.ScanKTPResponse
import com.entre.gethub.databinding.ActivityHomeKelolamygethubUploadverifikasiktpBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeKelolaMyGethubScanKTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeKelolamygethubUploadverifikasiktpBinding
    private lateinit var viewModel: HomeKelolaMyGethubScanKTPViewModel

    private val pickPhoto =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                binding.ivFrame.setImageURI(uri)
                binding.ivDeteksi.setImageDrawable(null)
                selectedImageUri = uri
                selectedImageBitmap = null
            }
        }

    private val capturePhoto =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
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
        binding = ActivityHomeKelolamygethubUploadverifikasiktpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[HomeKelolaMyGethubScanKTPViewModel::class.java]

        binding.iconBack.setOnClickListener {
            startActivity(Intent(this, HomeKelolaMyGethubActivity::class.java))
        }

        binding.ivFrame.setOnClickListener {
            showImageSourceDialog()
        }
        binding.btnSimpan.setOnClickListener {
            uploadImage()
        }
    }
    override fun onBackPressed() {
        startActivity(Intent(this, HomeKelolaMyGethubActivity::class.java))
        finish()
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("Take a Photo", "Choose from Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> capturePhoto.launch(null)
                    1 -> pickPhoto.launch("image/*")
                }
            }
        builder.create().show()
    }

    private fun uploadImage() {
        if (selectedImageUri != null) {
            val filePath = getRealPathFromURI(this, selectedImageUri!!)
            if (filePath != null) {
                uploadImage(File(filePath))
            } else {
                Toast.makeText(this, "Error getting image file path", Toast.LENGTH_SHORT).show()
            }
        } else if (selectedImageBitmap != null) {
            val file = createTempFile()
            try {
                val out = FileOutputStream(file)
                selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()
                uploadImage(file)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(imageFile: File) {
        viewModel.uploadScanKTP(imageFile).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    handleScanKTPResponse(result.data)
                    // Setelah berhasil mengunggah gambar KTP, perbarui profil dan tentukan aktivitas selanjutnya
                    viewModel.getUserProfile().observe(this) { userProfileResult ->
                        when (userProfileResult) {
                            is Result.Success -> {
                                val isVerifKtp = userProfileResult.data.data.isVerifKtp
                                val isVerifKtpUrl = userProfileResult.data.data.isVerifKtpUrl
                                if (isVerifKtp == true) {
                                    startActivity(Intent(this, HomeKelolaMyGethubScanKTPTerverifikasiActivity::class.java))
                                } else if (isVerifKtp == false || isVerifKtpUrl != null) {
                                    startActivity(Intent(this, HomeKelolaMyGethubScanKTPMenungguVerifikasiActivity::class.java))
                                }
                            }
                            is Result.Error -> {
                                Toast.makeText(this, "Error: ${userProfileResult.error}", Toast.LENGTH_SHORT).show()
                            }
                            else -> {
                                // Handling other cases
                            }
                        }
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Handling other cases
                }
            }
        }
    }


    private fun handleScanKTPResponse(response: ScanKTPResponse) {
        Toast.makeText(this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()

    }

    private fun createTempFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        )
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
