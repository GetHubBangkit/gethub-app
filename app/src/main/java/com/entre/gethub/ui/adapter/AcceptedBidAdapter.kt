package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.projects.AcceptedProjectBidResponse
import com.entre.gethub.databinding.ItemAcceptedProjectbidBinding
import com.entre.gethub.utils.Formatter

class AcceptedBidAdapter(
    private val acceptedProjectList: List<AcceptedProjectBidResponse.DataItem>,
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
        holder.bindItem(acceptedProjectList[position])
    }

    override fun getItemCount(): Int = acceptedProjectList.size

    class ViewHolder(private val binding: ItemAcceptedProjectbidBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(projectBid: AcceptedProjectBidResponse.DataItem) {
            with(binding) {
                val acceptedBudget = Formatter.formatRupiah(projectBid.budgetBid ?: 0)

                // Project Owner
//                Glide.with(itemView.context)
//                    .load(projectBid.project.ownerProject?.photo)
//                    .placeholder(R.drawable.profilepic2)
//                    .into(ivProjectOwnerPic)
//                tvProjectOwnerName.text = projectBid.project.ownerProject?.fullName
//                tvProjectOwnerProfession.text = projectBid.project.ownerProject?.fullName

                // Project Data
                tvProjectTitle.text = projectBid.project?.title
                tvProjectDeadline.text = "Deadline: ${projectBid.project?.deadlineDuration} Hari"
                tvProjectAmount.text = acceptedBudget
            }
        }

    }

}