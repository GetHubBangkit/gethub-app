package com.bangkit.gethub.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bangkit.gethub.R
import com.bangkit.gethub.data.Result
import com.bangkit.gethub.databinding.ActivityRegisterBinding
import com.bangkit.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            nameTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    nameTextField.editText?.error = getString(R.string.field_couldnt_be_empty)
                } else {
                    nameTextField.editText?.error = null
                }
            }

            emailTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    emailTextField.editText?.error = getString(R.string.field_couldnt_be_empty)
                } else if (!text.contains("@")) {
                    emailTextField.editText?.error = getString(R.string.please_input_a_valid_email)
                } else {
                    emailTextField.editText?.error = null
                }
            }

            passwordTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    passwordTextField.editText?.error = getString(R.string.field_couldnt_be_empty)
                } else if (text.length < 8) {
                    passwordTextField.editText?.error =
                        getString(R.string.password_should_not_less_than_8)
                } else {
                    passwordTextField.editText?.error = null
                }
            }

            btnToLogin.setOnClickListener {
                navigateToLoginActivity()
            }

            btnGoogle.setOnClickListener {
                showToast(getString(R.string.feature_still_on_development))
            }

            btnLinkedin.setOnClickListener {
                showToast(getString(R.string.feature_still_on_development))
            }

            btnRegister.setOnClickListener {
                val fullname = binding.nameTextField.editText!!.text.toString()
                val email = binding.emailTextField.editText!!.text.toString()
                val password = binding.passwordTextField.editText!!.text.toString()
                if (email.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                    showToast(getString(R.string.field_couldnt_be_empty))
                    return@setOnClickListener
                }
                register(fullname, email, password)
            }
        }
    }

    private fun register(fullname: String, email: String, password: String) {
        registerViewModel.register(fullname, email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showDialog(
                            this@RegisterActivity,
                            "Registrasi Berhasil",
                            "Silahkan cek email Anda untuk verifikasi"
                        )
                        with(binding) {
                            nameTextField.editText?.text?.clear()
                            emailTextField.editText?.text?.clear()
                            passwordTextField.editText?.text?.clear()
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                    else -> {
                        showLoading(false)
                        showToast(getString(R.string.something_went_wrong))
                    }
                }
            }
        }
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
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}