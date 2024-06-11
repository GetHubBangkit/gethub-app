package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.databinding.ItemAcceptedProjectbidBinding
import com.entre.gethub.utils.Formatter

class AcceptedBidAdapter(
    private val acceptedProjectList: List<AcceptedProjectBidResponse.DataItem>,
    private val chatButtonListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
    private val seeDetailListener: (projectId: String) -> Unit,
    private val finishProjectListener: (projectId: String) -> Unit,
    private val createSettlementListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
    private val reviewProjectOwnerListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
    private val seeContractListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
) : RecyclerView.Adapter<AcceptedBidAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAcceptedProjectbidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(
            acceptedProjectList[position],
            chatButtonListener,
            seeDetailListener,
            finishProjectListener,
            createSettlementListener,
            reviewProjectOwnerListener,
            seeContractListener
        )
    }

    override fun getItemCount(): Int = acceptedProjectList.size

    class ViewHolder(private val binding: ItemAcceptedProjectbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            projectBid: AcceptedProjectBidResponse.DataItem,
            chatButtonListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
            seeDetailListener: (projectId: String) -> Unit,
            finishProjectListener: (projectId: String) -> Unit,
            createSettlementListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
            reviewProjectOwnerListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
            seeContractListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
        ) {
            with(binding) {
                val acceptedBudget = Formatter.formatRupiah(projectBid.budgetBid ?: 0)

                // Project Owner
                projectBid.project.ownerProject?.let { owner ->
                    Glide.with(itemView.context)
                        .load(owner.photo)
                        .placeholder(R.drawable.profilepic2)
                        .into(ivUserProfile)
                    tvUserName.text = owner.fullName
                    tvUserJobName.text = owner.fullName
                }

                // Project Data
                tvProjectTitle.text = projectBid.project.title
                tvProjectDeadline.text = "Deadline: ${projectBid.project.deadlineDuration} Hari"
                tvProjectAmount.text = acceptedBudget

                cvChat.setOnClickListener {
                    chatButtonListener(projectBid)
                }

                cvContract.setOnClickListener {
                    seeContractListener(projectBid)
                }

                tvSeeDetail.setOnClickListener {
                    seeDetailListener(projectBid.projectId.toString())
                }

                when (projectBid.project.statusProject) {
                    "FINISHED" -> {
                        btnFinishProject.text = "Lakukan Settlement Pembayaran"
                        btnFinishProject.setOnClickListener {
                            createSettlementListener(projectBid)
                        }

                        if (projectBid.project.statusPayment == "SETTLEMENT") {
                            btnFinishProject.visibility = View.GONE
                            btnReviewProjectOwner.visibility = View.VISIBLE
                            btnReviewProjectOwner.setOnClickListener {
                                reviewProjectOwnerListener(projectBid)
                            }
                        }
                    }

                    else -> {
                        btnFinishProject.text = "Tandai Pekerjaan Selesai"
                        btnFinishProject.setOnClickListener {
                            finishProjectListener(projectBid.projectId.toString())
                        }
                    }
                }
            }
        }
    }
}
