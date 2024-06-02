package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.projects.PostedProjectResponse
import com.entre.gethub.databinding.ItemPostedProjectBinding
import com.entre.gethub.utils.Formatter

class PostedProjectAdapter(
    private val postedProjectList: List<PostedProjectResponse.ProjectsItem>,
    private val listener: (PostedProjectResponse.ProjectsItem, Int) -> Unit
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
        fun bindItem(project: PostedProjectResponse.ProjectsItem) {
            val minBudget = Formatter.formatRupiah(project.minBudget ?: 0)
            val maxBudget = Formatter.formatRupiah(project.maxBudget ?: 0)

            with(binding) {
                tvProjectTitle.text = project.title
                tvProjectDescription.text = project.description
                tvProjectAmountRange.text = "$minBudget - $maxBudget"
                tvProjectDeadline.text = "Deadline ${project.deadlineDuration} Hari"
                tvProjectTotalUserBids.text = "Total User Bids: ${project.totalBidders} User"
            }
        }
    }
}