package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.AnaliticGetHubDilihat
import com.entre.gethub.databinding.ItemAnaliticGethubkamudilihatolehBinding


class AnaliticGetHubDilihatAdapter(
    private val gethubpartnerList: ArrayList<AnaliticGetHubDilihat>,
    private val listener: (AnaliticGetHubDilihat, Int) -> Unit
) :
    RecyclerView.Adapter<AnaliticGetHubDilihatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemAnaliticGethubkamudilihatolehBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(gethubpartnerList[position])
        holder.itemView.setOnClickListener { listener(gethubpartnerList[position], position) }
    }

    override fun getItemCount(): Int {
        return gethubpartnerList.size
    }

    class ViewHolder(var ItemHomeGethubpartnerBinding: ItemAnaliticGethubkamudilihatolehBinding) :
        RecyclerView.ViewHolder(ItemHomeGethubpartnerBinding.root) {
        fun bindItem(gethubpartner: AnaliticGetHubDilihat) {

            ItemHomeGethubpartnerBinding.profilepic1.setImageResource(gethubpartner.profilepic)
            ItemHomeGethubpartnerBinding.profilename.text = gethubpartner.profilename
            ItemHomeGethubpartnerBinding.profiledesc.text = gethubpartner.profiledesc

        }
    }
}
