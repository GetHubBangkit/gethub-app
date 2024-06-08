package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.databinding.ItemHomeBiddingdikerjakanBinding
import com.entre.gethub.utils.Formatter


class HomeBiddingDikerjakanAdapter(
    private val onWorkingProjectList: List<AcceptedProjectBidResponse.DataItem>,
) :
    RecyclerView.Adapter<HomeBiddingDikerjakanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeBiddingdikerjakanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(onWorkingProjectList[position])
    }

    override fun getItemCount(): Int {
        return onWorkingProjectList.size
    }

    class ViewHolder(var binding: ItemHomeBiddingdikerjakanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(data: AcceptedProjectBidResponse.DataItem) {
            val project = data.project
            val minBudget = Formatter.formatRupiah(project.minBudget ?: 0)
            val maxBudget = Formatter.formatRupiah(project.maxBudget ?: 0)

            with(binding) {
                Glide.with(root.context)
                    .load(project.ownerProject?.photo.toString())
                    .placeholder(R.drawable.profilepic1)
                    .into(ivOwnerPic)
                tvOwnerName.text = project.ownerProject?.fullName
                tvOwnerProfession.text = project.ownerProject?.profession

                tvProjectTitle.text = project.title
                tvProjectDeadline.text = project.description
                tvProjectBudget.text = "$minBudget - $maxBudget"
                tvProjectDeadline.text = "Deadline: ${project.deadlineDuration} Hari"
            }
        }
    }
}
