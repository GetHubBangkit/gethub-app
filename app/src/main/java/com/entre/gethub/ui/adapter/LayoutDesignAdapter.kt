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

        // Check if the current item is selected
        if (selectedBayarPosition == position || selectedGratisPosition == position) {
            holder.itemView.alpha = 1.0f // Highlight selected item
        } else {
            holder.itemView.alpha = 0.5f // Dim unselected items
        }

        holder.itemView.setOnClickListener {
            val adapterPosition = holder.adapterPosition
            if (adapterPosition != RecyclerView.NO_POSITION) {
                if (selectedBayarPosition != RecyclerView.NO_POSITION && selectedBayarPosition != adapterPosition) {
                    selectedBayarPosition = RecyclerView.NO_POSITION
                }
                if (selectedGratisPosition != RecyclerView.NO_POSITION && selectedGratisPosition != adapterPosition) {
                    selectedGratisPosition = RecyclerView.NO_POSITION
                }
                if (selectedBayarPosition == RecyclerView.NO_POSITION && selectedGratisPosition == RecyclerView.NO_POSITION) {
                    // No item selected, select the current one
                    selectedBayarPosition = adapterPosition
                } else if (selectedBayarPosition != RecyclerView.NO_POSITION) {
                    // Clear previous selection and select the current item for recyclerViewHomeDesignBayar
                    selectedBayarPosition = adapterPosition
                } else {
                    // Clear previous selection and select the current item for recyclerViewHomeDesignGratis
                    selectedGratisPosition = adapterPosition
                }
                notifyDataSetChanged()
                listener(currentItem, adapterPosition)
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