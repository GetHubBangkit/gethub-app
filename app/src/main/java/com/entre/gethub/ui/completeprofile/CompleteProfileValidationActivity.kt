package com.entre.gethub.ui.completeprofile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.databinding.ActivityCompleteProfileValidationBinding
import com.google.android.material.snackbar.Snackbar

class CompleteProfileValidationActivity : AppCompatActivity() {

    private val binding: ActivityCompleteProfileValidationBinding by lazy {
        ActivityCompleteProfileValidationBinding.inflate(
            layoutInflater
        )
    }

    private var loginResponse: LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loginResponse = intent.getParcelableExtra(EXTRA_USER)

        setupView()
    }

    private fun setupView() {
        showSnackBar("Selamat Datang, ${loginResponse?.user?.username}")

        if (loginResponse?.user?.isCompleteProfile == true) {
            // navigate to Main Activity
        }

        with(binding) {
            ivScan.setOnClickListener {
                showToast(getString(R.string.coming_soon))
            }

            ivManual.setOnClickListener {
                startActivity(
                    Intent(
                        this@CompleteProfileValidationActivity,
                        CompleteProfileActivity::class.java
                    )
                )
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}