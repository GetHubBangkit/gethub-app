package com.entre.gethub.ui.home.mygethub.tentangsaya

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityHomeKelolaMyGethubEditTentangsayaBinding
import com.entre.gethub.ui.home.mygethub.HomeKelolaMyGethubActivity
import com.entre.gethub.utils.ViewModelFactory

class HomeKelolaMyGethubEditTentangSayaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeKelolaMyGethubEditTentangsayaBinding
    private val viewModel: HomeKelolaMyGethubEditTentangSayaViewModel by viewModels {
        ViewModelFactory.getInstance(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeKelolaMyGethubEditTentangsayaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        getUserProfile()
    }

    private fun setupView() {
        binding.apply {
            iconBack.setOnClickListener {
                finish()
            }

            aboutTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text.isNullOrEmpty()) {
                    aboutTextField.error = getString(R.string.about_field_couldn_be_empty)
                } else {
                    aboutTextField.error = null
                }
            }

            btnSimpan.setOnClickListener {
                val about = aboutTextField.editText?.text.toString()
                if (about.isNotEmpty()) {
                    editAbout(about)
                } else {
                    aboutTextField.error = getString(R.string.about_field_couldn_be_empty)
                }
            }
        }
    }

    private fun getUserProfile() {
        viewModel.getUserProfile().observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Empty -> {

                }
                is Result.Success -> {
                    showLoading(false)
                    val userProfile = result.data.data
                    binding.aboutTextField.editText?.setText(userProfile?.about?.toString() ?: "")

                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error ?: getString(R.string.something_went_wrong))
                }
            }
        }
    }

    private fun editAbout(about: String) {
        viewModel.editAbout(about).observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Empty -> {

                }
                is Result.Success -> {
                    showLoading(false)
                    startActivity(Intent(this, HomeKelolaMyGethubActivity::class.java))
                    finish()
                    showToast(result.data.message ?: getString(R.string.something_went_wrong))
                }
                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error ?: getString(R.string.something_went_wrong))
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
