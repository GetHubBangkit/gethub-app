package com.entre.gethub.ui.home.projectbids

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
import com.entre.gethub.databinding.ItemDetailProjectbidsFormBinding
import com.entre.gethub.ui.project.freelancerbidproject.BidProjectStatusActivity
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeDetailProjectBidsFormActivity : AppCompatActivity() {

    private val binding by lazy { ItemDetailProjectbidsFormBinding.inflate(layoutInflater) }
    private val homeDetailProjectBidsFormViewModel by viewModels<HomeDetailProjectBidsFormViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val projectBidId = intent.getStringExtra(EXTRA_PROJECT_ID)
        val minBudget = Formatter.formatRupiah(intent.getIntExtra(EXTRA_MIN_BUDGET, 0))
        val maxBudget = Formatter.formatRupiah(intent.getIntExtra(EXTRA_MAX_BUDGET, 0))

        binding.tvFormPriceRange.text = "$minBudget - $maxBudget"

        binding.iconBack.setOnClickListener {
            finish()
        }

        binding.btnSend.setOnClickListener {
            val edBudgetBid = binding.budgetBidTextField.editText
            val edMessage = binding.messageTextField.editText

            edBudgetBid?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edBudgetBid.setError("Field Budget wajib diisi")
                } else {
                    edBudgetBid.error = null
                }
            }

            edMessage?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edMessage.setError("Field Pesan wajib diisi")
                } else {
                    edMessage.error = null
                }
            }

            if (edBudgetBid?.text.toString().isEmpty() || edMessage?.text.toString().isEmpty()) {
                showToast(getString(R.string.field_couldnt_be_empty))
            } else {
                val budgetBidNominal: Int = Formatter.getCurrencyValue(edBudgetBid?.text.toString())

                bidProject(projectBidId.toString(), budgetBidNominal, edMessage?.text.toString())
            }

        }
    }

    private fun bidProject(projectId: String, budgetBid: Int, message: String) {
        homeDetailProjectBidsFormViewModel.bidProject(projectId, budgetBid, message)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message!!)
                            startActivity(
                                Intent(
                                    this@HomeDetailProjectBidsFormActivity,
                                    BidProjectStatusActivity::class.java
                                ).apply {
                                    putExtra(
                                        BidProjectStatusActivity.EXTRA_ID_FROM_PROJECT_BID_FORM_ACTIVITY,
                                        99
                                    )
                                }
                            )
                            finish()
                        }

                        is Result.Error -> {
                            showLoading(false)
                            showToast(result.error)

                            if (result.errorCode == 405) {
                                showPortfolioDialog(
                                    this@HomeDetailProjectBidsFormActivity,
                                    "Portfolio tidak sesuai",
                                    result.error
                                )
                            }
                        }

                        is Result.Empty -> {
                            showLoading(false)
                            showDialogVerificationDialog(
                                this@HomeDetailProjectBidsFormActivity,
                                getString(R.string.account_is_not_verified),
                                result.emptyError
                            )
                        }
                    }
                }
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this@HomeDetailProjectBidsFormActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun showDialogVerificationDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Kirim") { dialog, _ ->
                homeDetailProjectBidsFormViewModel.regenerateVerifyEmail()
                dialog.dismiss()
                showToast("Email berhasil dikirim")
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
                showToast("Verifikasi batal")
            }
            .show()
    }

    private fun showPortfolioDialog(context: Context, title: String, message: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_MIN_BUDGET = "extra_min_budget"
        const val EXTRA_MAX_BUDGET = "extra_max_budget"
    }
}