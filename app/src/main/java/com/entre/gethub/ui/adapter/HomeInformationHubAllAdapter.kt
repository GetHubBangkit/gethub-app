package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.data.remote.response.InformationHubResponse
import com.entre.gethub.databinding.ItemHomeInformationHubBinding

class HomeInformationHubAllAdapter(
    private var data: List<InformationHubResponse.Data>,
    private val clickListener: (InformationHubResponse.Data, Int) -> Unit
) : RecyclerView.Adapter<HomeInformationHubAllAdapter.ViewHolder>() {

    // Function to update the dataset
    fun updateData(newData: List<InformationHubResponse.Data>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeInformationHubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val informationHub = data[position]
        holder.bind(informationHub, clickListener)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val binding: ItemHomeInformationHubBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(informationHub: InformationHubResponse.Data, clickListener: (InformationHubResponse.Data, Int) -> Unit) {
            // Bind data to views here
            Glide.with(binding.ivimage.context)
                .load(informationHub.imageUrl)
                .into(binding.ivimage)
            binding.tvtitle.text = informationHub.title
            binding.tvInputKategori.text = informationHub.category

            // Set click listener
            binding.root.setOnClickListener { clickListener(informationHub, adapterPosition) }
        }
    }
}
