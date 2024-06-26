package com.entre.gethub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ItemPostedProjectBinding
import com.entre.gethub.utils.Formatter

class PostedProjectAdapter(
    private val context: Context,
    private val postedProjectList: List<PostedProjectResponse.ProjectsItem>,
    private val listener: (PostedProjectResponse.ProjectsItem, Int) -> Unit,
    private val chatButtonListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
    private val reviewButtonListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
    private val usernameTextListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
    private val seeContractListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
) : RecyclerView.Adapter<PostedProjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPostedProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(postedProjectList[position], chatButtonListener, reviewButtonListener, usernameTextListener, seeContractListener)
        holder.itemView.setOnClickListener { listener(postedProjectList[position], position) }
    }

    override fun getItemCount(): Int = postedProjectList.size

    class ViewHolder(private val binding: ItemPostedProjectBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            project: PostedProjectResponse.ProjectsItem,
            chatButtonListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
            reviewButtonListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
            usernameTextListener: (project: PostedProjectResponse.ProjectsItem) -> Unit,
            seeContractListener: (project: PostedProjectResponse.ProjectsItem) -> Unit
        ) {
            val minBudget = Formatter.formatRupiah(project.minBudget ?: 0)
            val maxBudget = Formatter.formatRupiah(project.maxBudget ?: 0)

            with(binding) {
                tvProjectTitle.text = project.title
                tvProjectDescription.text = project.description
                tvProjectAmountRange.text = "$minBudget - $maxBudget"
                tvProjectDeadline.text = "Deadline ${project.deadlineDuration} Hari"
                tvProjectTotalUserBids.text = "Total User Bids: ${project.totalBidders} User"

                if (project.isActive != true) {
                    tvProjectVerificationStatus.text = "Tidak terverifikasi"
                }

                when (project.statusProject) {
                    "CLOSE", "FINISHED" -> {
                        clProjectUserSelected.visibility = View.VISIBLE

                        loadUserDetails(project, usernameTextListener)

                        cvChat.setOnClickListener {
                            chatButtonListener(project)
                        }

                        cvContract.setOnClickListener {
                            seeContractListener(project)
                        }

                        if (project.statusProject == "FINISHED") {
                            btnReview.visibility = View.VISIBLE
                            tvProjectStatus.text = "Selesai Dikerjakan"
                            tvProjectStatus.setTextColor(context.getColor(R.color.color_project_bid_status))
                            btnReview.setOnClickListener {
                                reviewButtonListener(project)
                            }
                        }
                    }

                    else -> {
                        clProjectUserSelected.visibility = View.GONE
                        btnReview.visibility = View.GONE
                    }
                }
            }
        }

        private fun ItemPostedProjectBinding.loadUserDetails(
            project: PostedProjectResponse.ProjectsItem,
            usernameTextListener: (project: PostedProjectResponse.ProjectsItem) -> Unit
        ) {
            val context = root.context
            val userBid = project.selectedUserBid.usersBid
            Glide.with(context)
                .load(userBid?.photo)
                .placeholder(R.drawable.profilepic1)
                .into(ivUserProfile)
            tvUserName.text = userBid?.fullName
            tvUserJobName.text = userBid?.profession
            tvProjectAmount.text = Formatter.formatRupiah(project.selectedUserBid.budgetBid ?: 0)

            tvUserName.setOnClickListener { usernameTextListener(project) }

            if (userBid?.isPremium == true) {
                ivPremium.visibility = View.VISIBLE
            }

            if (userBid?.isVerifKtp == true) {
                ivVerified.visibility = View.VISIBLE
            }
        }
    }
}
