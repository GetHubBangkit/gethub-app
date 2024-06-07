package com.entre.gethub.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityRegisterBinding
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(
            layoutInflater
        )
    }
    private val registerViewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            nameTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    nameTextField.error = getString(R.string.field_couldnt_be_empty)
                } else {
                    nameTextField.error = null
                }
            }

            emailTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    emailTextField.error = getString(R.string.field_couldnt_be_empty)
                } else if (!text.contains("@")) {
                    emailTextField.error = getString(R.string.please_input_a_valid_email)
                } else {
                    emailTextField.error = null
                }
            }

            passwordTextField.editText?.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    passwordTextField.error = getString(R.string.field_couldnt_be_empty)
                } else if (text.length < 8) {
                    passwordTextField.error =
                        getString(R.string.password_should_not_less_than_8)
                } else {
                    passwordTextField.error = null
                }
            }

            btnToLogin.setOnClickListener {
                navigateToLoginActivity()
            }

            btnGoogle.setOnClickListener {
                showToast(getString(R.string.coming_soon))
            }

            btnLinkedin.setOnClickListener {
                showToast(getString(R.string.coming_soon))
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

            tvTermsCondition.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, TermsConditionActivity::class.java))
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
