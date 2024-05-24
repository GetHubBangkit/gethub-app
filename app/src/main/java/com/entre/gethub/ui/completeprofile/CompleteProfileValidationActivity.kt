package com.entre.gethub.ui.completeprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.auth.LoginResponse
import com.entre.gethub.databinding.ActivityCompleteProfileValidationBinding
import com.entre.gethub.ui.auth.LoginActivity
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class CompleteProfileValidationActivity : AppCompatActivity() {

    private val binding: ActivityCompleteProfileValidationBinding by lazy {
        ActivityCompleteProfileValidationBinding.inflate(
            layoutInflater
        )
    }

    private val completeProfileValidationViewModel by viewModels<CompleteProfileValidationViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    private var loginResponse: LoginResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loginResponse = intent.getParcelableExtra(EXTRA_USER)

        setupView()
        getUserProfile()
    }

    private fun setupView() {

        if (loginResponse != null) {
            showSnackBar("Selamat Datang, ${loginResponse?.user?.username}")
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

    private fun getUserProfile() {
        completeProfileValidationViewModel.getUserProfile()
            .observe(this@CompleteProfileValidationActivity) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            val user = result.data.data
                            showSnackBar("Selamat Datang, ${user?.username}")

                            if (user?.isCompleteProfile == true) {
                                startActivity(
                                    Intent(
                                        this@CompleteProfileValidationActivity,
                                        MainActivity::class.java
                                    )
                                )
                                finish()
                            }
                        }

                        is Result.Error -> {
                            showDialog(
                                this@CompleteProfileValidationActivity,
                                getString(R.string.something_went_wrong),
                                getString(
                                    R.string.please_login_again
                                )
                            )
                        }

                        else -> {
                            //
                        }
                    }
                }
            }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->
                navigateToLoginActivity()
            }
            .setOnDismissListener {
                navigateToLoginActivity()
            }
            .show()
    }

    private fun navigateToLoginActivity() {
        val intent =
            Intent(this@CompleteProfileValidationActivity, LoginActivity::class.java).addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            )
        startActivity(intent)
        finish()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}