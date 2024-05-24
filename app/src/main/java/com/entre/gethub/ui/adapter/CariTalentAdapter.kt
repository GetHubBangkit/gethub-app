package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.databinding.ItemHomeCariTalentBinding
import com.entre.gethub.ui.home.caritalent.CariTalent

class CariTalentAdapter(
    private val caritalentList: ArrayList<CariTalent>,
    private val listener: (CariTalent, Int) -> Unit
) :
    RecyclerView.Adapter<CariTalentAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeCariTalentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(caritalentList[position])
        holder.itemView.setOnClickListener { listener(caritalentList[position], position) }
    }

    override fun getItemCount(): Int {
        return caritalentList.size
    }

    class ViewHolder(var binding: ItemHomeCariTalentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(caritalent: CariTalent) {
//            binding.frame1.setImageResource(caritalent.image)
            binding.profilepic1.setImageResource(caritalent.profilepic)
            binding.profilename.text = caritalent.profilename
            binding.profiledesc.text = caritalent.profiledesc

        }
    }
}
