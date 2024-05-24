package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.entre.gethub.databinding.ItemHomeKelolamygethubTemalayoutdesignBinding
import com.entre.gethub.ui.models.LayoutDesign

class LayoutDesignAdapter (
    private val layoutdesignList: ArrayList<LayoutDesign>,
    private val listener: (LayoutDesign, Int) -> Unit
) :
    RecyclerView.Adapter<LayoutDesignAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = ItemHomeKelolamygethubTemalayoutdesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(layoutdesignList[position])
        holder.itemView.setOnClickListener { listener(layoutdesignList[position], position) }
    }

    override fun getItemCount(): Int {
        return layoutdesignList.size
    }

    class ViewHolder(var ItemLayoutDesignBinding: ItemHomeKelolamygethubTemalayoutdesignBinding) :
        RecyclerView.ViewHolder(ItemLayoutDesignBinding.root) {
        fun bindItem(layoutdesign: LayoutDesign) {
            ItemLayoutDesignBinding.image.setImageResource(layoutdesign.image)

        }
    }
}
