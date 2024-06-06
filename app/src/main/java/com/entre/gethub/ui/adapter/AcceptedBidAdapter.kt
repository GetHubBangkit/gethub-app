package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
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
) : RecyclerView.Adapter<AcceptedBidAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v = ItemAcceptedProjectbidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(
            acceptedProjectList[position], chatButtonListener, seeDetailListener
        )
    }

    override fun getItemCount(): Int = acceptedProjectList.size

    class ViewHolder(private val binding: ItemAcceptedProjectbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(
            projectBid: AcceptedProjectBidResponse.DataItem,
            chatButtonListener: (project: AcceptedProjectBidResponse.DataItem) -> Unit,
            seeDetailListener: (projectId: String) -> Unit,
        ) {
            with(binding) {
                val acceptedBudget = Formatter.formatRupiah(projectBid.budgetBid ?: 0)

                // Project Owner
                Glide.with(itemView.context)
                    .load(projectBid.project.ownerProject?.photo)
                    .placeholder(R.drawable.profilepic2)
                    .into(ivUserProfile)
                tvUserName.text = projectBid.project.ownerProject?.fullName
                tvUserJobName.text = projectBid.project.ownerProject?.fullName

                // Project Data
                tvProjectTitle.text = projectBid.project.title
                tvProjectDeadline.text = "Deadline: ${projectBid.project.deadlineDuration} Hari"
                tvProjectAmount.text = acceptedBudget

                cvChat.setOnClickListener {
                    chatButtonListener(projectBid)
                }

                tvSeeDetail.setOnClickListener {
                    seeDetailListener(projectBid.projectId.toString())
                }
            }
        }

    }

}