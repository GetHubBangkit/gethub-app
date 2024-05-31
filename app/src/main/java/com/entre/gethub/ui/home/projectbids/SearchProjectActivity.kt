package com.entre.gethub.ui.home.projectbids

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.SearchProjectResponse
import com.entre.gethub.databinding.ActivitySearchProjectBinding
import com.entre.gethub.ui.adapter.SearchProjectAdapter
import com.entre.gethub.utils.ViewModelFactory

class SearchProjectActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySearchProjectBinding.inflate(layoutInflater) }
    private val searchProjectViewModel by viewModels<SearchProjectViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.iconBack.setOnClickListener {
            finish()
        }

        setupSearchProject()
        showEmptyError(true, "Hasil pencarian tidak ditemukan")
    }

    private fun setupSearchProject() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView
            .editText
            .setOnEditorActionListener { textView, _, _ ->
                val query = textView.text.toString()
                if (query.isNotEmpty()) {
                    binding.tvHasilPencarian.text = "Hasil Pencarian \"$query\""
                    searchProjectViewModel.searchProjects(query).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                    showEmptyError(false, "Hasil pencarian tidak ditemukan")
                                }

                                is Result.Success -> {
                                    showLoading(false)
                                    showEmptyError(false, "Hasil pencarian tidak ditemukan")
                                    val projectBid = result.data.data?.projects
                                    setupRecyclerViewProjectBid(projectBid!!)
                                }

                                is Result.Empty -> {
                                    showLoading(false)
                                    showEmptyError(true, result.emptyError)
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showEmptyError(false, "Hasil pencarian tidak ditemukan")
                                    showToast(result.error)
                                }
                            }
                        }
                    }
                }
                binding.searchView.hide()
                false
            }
    }

    private fun setupRecyclerViewProjectBid(projectBidList: List<SearchProjectResponse.ProjectsItem>) {
        binding.rvRekomendasiProjectBid.apply {
            layoutManager =
                LinearLayoutManager(
                    this@SearchProjectActivity,
                    LinearLayoutManager.VERTICAL,
                    false,
                )
            adapter = SearchProjectAdapter(projectBidList) { project, _ ->
                val intent =
                    Intent(this@SearchProjectActivity, HomeDetailProjectBidsActivity::class.java)
                intent.putExtra(HomeDetailProjectBidsActivity.EXTRA_PROJECT_ID, project.id)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyError(isError: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isError) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
        binding.rvRekomendasiProjectBid.visibility = if (isError) View.GONE else View.VISIBLE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}