package com.entre.gethub.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.ProjectResponse
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding
import com.entre.gethub.utils.Formatter


class HomeProjectBidsAdapter(

    private val projectBidList: List<ProjectResponse.ProjectsItem>,
    private val listener: (ProjectResponse.ProjectsItem, Int) -> Unit
) :
    RecyclerView.Adapter<HomeProjectBidsAdapter.ViewHolder>() {

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
        fun bindItem(projectBid: ProjectResponse.ProjectsItem) {
            with(binding) {
                val postDate = Formatter.formatPostDate(projectBid.createdDate!!)
                val minBudget = Formatter.formatRupiah(projectBid.minBudget ?: 0)
                val maxBudget = Formatter.formatRupiah(projectBid.maxBudget ?: 0)

                // Project Owner
                Glide.with(itemView.context)
                    .load(projectBid.ownerProject?.photo)
                    .placeholder(R.drawable.profilepic2)
                    .into(ivProjectOwnerPic)
                tvProjectOwnerName.text = projectBid.ownerProject?.fullName
                tvProjectOwnerProfession.text = projectBid.ownerProject?.profession
                if (projectBid.ownerProject?.isPremium == true) {
                    ivPremium.visibility = View.VISIBLE
                }

                if (projectBid.ownerProject?.isVerifKtp == true) {
                    ivVerified.visibility = View.VISIBLE
                }

                // Project Data
                tvProjectTitle.text = projectBid.title
                tvProjectPriceRange.text = "$minBudget - $maxBudget"
                tvProjectDesc.text = projectBid.description
                tvProjectTotalUserBids.text = "Total User Bids: ${projectBid.totalBids} User"
                tvProjectPostDate.text = "Diunggah: $postDate"
                tvProjectDeadline.text = "Deadline: ${projectBid.deadlineDuration} Hari"
            }
        }
    }

}
