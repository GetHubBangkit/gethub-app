package com.entre.gethub.ui.project.postedproject

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
import com.entre.gethub.utils.Formatter
import com.entre.gethub.utils.ViewModelFactory

class PostedProjectStatusDetailActivity : AppCompatActivity() {

    private val binding by lazy { ActivityPostedProjectStatusDetailBinding.inflate(layoutInflater) }
    private val postedProjectStatusDetailViewModel by viewModels<PostedProjectStatusDetailViewModel> {
        ViewModelFactory.getInstance(
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val projectId = intent.getStringExtra(EXTRA_PROJECT_ID)

        projectId?.let {
            getPostedProjectDetail(it)
        }

        binding.iconBack.setOnClickListener {
            finish()
        }
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
        userBiddingList: List<PostedProjectDetailResponse.UsersBidItem>,
        projectId: String
    ) {
        binding.rvUserBidding.apply {
            layoutManager = LinearLayoutManager(
                this@PostedProjectStatusDetailActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
            adapter = SelectUserBiddingAdapter(userBiddingList) { usersBidItem ->
                postedProjectStatusDetailViewModel.chooseBidder(
                    projectId,
                    usersBidItem.id.toString()
                )
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
        const val EXTRA_PROJECT_ID = "extra_project_id"
    }
}