package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.ProjectResponse
import com.entre.gethub.databinding.ActivityHomeCariProjectBidsBinding
import com.entre.gethub.ui.adapter.HomeProjectBidsAdapter
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

        binding.ivSearch.setOnClickListener {
            startActivity(Intent(this, SearchProjectActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        getProjects()
    }

    private fun setupRecyclerViewProjectBid(projectBidList: List<ProjectResponse.ProjectsItem>) {
        binding.rvRekomendasiProjectBid.apply {
            layoutManager = LinearLayoutManager(
                this@HomeCariProjectBidsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = HomeProjectBidsAdapter(projectBidList) { projectBid, _ ->
                val intent = Intent(
                    this@HomeCariProjectBidsActivity,
                    HomeDetailProjectBidsActivity::class.java
                )
                intent.putExtra(HomeDetailProjectBidsActivity.EXTRA_PROJECT_ID, projectBid.id)
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
                        setupRecyclerViewProjectBid(result.data.data?.projects!!)
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}