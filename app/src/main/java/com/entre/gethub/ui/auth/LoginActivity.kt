package com.entre.gethub.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityLoginBinding
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationActivity
import com.entre.gethub.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory.getInstance(this) }

    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        with(binding) {
            edEmail = emailTextField.editText!!
            edPassword = passwordTextField.editText!!

            loginViewModel.getUserEmail().observe(this@LoginActivity) { email ->
                if (email != null) {
                    edEmail.setText(email)
                }
            }

            btnToRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
            }

            edEmail.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edEmail.error = getString(R.string.field_couldnt_be_empty)
                } else if (!text.contains("@")) {
                    edEmail.error = getString(R.string.please_input_a_valid_email)
                } else {
                    edEmail.error = null
                }
            }

            edPassword.doOnTextChanged { text, _, _, _ ->
                if (text!!.isEmpty()) {
                    edPassword.error = getString(R.string.field_couldnt_be_empty)
                } else if (text.length < 8) {
                    edPassword.error =
                        getString(R.string.password_should_not_less_than_8)
                } else {
                    edPassword.error = null
                }
            }

            btnLogin.setOnClickListener {
                val email = edEmail.text.toString()
                val password = edPassword.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    showToast(getString(R.string.field_couldnt_be_empty))
                    return@setOnClickListener
                }
                login(email, password)
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
                        showToast(getString(R.string.login_success))
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