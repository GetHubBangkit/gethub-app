package com.entre.gethub.ui.akun.paymenthistory

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.premium.PaymentHistoryResponse
import com.entre.gethub.databinding.ActivityPaymentHistoryBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.PaymentHistoryAdapter
import com.entre.gethub.utils.ViewModelFactory

class PaymentHistoryActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPaymentHistoryBinding.inflate(layoutInflater) }
    private val paymentHistoryViewModel by viewModels<PaymentHistoryViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val codeFromOtherActivity = intent.getIntExtra(EXTRA_CODE_FROM_OTHER_ACTIVITY, 0)

        getPaymentHistory()

        binding.iconBack.setOnClickListener {
            if (codeFromOtherActivity == 76) {
                val intent = Intent(this@PaymentHistoryActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@setOnClickListener
            }
            finish()
        }
    }

    private fun getPaymentHistory() {
        paymentHistoryViewModel.getPaymentHistory().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setupPaymentHistoryRecyclerView(result.data.data)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showError(true, result.error)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showError(true, result.emptyError)
                    }
                }
            }
        }
    }

    private fun setupPaymentHistoryRecyclerView(paymentHistoryList: List<PaymentHistoryResponse.DataItem>) {
        binding.rvPaymentHistory.apply {
            layoutManager = LinearLayoutManager(
                this@PaymentHistoryActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = PaymentHistoryAdapter(paymentHistoryList, seeDetailListener = { paymentHistory ->
                val intent = Intent(this@PaymentHistoryActivity, PaymentHistoryDetailActivity::class.java).apply {
                    putExtra(PaymentHistoryDetailActivity.EXTRA_REDIRECT_URL, paymentHistory.snapRedirect)
                }
                startActivity(intent)
            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isError) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    companion object {
        const val EXTRA_CODE_FROM_OTHER_ACTIVITY = "extra_code_from_other_activity"
    }
}