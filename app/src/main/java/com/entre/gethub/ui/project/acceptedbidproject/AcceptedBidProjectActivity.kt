package com.entre.gethub.ui.project.acceptedbidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.databinding.ActivityAcceptedBidProjectBinding
import com.entre.gethub.ui.adapter.AcceptedBidAdapter
import com.entre.gethub.ui.project.chat.ChatActivity
import com.entre.gethub.utils.ViewModelFactory

class AcceptedBidProjectActivity : AppCompatActivity() {

    private val binding by lazy { ActivityAcceptedBidProjectBinding.inflate(layoutInflater) }
    private val acceptedBidProjectViewModel by viewModels<AcceptedBidProjectViewModel> {
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

        getAccepted()
    }

    override fun onResume() {
        super.onResume()
        getAccepted()
    }

    private fun getAccepted() {
        acceptedBidProjectViewModel.getAcceptedBid().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        setupAcceptedBidRecyclerView(result.data.data)
                        binding.tvTotalMyProjectBids.text = "${result.data.data.size}"
                    }

                    is Result.Empty -> {
                        showLoading(false)
                        showEmpty(true, result.emptyError)
                        binding.tvTotalMyProjectBids.text = "0"
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showEmpty(true, result.error)
//                        showToast(result.error)
                    }
                }
            }
        }
    }

    private fun setupAcceptedBidRecyclerView(acceptedProjectList: List<AcceptedProjectBidResponse.DataItem>) {
        binding.rvAcceptedBids.apply {
            layoutManager = LinearLayoutManager(
                this@AcceptedBidProjectActivity,
                LinearLayoutManager.VERTICAL,
                false
            )

            adapter = AcceptedBidAdapter(acceptedProjectList, chatButtonListener = { data ->
                run {
                    val intent =
                        Intent(this@AcceptedBidProjectActivity, ChatActivity::class.java).apply {
                            putExtra(ChatActivity.EXTRA_OWNER_ID, data.project.ownerId)
                            putExtra(ChatActivity.EXTRA_FREELANCER_ID, data.userId)
                            putExtra(ChatActivity.EXTRA_CHANNEL_ID, data.project.chatroomId)
                            putExtra(ChatActivity.EXTRA_OWNER_NAME, data.project.ownerProject?.fullName)
                            putExtra(ChatActivity.EXTRA_OWNER_PHOTO, data.project.ownerProject?.photo)
                        }
                    startActivity(intent)
                }

            })
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmpty(isEmpty: Boolean, message: String) {
        binding.empty.llEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
        binding.empty.tvEmpty.text = message
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}