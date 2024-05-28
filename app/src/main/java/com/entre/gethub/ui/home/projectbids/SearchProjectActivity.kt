package com.entre.gethub.ui.home.projectbids

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.entre.gethub.data.Result
import com.entre.gethub.databinding.ActivitySearchProjectBinding
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
    }

    private fun setupSearchProject() {
        binding.searchView.setupWithSearchBar(binding.searchBar)
        binding.searchView
            .editText
            .setOnEditorActionListener { textView, _, _ ->
                val query = textView.text.toString()
                if (query.isNotEmpty()) {
                    searchProjectViewModel.searchProjects(query).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    val projectBid = result.data.data
//                                    setupRecyclerViewProjectBid(projectBid)
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

                false
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