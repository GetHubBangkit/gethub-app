package com.entre.gethub.ui.project.postedproject

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ActivityPostedProjectStatusBinding
import com.entre.gethub.ui.adapter.PostedProjectAdapter
import com.entre.gethub.utils.ViewModelFactory

class PostedProjectStatusActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostedProjectStatusBinding.inflate(layoutInflater) }
    private val postedProjectStatusViewModel by viewModels<PostedProjectStatusViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getPostedProject()

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getPostedProject()
    }

    private fun setupRecyclerView(postedProjectList: List<PostedProjectResponse.ProjectsItem>) {
        binding.rvPostedProject.apply {
            layoutManager = LinearLayoutManager(
                this@PostedProjectStatusActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = PostedProjectAdapter(postedProjectList) { project, _ ->
                showToast(project.title.toString())
            }
        }
    }

    private fun getPostedProject() {
        postedProjectStatusViewModel.getPostedProjects().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setupRecyclerView(result.data.data?.projects ?: emptyList())
                        binding.tvTotalPostedProject.text = result.data.data?.projects?.size.toString()
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(result.error)
                    }

                    is Result.Empty -> {
                        binding.tvTotalPostedProject.text = "0"
                        showEmptyError(true, result.emptyError)
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

    private fun showEmptyError(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }
}