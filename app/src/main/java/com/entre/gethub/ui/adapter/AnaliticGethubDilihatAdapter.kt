package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.CardViewersResponse.DataItem
import com.entre.gethub.databinding.ItemAnaliticGethubkamudilihatolehBinding

class AnaliticGethubDilihatAdapter(
    private val viewersList: List<DataItem>,
    private val listener: (DataItem, Int) -> Unit
) : RecyclerView.Adapter<AnaliticGethubDilihatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAnaliticGethubkamudilihatolehBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewer = viewersList[position]
        holder.bindItem(viewer)
        holder.itemView.setOnClickListener { listener(viewer, position) }
    }

    override fun getItemCount(): Int {
        return viewersList.size
    }

    class ViewHolder(private val binding: ItemAnaliticGethubkamudilihatolehBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindItem(viewer: DataItem) {
            Glide.with(itemView.context)
                .load(viewer.photo)
                .placeholder(R.drawable.profilepic1) // Placeholder image
                .into(binding.profilepic1)
            binding.profilename.text = viewer.fullName
            binding.profiledesc.text = viewer.profession ?: ""
        }
    }
}
