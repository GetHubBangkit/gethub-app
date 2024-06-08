package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.data.remote.response.ReysEventResponse
import com.entre.gethub.databinding.ItemHomeInformationHubBinding

class HomeInformationHubAdapter(
    private var data: List<ReysEventResponse.EventData>,
    private val clickListener: (ReysEventResponse.EventData, Int) -> Unit
) : RecyclerView.Adapter<HomeInformationHubAdapter.ViewHolder>() {

    // Function to update the dataset
    fun updateData(newData: List<ReysEventResponse.EventData>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeInformationHubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val eventData = data[position]
        holder.bind(eventData, clickListener)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: ItemHomeInformationHubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(eventData: ReysEventResponse.EventData, clickListener: (ReysEventResponse.EventData, Int) -> Unit) {
            // Bind data to views here
            Glide.with(binding.ivimage.context)
                .load(eventData.imageUrl)
                .into(binding.ivimage)
            binding.tvtitle.text = eventData.title
            binding.tvInputKategori.text = eventData.category

            // Set click listener
            binding.root.setOnClickListener { clickListener(eventData, adapterPosition) }
        }
    }
}
