package com.bangkit.gethub.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.bangkit.gethub.R
import com.bangkit.gethub.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

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
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }

            btnRegister.setOnClickListener {
                // TODO register
            }

            btnGoogle.setOnClickListener {
                showToast(getString(R.string.feature_still_on_development))
            }

            btnLinkedin.setOnClickListener {
                showToast(getString(R.string.feature_still_on_development))
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