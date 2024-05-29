package com.entre.gethub.ui.userpublicprofile

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityUserPublicProfileBinding
import com.entre.gethub.utils.ViewModelFactory
import com.entre.gethub.utils.generateQR

class UserPublicProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserPublicProfileBinding
    private lateinit var userPublicProfileViewModel: UserPublicProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPublicProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Membuat ViewModelFactory
        val viewModelFactory = ViewModelFactory.getInstance(applicationContext)
        // Membuat ViewModel menggunakan ViewModelFactory yang telah dibuat
        userPublicProfileViewModel = ViewModelProvider(this, viewModelFactory).get(UserPublicProfileViewModel::class.java)

        // Panggil fungsi untuk mendapatkan profil publik
        setupPublicProfile()
    }


    private fun setupPublicProfile() {
        // Ambil username dari intent atau dari sumber lainnya
        val username = intent.getStringExtra("username") ?: ""

        // Panggil fungsi untuk mendapatkan profil publik
        userPublicProfileViewModel.getPublicProfile(username).observe(this, Observer { userProfileResult ->
            // Periksa hasil pemanggilan API
            when (userProfileResult) {
                is Result.Loading -> {
                    // Tampilkan indikator loading
                    // Anda dapat menambahkan logika tampilan loading di sini jika diperlukan
                }
                is Result.Success -> {
                    // Sembunyikan indikator loading
                    val user = userProfileResult.data?.data
                    user?.let {
                        with(binding) {
                            cardBaseItem.apply {
                                tvGethubName.text = it.fullName
                                tvGethubProfession.text = it.profession
                                tvGethubEmail.text = it.email
                                tvGethubAddress.text = it.address
                                tvGethubPhone.text = it.phone
                                tvGethubWebsite.text = it.web
                            }
                        }
                        // Generate QR Code
                        generateQRCode(it.qrCode ?: "")
                    }
                }
                is Result.Error -> {
                    // Tampilkan pesan kesalahan
                    // Anda dapat menambahkan logika penanganan kesalahan di sini jika diperlukan
                }
                else -> {
                    // Tampilkan pesan kesalahan umum
                    // Anda dapat menambahkan logika penanganan kesalahan umum di sini jika diperlukan
                }
            }
        })
    }

    private fun generateQRCode(content: String) {
        generateQR(binding.cardBaseItem.qrCode, content)
    }


}
