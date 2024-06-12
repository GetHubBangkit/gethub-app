package com.entre.gethub.ui.project.ownerpostedproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.Result
import com.entre.gethub.data.remote.response.projects.PostedProjectDetailResponse
import com.entre.gethub.databinding.ActivityPostedProjectStatusDetailBinding
import com.entre.gethub.ui.adapter.SelectUserBiddingAdapter
import com.entre.gethub.ui.project.ownerpostedproject.payment.OwnerSettlementActivity
import com.entre.gethub.ui.userpublicprofile.UserPublicProfileActivity
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PostedProjectStatusDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostedProjectStatusDetailBinding.inflate(layoutInflater) }
    private val postedProjectStatusDetailViewModel by viewModels<PostedProjectStatusDetailViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }
    private var projectId: String = ""
    private var projectIdOnResume: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        projectId = intent.getStringExtra(EXTRA_PROJECT_ID).toString()

        projectId?.let {
            projectIdOnResume = it
            getPostedProjectDetail(it)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        getPostedProjectDetail(projectIdOnResume)
    }

    private fun getPostedProjectDetail(id: String) {
        postedProjectStatusDetailViewModel.getPostedProjectDetail(id).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)
                        val project = result.data.data
                        val minBudget = Formatter.formatRupiah(project?.minBudget ?: 0)
                        val maxBudget = Formatter.formatRupiah(project?.maxBudget ?: 0)
                        with(binding) {
                            tvProjectDetailTitle.text = project?.title
                            tvProjectDetailDescription.text = project?.description
                            tvProjectDetailAmountRange.text = "$minBudget - $maxBudget"
                            tvProjectDetailDateRange.text =
                                "${project?.minDeadline} - ${project?.maxDeadline}"

                            // owner
                            tvPosterName.text = project?.ownerProject?.fullName
                            tvPosterJobName.text = project?.ownerProject?.profession
                            Glide.with(this@PostedProjectStatusDetailActivity)
                                .load(project?.ownerProject?.photo)
                                .placeholder(R.drawable.profilepic1)
                                .into(ivPosterProfile)

                            tvPostDate.text = project?.createdDate
                            tvUserBiddingTotalPerson.text = "${project?.totalBidders ?: 0} Orang"
                            tvUserBiddingTotalOnList.text = "${project?.totalBidders ?: 0} Orang"
                        }
                        setupRecyclerViewUserBidding(
                            project = project,
                            result.data.data?.usersBid ?: emptyList(),
                            project?.id.toString()
                        )

                        if (project?.totalBidders == 0) {
                            showEmptyError(true, "Belum ada user bidding")
                        }
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

    private fun setupRecyclerViewUserBidding(
        project: PostedProjectDetailResponse.Data,
        userBiddingList: List<PostedProjectDetailResponse.UsersBidItem>,
        projectId: String
    ) {
        binding.rvUserBidding.apply {
            layoutManager = LinearLayoutManager(
                this@PostedProjectStatusDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = SelectUserBiddingAdapter(
                project = project,
                userBiddingList,
                onSelectUserBid = { usersBidItem ->
                    showDialog(
                        this@PostedProjectStatusDetailActivity,
                        "Pilih user bidding",
                        "Apakah anda yakin memilih ${usersBidItem.fullName} untuk mengerjakan proyek Anda?",
                        projectId,
                        usersBidItem.id.toString()
                    )
                },
                onClickUserBid = { usersBidItem ->
                    navigateToUserProfile(usersBidItem.username.toString())
                },
            )
        }
    }

    private fun chooseBidder(projectId: String, freelancerId: String) {
        postedProjectStatusDetailViewModel.chooseBidder(projectId, freelancerId)
            .observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> showLoading(true)
                        is Result.Success -> {
                            showLoading(false)
                            val intent = Intent(this, OwnerSettlementActivity::class.java).apply {
                                putExtra(OwnerSettlementActivity.EXTRA_PROJECT_ID, projectId)
                                putExtra(OwnerSettlementActivity.EXTRA_FREELANCER_ID, freelancerId)
                            }
                            startActivity(intent)
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

    private fun navigateToUserProfile(username: String) {
        val intent = Intent(this, UserPublicProfileActivity::class.java)
        intent.putExtra("username", username)
        startActivity(intent)
    }

    private fun showDialog(
        context: Context,
        title: String,
        message: String,
        projectId: String,
        freelancerId: String
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yakin") { dialog, _ ->
                chooseBidder(projectId, freelancerId)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}