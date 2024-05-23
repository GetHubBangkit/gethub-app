package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.HomeGethubPartner
import com.entre.gethub.databinding.ItemHomeGethubparterBinding


class HomeGethubPartnerAdapter(
    private val homehomegethubpartnerList: ArrayList<HomeGethubPartner>,
    private val listener: (HomeGethubPartner, Int) -> Unit
) :
    RecyclerView.Adapter<HomeGethubPartnerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeGethubparterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(homehomegethubpartnerList[position])
        holder.itemView.setOnClickListener { listener(homehomegethubpartnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return homehomegethubpartnerList.size
    }

    class ViewHolder(var ItemHomeGethubpartnerBinding: ItemHomeGethubparterBinding) :
        RecyclerView.ViewHolder(ItemHomeGethubpartnerBinding.root) {
        fun bindItem(homehomegethubpartner: HomeGethubPartner) {

            ItemHomeGethubpartnerBinding.profilepic1.setImageResource(homehomegethubpartner.profilepic)
            ItemHomeGethubpartnerBinding.profilename.text = homehomegethubpartner.profilename
            ItemHomeGethubpartnerBinding.profiledesc.text = homehomegethubpartner.profiledesc

        }
    }
}
