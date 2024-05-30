package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.SearchingPartnerResponse
import com.entre.gethub.databinding.CardBaseItemBinding

class GethubPartnerSearchListAdapter (
    private val searchingPartnerList: List<SearchingPartnerResponse.Partner>,
    private val listener: (SearchingPartnerResponse.Partner, Int) -> Unit
) :
    RecyclerView.Adapter<GethubPartnerSearchListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = CardBaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(searchingPartnerList[position])
        holder.itemView.setOnClickListener { listener(searchingPartnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return searchingPartnerList.size
    }

    class ViewHolder(private var cardBaseItemBinding: CardBaseItemBinding) :
        RecyclerView.ViewHolder(cardBaseItemBinding.root) {
        fun bindItem(partner: SearchingPartnerResponse.Partner) {
            with(cardBaseItemBinding) {
                tvGethubName.text = partner.fullName
                tvGethubProfession.text = partner.profession
                tvGethubEmail.text = partner.email
                tvGethubPhone.text = partner.phone
                tvGethubAddress.text = partner.address
                tvGethubWebsite.text = partner.website
            }
        }
    }
}
