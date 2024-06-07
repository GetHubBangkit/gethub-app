package com.entre.gethub.ui.project.freelanceracceptedproject.settlement

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityFreelancerSettlementBinding
import com.entre.gethub.ui.adapter.BankAdapter
import com.entre.gethub.ui.adapter.CategoryAdapter
import com.entre.gethub.ui.project.freelanceracceptedproject.AcceptedBidProjectActivity
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory

class FreelancerSettlementActivity : AppCompatActivity() {

    private val binding by lazy { ActivityFreelancerSettlementBinding.inflate(layoutInflater) }
    private val freelancerSettlementViewModel by viewModels<FreelancerSettlementViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""
    private var projectTitle: String = ""
    private var projectDeadline: String = ""
    private var selectedBankName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        intent.apply {
            projectId = getStringExtra(EXTRA_PROJECT_ID).toString()
            projectTitle = getStringExtra(EXTRA_PROJECT_TITLE).toString()
            projectDeadline = getStringExtra(EXTRA_PROJECT_DEADLINE).toString()
        }

        setupView()

        getFreelancerSettlement()

        getBanks()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun setupView() {
        with(binding) {
            tvProjectTitle.text = projectTitle
            tvProjectDeadline.text = "Deadline: $projectDeadline Hari"

            val edRekeningNumber = accountNumberTextField.editText
            val edRekeningAccount = accountOwnerTextField.editText

            edRekeningNumber?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edRekeningNumber.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    edRekeningNumber.error = null
                }
            }

            edRekeningAccount?.doOnTextChanged { text, _, _, _ ->
                if (text.toString().isEmpty()) {
                    edRekeningAccount.setError(getString(R.string.field_couldnt_be_empty))
                } else {
                    edRekeningAccount.error = null
                }
            }

            btnPay.setOnClickListener {
                if (edRekeningNumber?.text.toString()
                        .isEmpty() || edRekeningAccount?.text.toString()
                        .isEmpty() || selectedBankName.isEmpty()
                ) {
                    Toast.makeText(
                        this@FreelancerSettlementActivity,
                        getString(R.string.field_couldnt_be_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                createSettlementFreelancer(
                    projectId = projectId,
                    rekeningAccount = edRekeningAccount?.text.toString(),
                    rekeningBank = selectedBankName,
                    rekeningNumber = edRekeningNumber?.text.toString()
                )
            }
        }
    }

    private fun getFreelancerSettlement() {
        freelancerSettlementViewModel.getFreelancerSettlement(projectId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        with(binding) {
                            tvProjectStatus.text = result.data.data?.jobStatus
                            tvOfferReceived.text =
                                Formatter.formatRupiah(result.data.data?.offerReceived ?: 0)
                            tvServices.text =
                                Formatter.formatRupiah(result.data.data?.serviceFee ?: 0)
                            tvTotal.text = Formatter.formatRupiah(result.data.data?.total ?: 0)
                        }
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        //
                    }
                }
            }
        }
    }

    private fun getBanks() {
        freelancerSettlementViewModel.getBanks().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Success -> {
                        val banks = result.data.data
                        val adapter = BankAdapter(
                            this,
                            banks
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerKategori.adapter = adapter

                        binding.spinnerKategori.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    selectedBankName = adapter.getBankName(position)
                                }

                                override fun onNothingSelected(p0: AdapterView<*>?) {
                                    // Nothing
                                }

                            }
                    }

                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun createSettlementFreelancer(
        projectId: String,
        rekeningAccount: String,
        rekeningBank: String,
        rekeningNumber: String
    ) {
        freelancerSettlementViewModel.createSettlementFreelancer(
            projectId,
            rekeningAccount,
            rekeningBank,
            rekeningNumber
        ).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val intent = Intent(
                            this@FreelancerSettlementActivity,
                            AcceptedBidProjectActivity::class.java
                        ).apply {
                            putExtra(
                                AcceptedBidProjectActivity.EXTRA_CODE_FROM_FREELANCER_SETTLEMENT,
                                108
                            )
                        }
                        startActivity(intent)
                        finish()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        Toast.makeText(
                            this@FreelancerSettlementActivity,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_PROJECT_TITLE = "extra_project_title"
        const val EXTRA_PROJECT_DEADLINE = "extra_project_deadline"
    }
}