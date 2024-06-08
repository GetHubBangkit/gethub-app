package com.entre.gethub.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.profiles.UserProfileResponse
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.completeprofile.CompleteProfileValidationActivity
import com.entre.gethub.ui.onboarding.OnboardingActivity
import com.entre.gethub.utils.ViewModelFactory
import kotlinx.coroutines.*
import java.lang.Exception

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModels<SplashViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            splashViewModel.getUserSignedInStatus().observe(this@SplashActivity) { loggedInStatus ->
                if (loggedInStatus) {
                    getUserProfile()
                } else {
                    startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun getUserProfile() {
        splashViewModel.getUserProfile().observe(this@SplashActivity) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val user = result.data.data
                    if (user.isCompleteProfile == true) {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@SplashActivity, CompleteProfileValidationActivity::class.java))
                        finish()
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    showDialog(
                        this@SplashActivity,
                        getString(R.string.something_went_wrong),
                        getString(R.string.please_login_again)
                    )
                }
                else -> {

                }
            }
        }
    }

    private fun showSnackBar(message: String) {
        // Implement your snackbar logic here
    }

    private fun showLoading(isLoading: Boolean) {
        // Implement your loading indicator logic here
    }

    private fun showDialog(context: SplashActivity, title: String, message: String) {
        // Implement your dialog logic here
    }
}
