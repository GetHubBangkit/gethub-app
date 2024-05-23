package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.data.remote.response.SponsorResponse
import com.entre.gethub.databinding.ItemGethubSponsorBinding

class SponsorAdapter(
    private var sponsorList: List<SponsorResponse.Data>,
    private val listener: (SponsorResponse.Data, Int) -> Unit
) : RecyclerView.Adapter<SponsorAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGethubSponsorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sponsor = sponsorList[position]
        holder.bind(sponsor)
        holder.itemView.setOnClickListener { listener(sponsor, position) }
    }

    override fun getItemCount(): Int {
        return sponsorList.size
    }

    fun updateData(newSponsorList: List<SponsorResponse.Data>) {
        sponsorList = newSponsorList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemGethubSponsorBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sponsor: SponsorResponse.Data) {
            // Load the image using Glide
            Glide.with(binding.image.context)
                .load(sponsor.imageUrl)
                .into(binding.image)
        }
    }
}
