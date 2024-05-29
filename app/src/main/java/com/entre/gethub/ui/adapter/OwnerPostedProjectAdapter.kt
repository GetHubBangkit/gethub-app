package com.entre.gethub.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.ProjectStatsResponse
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding


class OwnerPostedProjectAdapter(

    private val projectBidList: List<ProjectStatsResponse.BidProjectsItem>,
    private val listener: (ProjectStatsResponse.BidProjectsItem, Int) -> Unit
) :
    RecyclerView.Adapter<OwnerPostedProjectAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemProjectRekomendasijobbidBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(projectBidList[position])
        holder.itemView.setOnClickListener { listener(projectBidList[position], position) }
    }

    override fun getItemCount(): Int {
        return projectBidList.size
    }

    class ViewHolder(private val binding: ItemProjectRekomendasijobbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItem(projectBid: ProjectStatsResponse.BidProjectsItem) {
            with(binding) {
                // Project Owner
                Glide.with(itemView.context)
                    .load(projectBid.owner?.photo)
                    .placeholder(R.drawable.profilepic2)
                    .into(ivProjectOwnerPic)
                tvProjectOwnerName.text = projectBid.owner?.fullName
                tvProjectOwnerProfession.text = projectBid.owner?.profession

                // Project Data
                tvProjectTitle.text = projectBid.title
                tvProjectPriceRange.text = "Rp ${projectBid.minBudget} - Rp ${projectBid.maxBudget}"
                tvProjectDesc.text = "projectBid.description"
                tvProjectTotalUserBids.text = "Total User Bids: 5 User"
                tvProjectPostDate.text = "Diunggah: ${projectBid.createdDate}"
                tvProjectDeadline.text = "Deadline: ${projectBid.deadlineDuration} Hari"
            }
        }
    }

}
