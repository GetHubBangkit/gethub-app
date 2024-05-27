package com.entre.gethub.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.Project
import com.entre.gethub.ui.models.ProjectBid
import com.entre.gethub.databinding.ItemProjectRekomendasijobbidBinding
import com.entre.gethub.utils.DateUtils


class HomeProjectBidsAdapter(

    private val projectBidList: List<Project>,
    private val listener: (Project, Int) -> Unit
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
        fun bindItem(projectBid: Project) {
            with(binding) {
                val deadline = DateUtils.calculateDeadlineDays(
                    projectBid.minDeadline!!,
                    projectBid.maxDeadline!!
                )
                ivProjectOwnerPic.setImageResource(R.drawable.profilepic2)
                tvProjectOwnerName.text = projectBid.ownerId
                tvProjectOwnerProfession.text = projectBid.ownerId
                tvProjectTitle.text = projectBid.title
                tvProjectPriceRange.text = "Rp ${projectBid.minBudget} - Rp ${projectBid.maxBudget}"
                tvProjectDesc.text = projectBid.description
                tvProjectTotalUserBids.text = "Total User Bids: 5 User"
                tvProjectPostDate.text = "Diunggah: ${projectBid.createdDate}"
                tvProjectDeadline.text = "Deadline: $deadline Hari"

            }
        }
    }

}
