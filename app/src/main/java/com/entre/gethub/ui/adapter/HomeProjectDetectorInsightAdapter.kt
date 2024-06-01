package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.ml.ProjectDetectorResponse
import com.entre.gethub.databinding.ItemHomeProjectDetectorInsightBinding

class HomeProjectDetectorInsightAdapter(
    private val dataList: List<ProjectDetectorResponse.Insight>
) : RecyclerView.Adapter<HomeProjectDetectorInsightAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemHomeProjectDetectorInsightBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(private val binding: ItemHomeProjectDetectorInsightBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ProjectDetectorResponse.Insight) {
            binding.tvInsight.text = item.text
        }
    }
}