package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.TopTalent
import com.entre.gethub.databinding.ItemProjectToptalentBinding

class TopTalentAdapter(
    private val toptalentList: List<TopTalent>,
    private val listener: (TopTalent, Int) -> Unit
) : RecyclerView.Adapter<TopTalentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemProjectToptalentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toptalent = toptalentList[position]
        holder.bindItem(toptalent)
        holder.itemView.setOnClickListener { listener(toptalent, position) }
    }


    override fun getItemCount(): Int {
        return toptalentList.size
    }

    class ViewHolder(private val binding: ItemProjectToptalentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(toptalent: TopTalent) {
            // Set data from TopTalent to views
            Glide.with(itemView.context)
                .load(toptalent.photo)
                .placeholder(R.drawable.profilepic1) // Placeholder image
                .into(binding.profilepic1)
            binding.profilename.text = toptalent.fullName
            binding.profiledesc.text = toptalent.profession ?: ""
        }
    }

}
