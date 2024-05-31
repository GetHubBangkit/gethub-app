package com.entre.gethub.ui.home.deteksiproject

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.databinding.ActivityHomeProjectDetectorBinding

class HomeProjectDetectorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeProjectDetectorBinding

    private val pickPhoto = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            // Menampilkan gambar yang dipilih di ivFrame
            binding.ivFrame.setImageURI(uri)
            // Simpan URI gambar yang dipilih untuk digunakan saat menekan btnSimpan
            this.selectedImageUri = uri
        }
    }

    private val capturePhoto = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Menampilkan gambar yang diambil dari kamera di ivFrame
            binding.ivFrame.setImageBitmap(bitmap)
            // Simpan bitmap gambar yang diambil untuk digunakan saat menekan btnSimpan
            this.selectedImageBitmap = bitmap
        }
    }

    private var selectedImageUri: Uri? = null
    private var selectedImageBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeProjectDetectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur listener untuk ivFrame
        binding.ivFrame.setOnClickListener {
            // Memilih sumber gambar: galeri atau kamera
            showImageSourceOptions()
        }

        binding.btnSimpan.setOnClickListener {
            // Memeriksa apakah gambar dipilih dari galeri atau kamera sebelumnya
            if (selectedImageUri != null || selectedImageBitmap != null) {
                // Jika gambar dipilih, pindah ke HomeProjectDetectorDetailActivity
                startActivity(Intent(this, HomeProjectDetectorDetailActivity::class.java).apply {
                    putExtra("imageUri", selectedImageUri?.toString())
                    putExtra("imageBitmap", selectedImageBitmap)
                })
            } else {
                // Jika tidak, mungkin tampilkan pesan kesalahan atau tindakan lainnya
            }
        }
    }

    // Fungsi untuk menampilkan dialog pilihan galeri atau kamera
    private fun showImageSourceOptions() {
        val options = arrayOf<CharSequence>("Galeri", "Kamera")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Pilih Sumber Gambar")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    // Pilih gambar dari galeri
                    pickPhoto.launch("image/*")
                }
                1 -> {
                    // Ambil gambar dari kamera
                    capturePhoto.launch(null)
                }
            }
            dialog.dismiss()
        }
        builder.show()
    }
}
