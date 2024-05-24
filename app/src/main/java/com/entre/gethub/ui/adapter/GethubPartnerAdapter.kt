package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.R
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.databinding.ItemGethubPartnerBinding


class GethubPartnerAdapter(
    private val gethubPartnerList: List<GetHubPartner>,
    private val listener: (GetHubPartner, Int) -> Unit
) :
    RecyclerView.Adapter<GethubPartnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemGethubPartnerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(gethubPartnerList[position])
        holder.itemView.setOnClickListener { listener(gethubPartnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return gethubPartnerList.size
    }

    class ViewHolder(var binding: ItemGethubPartnerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(gethubpartner: GetHubPartner) {
            Glide.with(itemView.context)
                .load(gethubpartner.photo)
                .placeholder(R.drawable.profilepic1)
                .into(binding.profilepic1)

            binding.profilename.text = gethubpartner.fullName
            binding.profiledesc.text = gethubpartner.profession
        }
    }
}
