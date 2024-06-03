package com.entre.gethub.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.entre.gethub.databinding.ItemHomeKelolamygethubTemalayoutdesignBinding
import com.entre.gethub.ui.models.LayoutDesign

class LayoutDesignAdapter(
    private val layoutdesignList: ArrayList<LayoutDesign>,
    private val listener: (LayoutDesign, Int) -> Unit
) : RecyclerView.Adapter<LayoutDesignAdapter.ViewHolder>() {

    private var selectedBayarPosition: Int = RecyclerView.NO_POSITION
    private var selectedGratisPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeKelolamygethubTemalayoutdesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = layoutdesignList[position]
        holder.bindItem(currentItem)

        // Check if the current item is selected for recyclerViewHomeDesignGratis
        if (selectedGratisPosition == position) {
            holder.itemView.alpha = 1.0f // Highlight selected item
        } else if (selectedBayarPosition == position) {
            holder.itemView.alpha = 1.0f // Highlight selected item
        } else {
            holder.itemView.alpha = 0.5f // Dim unselected items
        }

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                if (selectedGratisPosition != adapterPosition) {
                    // Clear previous selection and select the current item for recyclerViewHomeDesignGratis
                    selectedGratisPosition = adapterPosition
                    // Unselect item for recyclerViewHomeDesignBayar
                    selectedBayarPosition = RecyclerView.NO_POSITION
                    notifyDataSetChanged()
                    listener(currentItem, adapterPosition)
                } else if (selectedBayarPosition != adapterPosition) {
                    // Clear previous selection and select the current item for recyclerViewHomeDesignBayar
                    selectedBayarPosition = adapterPosition
                    // Unselect item for recyclerViewHomeDesignGratis
                    selectedGratisPosition = RecyclerView.NO_POSITION
                    notifyDataSetChanged()
                    listener(currentItem, adapterPosition)
                }
            }
        }
    }








    override fun getItemCount(): Int {
        return layoutdesignList.size
    }

    class ViewHolder(private val binding: ItemHomeKelolamygethubTemalayoutdesignBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItem(layoutdesign: LayoutDesign) {
            Glide.with(itemView.context)
                .load(layoutdesign.image)
                .into(binding.image)
        }
    }
}