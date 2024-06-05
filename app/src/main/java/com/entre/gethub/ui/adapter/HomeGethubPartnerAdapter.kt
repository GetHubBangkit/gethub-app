package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.NewPartnerResponse
import com.entre.gethub.databinding.ItemHomeGethubparterBinding

class HomeGethubPartnerAdapter(
    private val partnerList: List<NewPartnerResponse.Data>,
    private val listener: (NewPartnerResponse.Data, Int) -> Unit
) : RecyclerView.Adapter<HomeGethubPartnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeGethubparterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(partnerList[position])
        holder.itemView.setOnClickListener { listener(partnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return partnerList.size
    }

    class ViewHolder(private val binding: ItemHomeGethubparterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindItem(partner: NewPartnerResponse.Data) {
            // Load image using Glide
            Glide.with(binding.root)
                .load(partner.photo)
                .centerCrop()
                .placeholder(R.drawable.profilepic1) // Placeholder image
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.profilepic1)

            // Set name and profession
            binding.profilename.text = partner.fullName
            binding.profiledesc.text = partner.profession
        }
    }
}
