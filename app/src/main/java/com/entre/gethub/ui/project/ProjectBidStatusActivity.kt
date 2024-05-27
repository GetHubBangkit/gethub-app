package com.entre.gethub.ui.project

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.MyProjectBidResponse
import com.entre.gethub.databinding.ActivityProjectBidStatusBinding
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.MyProjectBidsAdapter
import com.entre.gethub.utils.ViewModelFactory

class ProjectBidStatusActivity : AppCompatActivity() {

    private val binding by lazy { ActivityProjectBidStatusBinding.inflate(layoutInflater) }
    private val projectBidStatusViewModel by viewModels<ProjectBidStatusViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var homeDetailProjectBidsFormId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        homeDetailProjectBidsFormId = intent.getIntExtra(EXTRA_ID_FROM_PROJECT_BID_FORM_ACTIVITY, 0)

        getMyProjectBids()

        binding.iconBack.setOnClickListener {
            if (homeDetailProjectBidsFormId!!.equals(99)) {
                startActivity(Intent(this@ProjectBidStatusActivity, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }
            finish()
        }
    }

    private fun getMyProjectBids() {
        projectBidStatusViewModel.getMyProjectBids().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        binding.tvTotalMyProjectBids.text = result.data.data?.totalBids.toString()
                        setupRecyclerView(result.data.data?.usersBid!!)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showEmptyError(true, result.emptyError)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(myProjectBidList: List<MyProjectBidResponse.UsersBidItem>) {
        binding.rvMyProjectBids.apply {
            layoutManager = LinearLayoutManager(
                this@ProjectBidStatusActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = MyProjectBidsAdapter(myProjectBidList) { projectBid, _ ->
                // Implement intent
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyError(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    companion object {
        const val EXTRA_ID_FROM_PROJECT_BID_FORM_ACTIVITY =
            "extra_id_from_project_bid_form_activity"
    }
}