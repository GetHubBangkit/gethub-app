package com.entre.gethub.ui.home.projectbids

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
import com.entre.gethub.data.remote.response.projects.Project
import com.entre.gethub.databinding.ActivityHomeCariProjectBidsBinding
import com.entre.gethub.ui.adapter.HomeProjectBidsAdapter
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.utils.ViewModelFactory

class HomeCariProjectBidsActivity : AppCompatActivity() {
    private val binding by lazy { ActivityHomeCariProjectBidsBinding.inflate(layoutInflater) }
    private val homeCariProjectBidsViewModel by viewModels<HomeCariProjectBidsViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getProjects()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerViewProjectBid(projectBidList: List<Project>) {
        binding.rvRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(
                this@HomeCariProjectBidsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = HomeProjectBidsAdapter(projectBidList) { projectBid, position ->
                // Handling item click event
                val intent = Intent(
                    this@HomeCariProjectBidsActivity,
                    HomeDetailProjectBidsActivity::class.java
                )
                intent.putExtra(
                    "project_bid",
                    projectBid
                ) // Passing ProjectBid object to detail activity
                startActivity(intent)
            }
        }
    }

    private fun getProjects() {
        homeCariProjectBidsViewModel.getProjects().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setupRecyclerViewProjectBid(result.data.data)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showEmptyError(true, message = result.emptyError)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showEmptyError(true, message = result.error)
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyError(isError: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isError) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }
}