package com.entre.gethub.ui.home.projectbids

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.AllProjectMilestoneResponse
import com.entre.gethub.databinding.ActivityHomeMilestoneProjectBidsBinding
import com.entre.gethub.ui.adapter.HomeProjectMilestoneAdapter
import com.entre.gethub.ui.adapter.ProjectMilestoneAdapter
import com.entre.gethub.utils.ViewModelFactory

class HomeMilestoneProjectBidsActivity : AppCompatActivity() {

    private val binding by lazy { ActivityHomeMilestoneProjectBidsBinding.inflate(layoutInflater) }
    private val homeMilestoneProjectBidsViewModel by viewModels<HomeMilestoneProjectBidsViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }

        projectId = intent.getStringExtra(EXTRA_PROJECT_ID).toString()

        getProjectMilestone(projectId)
    }

    private fun getProjectMilestone(projectId: String) {
        homeMilestoneProjectBidsViewModel.getMilestone(projectId).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        showEmpty(false, "")
                        setupMilestoneRecyclerView(result.data.data.sortedBy { it.taskNumber })
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showEmpty(true, result.emptyError)
                    }
                }
            }
        }
    }

    private fun setupMilestoneRecyclerView(projectMilestoneList: List<AllProjectMilestoneResponse.DataItem>) {
        binding.rvMilestone.apply {
            layoutManager = LinearLayoutManager(
                this@HomeMilestoneProjectBidsActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = HomeProjectMilestoneAdapter(projectMilestoneList)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmpty(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    companion object {
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}