package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.data.remote.response.partners.GetHubPartner
import com.entre.gethub.databinding.CardBaseItemBinding

class GethubPartnerListAdapter (
    private val gethubPartnerList: List<GetHubPartner>,
    private val listener: (GetHubPartner, Int) -> Unit
) :
    RecyclerView.Adapter<GethubPartnerListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = CardBaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(gethubPartnerList[position])
        holder.itemView.setOnClickListener { listener(gethubPartnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return gethubPartnerList.size
    }

    class ViewHolder(private var cardBaseItemBinding: CardBaseItemBinding) :
        RecyclerView.ViewHolder(cardBaseItemBinding.root) {
        fun bindItem(gethubPartner: GetHubPartner) {
            with(cardBaseItemBinding) {
                tvGethubName.text = gethubPartner.fullName
                tvGethubProfession.text = gethubPartner.profession
                tvGethubEmail.text = gethubPartner.email
                tvGethubPhone.text = gethubPartner.phone
                tvGethubAddress.text = gethubPartner.address
                tvGethubWebsite.text = gethubPartner.website
            }
        }
    }
}
