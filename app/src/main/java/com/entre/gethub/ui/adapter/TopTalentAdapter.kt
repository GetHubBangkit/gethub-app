package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.TopTalent
import com.entre.gethub.databinding.ItemProjectToptalentBinding


class TopTalentAdapter(
    private val toptalentList: ArrayList<TopTalent>,
    private val listener: (TopTalent, Int) -> Unit
) :
    RecyclerView.Adapter<TopTalentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemProjectToptalentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(toptalentList[position])
        holder.itemView.setOnClickListener { listener(toptalentList[position], position) }
    }

    override fun getItemCount(): Int {
        return toptalentList.size
    }

    class ViewHolder(var ItemProjectToptalentBinding: ItemProjectToptalentBinding) :
        RecyclerView.ViewHolder(ItemProjectToptalentBinding.root) {
        fun bindItem(toptalent: TopTalent) {
//            ItemProjectToptalentBinding.frame1.setImageResource(toptalent.image)
            ItemProjectToptalentBinding.profilepic1.setImageResource(toptalent.profilepic)
            ItemProjectToptalentBinding.profilename.text = toptalent.profilename
            ItemProjectToptalentBinding.profiledesc.text = toptalent.profiledesc

        }
    }
}
