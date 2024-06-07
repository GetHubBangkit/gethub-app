package com.entre.gethub.ui.project.freelanceracceptedproject

import android.content.Context
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
import com.entre.gethub.ui.MainActivity
import com.entre.gethub.ui.adapter.AcceptedBidAdapter
import com.entre.gethub.ui.home.projectbids.HomeMilestoneProjectBidsActivity
import com.entre.gethub.ui.project.chat.ChatActivity
import com.entre.gethub.ui.project.freelanceracceptedproject.review.OwnerReviewActivity
import com.entre.gethub.ui.project.freelanceracceptedproject.settlement.FreelancerSettlementActivity
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

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

        val codeFromFreelancerSettlementActivity = intent.getIntExtra(
            EXTRA_CODE_FROM_FREELANCER_SETTLEMENT, 0
        )

        binding.iconBack.setOnClickListener {
            if (codeFromFreelancerSettlementActivity == 108) {
                startActivity(Intent(this@AcceptedBidProjectActivity, MainActivity::class.java))
                finish()
                return@setOnClickListener
            }
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
                val intent =
                    Intent(
                        this@AcceptedBidProjectActivity,
                        ChatActivity::class.java
                    ).apply {
                        putExtra(ChatActivity.EXTRA_RECEIVER_ID, data.project.ownerId)
                        putExtra(ChatActivity.EXTRA_SENDER_ID, data.userId)
                        putExtra(ChatActivity.EXTRA_CHANNEL_ID, data.project.chatroomId)
                        putExtra(
                            ChatActivity.EXTRA_RECEIVER_NAME,
                            data.project.ownerProject?.fullName
                        )
                        putExtra(
                            ChatActivity.EXTRA_RECEIVER_PHOTO,
                            data.project.ownerProject?.photo
                        )
                    }
                startActivity(intent)

            },
                seeDetailListener = { projectId ->
                    val intent = Intent(
                        this@AcceptedBidProjectActivity,
                        HomeMilestoneProjectBidsActivity::class.java
                    ).apply {
                        putExtra(HomeMilestoneProjectBidsActivity.EXTRA_PROJECT_ID, projectId)
                    }
                    startActivity(intent)

                },
                finishProjectListener = { projectId ->
                    showDialog(
                        this@AcceptedBidProjectActivity,
                        "Tandai pekerjaan selesai",
                        "Apakah Anda yakin bahwa pekerjaan telah selesai?",
                        projectId
                    )
                },
                createSettlementListener = { project ->
                    val intent = Intent(
                        this@AcceptedBidProjectActivity,
                        FreelancerSettlementActivity::class.java
                    ).apply {
                        putExtra(FreelancerSettlementActivity.EXTRA_PROJECT_ID, project.projectId)
                        putExtra(
                            FreelancerSettlementActivity.EXTRA_PROJECT_TITLE,
                            project.project.title
                        )
                        putExtra(
                            FreelancerSettlementActivity.EXTRA_PROJECT_DEADLINE,
                            project.project.deadlineDuration
                        )
                    }

                    startActivity(intent)
                },
                reviewProjectOwnerListener = { project ->
                    val intent = Intent(this@AcceptedBidProjectActivity, OwnerReviewActivity::class.java).apply {
                        putExtra(OwnerReviewActivity.EXTRA_PROJECT_ID, project.projectId)
                        putExtra(OwnerReviewActivity.EXTRA_OWNER_ID, project.project.ownerId)
                        putExtra(OwnerReviewActivity.EXTRA_OWNER_NAME, project.project.ownerProject?.fullName)
                        putExtra(OwnerReviewActivity.EXTRA_OWNER_PROFESSION, project.project.ownerProject?.profession)
                        putExtra(OwnerReviewActivity.EXTRA_OWNER_PHOTO, project.project.ownerProject?.photo)
                    }
                    startActivity(intent)
                }
            )
        }

    }

    private fun showDialog(context: Context, title: String, message: String, projectId: String) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yakin") { _, _ ->
                acceptedBidProjectViewModel.finishProject(projectId)
                    .observe(this@AcceptedBidProjectActivity) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> showLoading(true)
                                is Result.Success -> {
                                    showLoading(false)
                                    showToast(result.data.message.toString())
                                    getAccepted()
                                }

                                is Result.Error -> {
                                    showLoading(false)
                                    showToast(result.error)
                                }

                                else -> {
                                    //
                                }
                            }
                        }
                    }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .setOnDismissListener {
                it.dismiss()
            }
            .show()
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

    companion object {
        const val EXTRA_CODE_FROM_FREELANCER_SETTLEMENT = "extra_code_from_freelancer_settlement"
    }
}