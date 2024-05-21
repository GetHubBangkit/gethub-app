package com.bangkit.gethub.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bangkit.gethub.R
import com.bangkit.gethub.ui.auth.LoginActivity
import com.bangkit.gethub.ui.completeprofile.CompleteProfileValidationActivity
import com.bangkit.gethub.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by viewModels<SplashViewModel> { ViewModelFactory.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            splashViewModel.getUserSignedInStatus().observe(this@SplashActivity) { loggedInStatus ->
                if (loggedInStatus) {
                    startActivity(
                        Intent(
                            this@SplashActivity,
                            CompleteProfileValidationActivity::class.java
                        )
                    )
                    finish()
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}