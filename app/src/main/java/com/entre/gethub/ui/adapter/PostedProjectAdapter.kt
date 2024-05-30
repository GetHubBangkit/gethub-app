package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ItemPostedProjectBinding

class PostedProjectAdapter(
    private val postedProjectList: List<PostedProjectResponse.DataItem>,
    private val listener: (PostedProjectResponse.DataItem, Int) -> Unit
) : RecyclerView.Adapter<PostedProjectAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            ItemPostedProjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(postedProjectList[position])
        holder.itemView.setOnClickListener { listener(postedProjectList[position], position) }
    }

    override fun getItemCount(): Int = postedProjectList.size

    class ViewHolder(private val binding: ItemPostedProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(project: PostedProjectResponse.DataItem) {
            with(binding) {
                tvProjectTitle.text = project.title
                tvProjectDescription.text = project.description
                tvProjectAmountRange.text = "Rp ${project.minBudget} - Rp ${project.maxBudget}"
                tvProjectDeadline.text = "Deadline ${project.deadlineDuration} Hari"
                tvProjectTotalUserBids.text = "Total User Bids: 5 User"
            }
        }
    }
}