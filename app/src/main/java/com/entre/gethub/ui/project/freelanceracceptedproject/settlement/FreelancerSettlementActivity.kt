package com.entre.gethub.ui.project.freelanceracceptedproject.settlement

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivityFreelancerSettlementBinding
import com.entre.gethub.ui.adapter.BankAdapter
import com.entre.gethub.ui.adapter.CategoryAdapter
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

        setupProjectData()

        getFreelancerSettlement()

        getBanks()
    }

    private fun setupProjectData() {
        with(binding) {
            tvProjectTitle.text = projectTitle
            tvProjectDeadline.text = "Deadline: $projectDeadline Hari"
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
        const val EXTRA_PROJECT_TITLE = "extra_project_title"
        const val EXTRA_PROJECT_DEADLINE = "extra_project_deadline"
    }
}