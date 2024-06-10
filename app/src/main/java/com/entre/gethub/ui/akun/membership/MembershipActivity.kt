package com.entre.gethub.ui.akun.membership

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityMembershipBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.home.deteksiproject.HomeProjectDetectorActivity
import com.entre.gethub.utils.ViewModelFactory

class MembershipActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMembershipBinding.inflate(layoutInflater) }
    private val membershipViewModel by viewModels<MembershipViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.btnSubscribe.setOnClickListener {
            premium()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    private fun premium() {
        membershipViewModel.premium().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)

                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, MembershipPaymentActivity::class.java).apply {
                            putExtra(
                                MembershipPaymentActivity.EXTRA_REDIRECT_URL,
                                result.data.redirectUrl
                            )
                        }
                        startActivity(intent)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}