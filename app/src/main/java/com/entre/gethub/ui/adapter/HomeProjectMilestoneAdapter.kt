package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.projects.AllProjectMilestoneResponse
import com.entre.gethub.databinding.ItemProjectMilestoneBinding

class HomeProjectMilestoneAdapter(
    private val projectMilestoneList: List<AllProjectMilestoneResponse.DataItem>,
) : RecyclerView.Adapter<HomeProjectMilestoneAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val v =
            ItemProjectMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(projectMilestoneList[position])
    }

    override fun getItemCount(): Int = projectMilestoneList.size

    class ViewHolder(private val binding: ItemProjectMilestoneBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(milestone: AllProjectMilestoneResponse.DataItem) {
            with(binding) {
                tvMilestoneNumber.text = "MILESTONE ${bindingAdapterPosition + 1}"
                tvMilestoneDesc.text = milestone.taskDescription.toString()
                ivMilestoneDelete.visibility = View.GONE
            }
        }
    }
}