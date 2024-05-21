package com.bangkit.gethub.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.bangkit.gethub.R
import com.bangkit.gethub.data.Result
import com.bangkit.gethub.databinding.ActivityLoginBinding
import com.bangkit.gethub.ui.completeprofile.CompleteProfileValidationActivity
import com.bangkit.gethub.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            btnToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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

            btnLogin.setOnClickListener {

            }

        }
    }

    private fun login(email: String, password: String) {
        loginViewModel.login(email, password).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        val user = result.data.user
                        showLoading(false)
                        if (user?.isCompleteProfile == null || user.isCompleteProfile == false) {
                            val intent = Intent(
                                this@LoginActivity,
                                CompleteProfileValidationActivity::class.java
                            )
                                .putExtra(CompleteProfileValidationActivity.EXTRA_USER, result.data)
                            startActivity(intent)
                            finish()
                        } else {
                            // go to main activity
                        }
                        showToast("Login berhasil")
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}