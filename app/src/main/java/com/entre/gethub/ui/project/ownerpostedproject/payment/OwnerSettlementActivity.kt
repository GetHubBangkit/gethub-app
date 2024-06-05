package com.entre.gethub.ui.project.ownerpostedproject.payment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityOwnerSettlementBinding
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory

class OwnerSettlementActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOwnerSettlementBinding.inflate(layoutInflater) }
    private val ownerSettlementViewModel by viewModels<OwnerSettlementViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        projectId = intent.getStringExtra(EXTRA_PROJECT_ID).toString()

        getSettlement(projectId)

        binding.btnPay.setOnClickListener {
            generatePaymentToken(projectId)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun getSettlement(projectId: String) {
        ownerSettlementViewModel.getSettlement(projectId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        with(binding) {
                            val deposit = Formatter.formatRupiah(result.data.data?.deposit ?: 0)
                            val serviceFee =
                                Formatter.formatRupiah(result.data.data?.serviceFee ?: 0)
                            val total = Formatter.formatRupiah(result.data.data?.total ?: 0)

                            tvDeposit.text = deposit
                            tvServices.text = serviceFee
                            tvTotal.text = total
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    else -> {
                        //
                    }
                }
            }
        }
    }

    private fun generatePaymentToken(projectId: String) {
        ownerSettlementViewModel.generatePaymentToken(projectId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(this, OwnerPaymentWebViewActivity::class.java).apply {
                            putExtra(
                                OwnerPaymentWebViewActivity.EXTRA_REDIRECT_URL,
                                result.data.data.redirectUrl
                            )
                        }
                        startActivity(intent)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    else -> {
                        //
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

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}